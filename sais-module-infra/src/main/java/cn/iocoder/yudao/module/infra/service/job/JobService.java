package cn.iocoder.yudao.module.infra.service.job;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.infra.controller.admin.job.vo.job.JobPageReqVO;
import cn.iocoder.yudao.module.infra.controller.admin.job.vo.job.JobSaveReqVO;
import cn.iocoder.yudao.module.infra.dal.dataobject.job.JobDO;
import jakarta.validation.Valid;
import org.quartz.SchedulerException;

import java.util.List;

/**
 * Scheduled task service API
 *
 * @author Yudao Source Code
 */
public interface JobService {

    /**
     * Create a scheduled task
     *
     * @param createReqVO Create information
     * @return ID
     */
    Long createJob(@Valid JobSaveReqVO createReqVO) throws SchedulerException;

    /**
     * Update scheduled tasks
     *
     * @param updateReqVO Update information
     */
    void updateJob(@Valid JobSaveReqVO updateReqVO) throws SchedulerException;

    /**
     * Update the status of scheduled tasks
     *
     * @param id     Task ID
     * @param status Status
     */
    void updateJobStatus(Long id, Integer status) throws SchedulerException;

    /**
     * Trigger scheduled tasks
     *
     * @param id Task ID
     */
    void triggerJob(Long id) throws SchedulerException;

    /**
     * Synchronize scheduled tasks
     *
     * Purpose: Force the job information stored by yourself to be synchronized to Quartz
     */
    void syncJob() throws SchedulerException;

    /**
     * Delete scheduled tasks
     *
     * @param id ID
     */
    void deleteJob(Long id) throws SchedulerException;

    /**
     * Delete scheduled tasks in batches
     *
     * @param ids IDed list
     */
    void deleteJobList(List<Long> ids) throws SchedulerException;

    /**
     * Get scheduled tasks
     *
     * @param id ID
     * @return scheduled tasks
     */
    JobDO getJob(Long id);

    /**
     * Get scheduled task pagination
     *
     * @param pageReqVO Page query
     * @return Scheduled task paging
     */
    PageResult<JobDO> getJobPage(JobPageReqVO pageReqVO);

}
