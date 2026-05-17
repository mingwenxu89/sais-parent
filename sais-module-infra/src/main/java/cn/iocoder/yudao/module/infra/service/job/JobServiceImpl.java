package cn.iocoder.yudao.module.infra.service.job;

import cn.hutool.extra.spring.SpringUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.quartz.core.handler.JobHandler;
import cn.iocoder.yudao.framework.quartz.core.scheduler.SchedulerManager;
import cn.iocoder.yudao.framework.quartz.core.util.CronUtils;
import cn.iocoder.yudao.module.infra.controller.admin.job.vo.job.JobPageReqVO;
import cn.iocoder.yudao.module.infra.controller.admin.job.vo.job.JobSaveReqVO;
import cn.iocoder.yudao.module.infra.dal.dataobject.job.JobDO;
import cn.iocoder.yudao.module.infra.dal.mysql.job.JobMapper;
import cn.iocoder.yudao.module.infra.enums.job.JobStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.Objects;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.containsAny;
import static cn.iocoder.yudao.module.infra.enums.ErrorCodeConstants.*;

/**
 * Scheduled task Service implementation class
 *
 * @author Yudao Source Code
 */
@Service
@Validated
@Slf4j
public class JobServiceImpl implements JobService {

    @Resource
    private JobMapper jobMapper;

    @Resource
    private SchedulerManager schedulerManager;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createJob(JobSaveReqVO createReqVO) throws SchedulerException {
        validateCronExpression(createReqVO.getCronExpression());
        // 1.1 Verify uniqueness
        if (jobMapper.selectByHandlerName(createReqVO.getHandlerName()) != null) {
            throw exception(JOB_HANDLER_EXISTS);
        }
        // 1.2 Verify whether JobHandler exists
        validateJobHandlerExists(createReqVO.getHandlerName());

        // 2. Insert JobDO
        JobDO job = BeanUtils.toBean(createReqVO, JobDO.class);
        job.setStatus(JobStatusEnum.INIT.getStatus());
        fillJobMonitorTimeoutEmpty(job);
        jobMapper.insert(job);

        // 3.1 Add Job to Quartz
        schedulerManager.addJob(job.getId(), job.getHandlerName(), job.getHandlerParam(), job.getCronExpression(),
                createReqVO.getRetryCount(), createReqVO.getRetryInterval());
        // 3.2 Update JobDO
        JobDO updateObj = JobDO.builder().id(job.getId()).status(JobStatusEnum.NORMAL.getStatus()).build();
        jobMapper.updateById(updateObj);
        return job.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateJob(JobSaveReqVO updateReqVO) throws SchedulerException {
        validateCronExpression(updateReqVO.getCronExpression());
        // 1.1 Verify existence
        JobDO job = validateJobExists(updateReqVO.getId());
        // 1.2 It can only be modified if it is in the open state. The reason is that if the Quartz Job is in the paused state, modifying the Quartz Job will cause the task to start executing again.
        if (!job.getStatus().equals(JobStatusEnum.NORMAL.getStatus())) {
            throw exception(JOB_UPDATE_ONLY_NORMAL_STATUS);
        }
        // 1.3 Verify whether JobHandler exists
        validateJobHandlerExists(updateReqVO.getHandlerName());

        // 2. Update JobDO
        JobDO updateObj = BeanUtils.toBean(updateReqVO, JobDO.class);
        fillJobMonitorTimeoutEmpty(updateObj);
        jobMapper.updateById(updateObj);

        // 3. Update Job to Quartz
        schedulerManager.updateJob(job.getHandlerName(), updateReqVO.getHandlerParam(), updateReqVO.getCronExpression(),
                updateReqVO.getRetryCount(), updateReqVO.getRetryInterval());
    }

    private void validateJobHandlerExists(String handlerName) {
        try {
            Object handler = SpringUtil.getBean(handlerName);
            assert handler != null;
            if (!(handler instanceof JobHandler)) {
                throw exception(JOB_HANDLER_BEAN_TYPE_ERROR);
            }
        } catch (NoSuchBeanDefinitionException e) {
            throw exception(JOB_HANDLER_BEAN_NOT_EXISTS);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateJobStatus(Long id, Integer status) throws SchedulerException {
        // Check status
        if (!containsAny(status, JobStatusEnum.NORMAL.getStatus(), JobStatusEnum.STOP.getStatus())) {
            throw exception(JOB_CHANGE_STATUS_INVALID);
        }
        // Check existence
        JobDO job = validateJobExists(id);
        // Verify whether it is already in the current state
        if (job.getStatus().equals(status)) {
            throw exception(JOB_CHANGE_STATUS_EQUALS);
        }
        // Update job status
        JobDO updateObj = JobDO.builder().id(id).status(status).build();
        jobMapper.updateById(updateObj);

        // Update status Job to Quartz
        if (JobStatusEnum.NORMAL.getStatus().equals(status)) { // turn on
            schedulerManager.resumeJob(job.getHandlerName());
        } else { // pause
            schedulerManager.pauseJob(job.getHandlerName());
        }
    }

    @Override
    public void triggerJob(Long id) throws SchedulerException {
        // Check existence
        JobDO job = validateJobExists(id);

        // Trigger Job in Quartz
        schedulerManager.triggerJob(job.getId(), job.getHandlerName(), job.getHandlerParam());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void syncJob() throws SchedulerException {
        // 1. Query Job configuration
        List<JobDO> jobList = jobMapper.selectList();

        // 2. Traversal processing
        for (JobDO job : jobList) {
            // 2.1 Delete first, then create
            schedulerManager.deleteJob(job.getHandlerName());
            schedulerManager.addJob(job.getId(), job.getHandlerName(), job.getHandlerParam(), job.getCronExpression(),
                    job.getRetryCount(), job.getRetryInterval());
            // 2.2 If status is paused, it needs to be paused
            if (Objects.equals(job.getStatus(), JobStatusEnum.STOP.getStatus())) {
                schedulerManager.pauseJob(job.getHandlerName());
            }
            log.info("[syncJob][id({}) handlerName({}) synchronization completed]", job.getId(), job.getHandlerName());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteJob(Long id) throws SchedulerException {
        // Check existence
        JobDO job = validateJobExists(id);
        // Update
        jobMapper.deleteById(id);

        // Delete Job into Quartz
        schedulerManager.deleteJob(job.getHandlerName());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteJobList(List<Long> ids) throws SchedulerException {
        // Batch delete
        List<JobDO> jobs = jobMapper.selectByIds(ids);
        jobMapper.deleteByIds(ids);

        // Delete Job into Quartz
        for (JobDO job : jobs) {
            schedulerManager.deleteJob(job.getHandlerName());
        }
    }

    private JobDO validateJobExists(Long id) {
        JobDO job = jobMapper.selectById(id);
        if (job == null) {
            throw exception(JOB_NOT_EXISTS);
        }
        return job;
    }

    private void validateCronExpression(String cronExpression) {
        if (!CronUtils.isValid(cronExpression)) {
            throw exception(JOB_CRON_EXPRESSION_VALID);
        }
    }

    @Override
    public JobDO getJob(Long id) {
        return jobMapper.selectById(id);
    }

    @Override
    public PageResult<JobDO> getJobPage(JobPageReqVO pageReqVO) {
        return jobMapper.selectPage(pageReqVO);
    }

    private static void fillJobMonitorTimeoutEmpty(JobDO job) {
        if (job.getMonitorTimeout() == null) {
            job.setMonitorTimeout(0);
        }
    }

}
