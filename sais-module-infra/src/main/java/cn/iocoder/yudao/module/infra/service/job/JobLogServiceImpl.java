package cn.iocoder.yudao.module.infra.service.job;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.infra.controller.admin.job.vo.log.JobLogPageReqVO;
import cn.iocoder.yudao.module.infra.dal.dataobject.job.JobLogDO;
import cn.iocoder.yudao.module.infra.dal.mysql.job.JobLogMapper;
import cn.iocoder.yudao.module.infra.enums.job.JobLogStatusEnum;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

/**
 * Job log service implementation class
 *
 * @author Yudao Source Code
 */
@Service
@Validated
@Slf4j
public class JobLogServiceImpl implements JobLogService {

    @Resource
    private JobLogMapper jobLogMapper;

    @Override
    public Long createJobLog(Long jobId, LocalDateTime beginTime,
                             String jobHandlerName, String jobHandlerParam, Integer executeIndex) {
        JobLogDO log = JobLogDO.builder().jobId(jobId).handlerName(jobHandlerName)
                .handlerParam(jobHandlerParam).executeIndex(executeIndex)
                .beginTime(beginTime).status(JobLogStatusEnum.RUNNING.getStatus()).build();
        jobLogMapper.insert(log);
        return log.getId();
    }

    @Override
    @Async
    public void updateJobLogResultAsync(Long logId, LocalDateTime endTime, Integer duration, boolean success, String result) {
        try {
            JobLogDO updateObj = JobLogDO.builder().id(logId).endTime(endTime).duration(duration)
                    .status(success ? JobLogStatusEnum.SUCCESS.getStatus() : JobLogStatusEnum.FAILURE.getStatus())
                    .result(result).build();
            jobLogMapper.updateById(updateObj);
        } catch (Exception ex) {
            log.error("[updateJobLogResultAsync][logId({}) endTime({}) duration({}) success({}) result({})]",
                    logId, endTime, duration, success, result);
        }
    }

    @Override
    @SuppressWarnings("DuplicatedCode")
    public Integer cleanJobLog(Integer exceedDay, Integer deleteLimit) {
        int count = 0;
        LocalDateTime expireDate = LocalDateTime.now().minusDays(exceedDay);
        // Delete in a loop until there is no data that meets the condition
        for (int i = 0; i < Short.MAX_VALUE; i++) {
            int deleteCount = jobLogMapper.deleteByCreateTimeLt(expireDate, deleteLimit);
            count += deleteCount;
            // Reaching the expected ID of deletions means it’s over
            if (deleteCount < deleteLimit) {
                break;
            }
        }
        return count;
    }

    @Override
    public JobLogDO getJobLog(Long id) {
        return jobLogMapper.selectById(id);
    }

    @Override
    public PageResult<JobLogDO> getJobLogPage(JobLogPageReqVO pageReqVO) {
        return jobLogMapper.selectPage(pageReqVO);
    }

}
