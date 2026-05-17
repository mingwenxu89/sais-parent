package cn.iocoder.yudao.framework.quartz.core.service;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Job Log Framework Service Interface
 *
 * @author Yudao Source Code
 */
public interface JobLogFrameworkService {

    /**
     * Create job log
     *
     * @param jobId Task number
     * @param beginTime start time
     * @param jobHandlerName Job processor name
     * @param jobHandlerParam Job processor parameters
     * @param executeIndex How many times to execute
     * @return Job log number
     */
    Long createJobLog(@NotNull(message = "task number cannot be empty") Long jobId,
                      @NotNull(message = "start time") LocalDateTime beginTime,
                      @NotEmpty(message = "job processor name cannot be empty") String jobHandlerName,
                      String jobHandlerParam,
                      @NotNull(message = "the number of executions cannot be empty") Integer executeIndex);

    /**
     * Update the execution results of the Job log
     *
     * @param logId Log number
     * @param endTime end time. Because it is asynchronous, it is not allowed to record the time to avoID it.
     * @param duration Running time, unit: milliseconds
     * @param success Is it successful?
     * @param result success data
     */
    void updateJobLogResultAsync(@NotNull(message = "log number cannot be empty") Long logId,
                                 @NotNull(message = "end time cannot be empty") LocalDateTime endTime,
                                 @NotNull(message = "running time cannot be empty") Integer duration,
                                 boolean success, String result);
}
