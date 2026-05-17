package cn.iocoder.yudao.module.infra.service.job;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.quartz.core.service.JobLogFrameworkService;
import cn.iocoder.yudao.module.infra.controller.admin.job.vo.log.JobLogPageReqVO;
import cn.iocoder.yudao.module.infra.dal.dataobject.job.JobLogDO;

/**
 * Job log service API
 *
 * @author Yudao Source Code
 */
public interface JobLogService extends JobLogFrameworkService {

    /**
     * Get scheduled tasks
     *
     * @param id ID
     * @return scheduled tasks
     */
    JobLogDO getJobLog(Long id);

    /**
     * Get scheduled task pagination
     *
     * @param pageReqVO Page query
     * @return Scheduled task paging
     */
    PageResult<JobLogDO> getJobLogPage(JobLogPageReqVO pageReqVO);

    /**
     * Clear task logs from exceedDay days ago
     *
     * @param exceedDay   How many days will it take to clean up?
     * @param deleteLimit ID of intervals to clear
     */
    Integer cleanJobLog(Integer exceedDay, Integer deleteLimit);

}
