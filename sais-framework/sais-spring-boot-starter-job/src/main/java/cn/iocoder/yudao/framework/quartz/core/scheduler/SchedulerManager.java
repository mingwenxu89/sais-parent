package cn.iocoder.yudao.framework.quartz.core.scheduler;

import cn.iocoder.yudao.framework.quartz.core.enums.JobDataKeyEnum;
import cn.iocoder.yudao.framework.quartz.core.handler.JobHandlerInvoker;
import org.quartz.*;

import static cn.iocoder.yudao.framework.common.exception.enums.GlobalErrorCodeConstants.NOT_IMPLEMENTED;
import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception0;

/**
 * The manager of {@link org.quartz.Scheduler}, responsible for creating tasks
 *
 * Considering the simplicity of implementation, we use jobHandlerName as the unique identifier, that is:
 * 1. Job’s {@link JobDetail#getKey()}
 * 2. Trigger’s {@link Trigger#getKey()}
 *
 * In addition, jobHandlerName corresponds to the name of Spring Bean, which can be called directly
 *
 * @author Yudao Source Code
 */
public class SchedulerManager {

    private final Scheduler scheduler;

    public SchedulerManager(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    /**
     * Add Job to Quartz
     *
     * @param jobId Task number
     * @param jobHandlerName Task handler name
     * @param jobHandlerParam Task handler parameters
     * @param cronExpression CRON expression
     * @param retryCount Number of retries
     * @param retryInterval Retry interval
     * @throws SchedulerException Add exception
     */
    public void addJob(Long jobId, String jobHandlerName, String jobHandlerParam, String cronExpression,
                       Integer retryCount, Integer retryInterval)
            throws SchedulerException {
        validateScheduler();
        // Create JobDetail object
        JobDetail jobDetail = JobBuilder.newJob(JobHandlerInvoker.class)
                .usingJobData(JobDataKeyEnum.JOB_ID.name(), jobId)
                .usingJobData(JobDataKeyEnum.JOB_HANDLER_NAME.name(), jobHandlerName)
                .withIdentity(jobHandlerName).build();
        // Create a Trigger object
        Trigger trigger = this.buildTrigger(jobHandlerName, jobHandlerParam, cronExpression, retryCount, retryInterval);
        // Added Job Scheduling
        scheduler.scheduleJob(jobDetail, trigger);
    }

    /**
     * Update Job to Quartz
     *
     * @param jobHandlerName Task handler name
     * @param jobHandlerParam Task handler parameters
     * @param cronExpression CRON expression
     * @param retryCount Number of retries
     * @param retryInterval Retry interval
     * @throws SchedulerException update exception
     */
    public void updateJob(String jobHandlerName, String jobHandlerParam, String cronExpression,
                          Integer retryCount, Integer retryInterval)
            throws SchedulerException {
        validateScheduler();
        // Create a new Trigger object
        Trigger newTrigger = this.buildTrigger(jobHandlerName, jobHandlerParam, cronExpression, retryCount, retryInterval);
        // Modify schedule
        scheduler.rescheduleJob(new TriggerKey(jobHandlerName), newTrigger);
    }

    /**
     * Delete Job in Quartz
     *
     * @param jobHandlerName Task handler name
     * @throws SchedulerException delete exception
     */
    public void deleteJob(String jobHandlerName) throws SchedulerException {
        validateScheduler();
        // Pause the Trigger object
        scheduler.pauseTrigger(new TriggerKey(jobHandlerName));
        // Cancel and delete job schedule
        scheduler.unscheduleJob(new TriggerKey(jobHandlerName));
        scheduler.deleteJob(new JobKey(jobHandlerName));
    }

    /**
     * Pause Job in Quartz
     *
     * @param jobHandlerName Task handler name
     * @throws SchedulerException Pause exception
     */
    public void pauseJob(String jobHandlerName) throws SchedulerException {
        validateScheduler();
        scheduler.pauseJob(new JobKey(jobHandlerName));
    }

    /**
     * Start a Job in Quartz
     *
     * @param jobHandlerName Task handler name
     * @throws SchedulerException startup exception
     */
    public void resumeJob(String jobHandlerName) throws SchedulerException {
        validateScheduler();
        scheduler.resumeJob(new JobKey(jobHandlerName));
        scheduler.resumeTrigger(new TriggerKey(jobHandlerName));
    }

    /**
     * Immediately trigger a Job in Quartz
     *
     * @param jobId Task number
     * @param jobHandlerName Task handler name
     * @param jobHandlerParam Task handler parameters
     * @throws SchedulerException triggers exception
     */
    public void triggerJob(Long jobId, String jobHandlerName, String jobHandlerParam)
            throws SchedulerException {
        validateScheduler();
        // trigger task
        JobDataMap data = new JobDataMap(); // No need to retry, so retryCount and retryInterval are not set
        data.put(JobDataKeyEnum.JOB_ID.name(), jobId);
        data.put(JobDataKeyEnum.JOB_HANDLER_NAME.name(), jobHandlerName);
        data.put(JobDataKeyEnum.JOB_HANDLER_PARAM.name(), jobHandlerParam);
        scheduler.triggerJob(new JobKey(jobHandlerName), data);
    }

    private Trigger buildTrigger(String jobHandlerName, String jobHandlerParam, String cronExpression,
                                 Integer retryCount, Integer retryInterval) {
        return TriggerBuilder.newTrigger()
                .withIdentity(jobHandlerName)
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                .usingJobData(JobDataKeyEnum.JOB_HANDLER_PARAM.name(), jobHandlerParam)
                .usingJobData(JobDataKeyEnum.JOB_RETRY_COUNT.name(), retryCount)
                .usingJobData(JobDataKeyEnum.JOB_RETRY_INTERVAL.name(), retryInterval)
                .build();
    }

    private void validateScheduler() {
        if (scheduler == null) {
            throw exception0(NOT_IMPLEMENTED.getCode(),
                    "[定时任务 - 已禁用][参考 https:// doc.iocoder.cn/job/Open]");
        }
    }

}
