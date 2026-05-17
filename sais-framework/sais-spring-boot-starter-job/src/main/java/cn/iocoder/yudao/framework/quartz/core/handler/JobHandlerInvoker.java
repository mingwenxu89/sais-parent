package cn.iocoder.yudao.framework.quartz.core.handler;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.thread.ThreadUtil;
import cn.iocoder.yudao.framework.quartz.core.enums.JobDataKeyEnum;
import cn.iocoder.yudao.framework.quartz.core.service.JobLogFrameworkService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import jakarta.annotation.Resource;
import java.time.LocalDateTime;

import static cn.hutool.core.exceptions.ExceptionUtil.getRootCauseMessage;

/**
 * Basic Job caller, responsible for calling {@link JobHandler#execute(String)} to execute tasks
 *
 * @author Yudao Source Code
 */
@DisallowConcurrentExecution
@PersistJobDataAfterExecution
@Slf4j
public class JobHandlerInvoker extends QuartzJobBean {

 @Resource
 private ApplicationContext applicationContext;

 @Resource
 private JobLogFrameworkService jobLogFrameworkService;

 @Override
 protected void executeInternal(JobExecutionContext executionContext) throws JobExecutionException {
        // The first step is to obtain Job data
 Long jobId = executionContext.getMergedJobDataMap().getLong(JobDataKeyEnum.JOB_ID.name());
 String jobHandlerName = executionContext.getMergedJobDataMap().getString(JobDataKeyEnum.JOB_HANDLER_NAME.name());
 String jobHandlerParam = executionContext.getMergedJobDataMap().getString(JobDataKeyEnum.JOB_HANDLER_PARAM.name());
 int refireCount = executionContext.getRefireCount();
 int retryCount = (Integer) executionContext.getMergedJobDataMap().getOrDefault(JobDataKeyEnum.JOB_RETRY_COUNT.name(), 0);
 int retryInterval = (Integer) executionContext.getMergedJobDataMap().getOrDefault(JobDataKeyEnum.JOB_RETRY_INTERVAL.name(), 0);

        // The second step is to perform the task
 Long jobLogId = null;
 LocalDateTime startTime = LocalDateTime.now();
 String data = null;
 Throwable exception = null;
 try {
            // Record job log (initial)
 jobLogId = jobLogFrameworkService.createJobLog(jobId, startTime, jobHandlerName, jobHandlerParam, refireCount + 1);
            // perform tasks
 data = this.executeInternal(jobHandlerName, jobHandlerParam);
 } catch (Throwable ex) {
 exception = ex;
 }

        // The third step is to record the execution log
 this.updateJobLogResultAsync(jobLogId, startTime, data, exception, executionContext);

        // Step 4: Handle abnormal situations
 handleException(exception, refireCount, retryCount, retryInterval);
 }

 private String executeInternal(String jobHandlerName, String jobHandlerParam) throws Exception {
        // Get the JobHandler object
 JobHandler jobHandler = applicationContext.getBean(jobHandlerName, JobHandler.class);
        Assert.notNull(jobHandler, "JobHandler will not be empty");
        // perform tasks
 return jobHandler.execute(jobHandlerParam);
 }

 private void updateJobLogResultAsync(Long jobLogId, LocalDateTime startTime, String data, Throwable exception,
 JobExecutionContext executionContext) {
 LocalDateTime endTime = LocalDateTime.now();
        // Is processing successful?
 boolean success = exception == null;
 if (!success) {
 data = getRootCauseMessage(exception);
 }
        // Change log
 try {
 jobLogFrameworkService.updateJobLogResultAsync(jobLogId, endTime, (int) LocalDateTimeUtil.between(startTime, endTime).toMillis(), success, data);
 } catch (Exception ex) {
            log.error("[executeInternal][Job({}) logId({}) Record execution log failure ({}/{})]",
 executionContext.getJobDetail().getKey(), jobLogId, success, data);
 }
 }

 private void handleException(Throwable exception,
 int refireCount, int retryCount, int retryInterval) throws JobExecutionException {
        // If there is an exception, retry
 if (exception == null) {
 return;
 }
        // Situation 1: If the retry limit is reached, just throw an exception directly
 if (refireCount >= retryCount) {
 throw new JobExecutionException(exception);
 }

        // Case 2: If the retry upper limit is not reached, sleep for a certain interval and then try again.
        // Sleep is used here to implement it, mainly because it is hoped that the implementation will be relatively simple. Because, at the same time, there will not be a large number of failed jobs.
 if (retryInterval > 0) {
 ThreadUtil.sleep(retryInterval);
 }
        // The second parameter, refireImmediately = true, means retry immediately
 throw new JobExecutionException(exception, true);
 }

}
