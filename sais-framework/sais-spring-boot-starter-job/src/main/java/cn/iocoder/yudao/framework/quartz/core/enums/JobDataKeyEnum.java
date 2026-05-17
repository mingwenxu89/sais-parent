package cn.iocoder.yudao.framework.quartz.core.enums;

/**
 * Key enumeration of Quartz Job Data
 */
public enum JobDataKeyEnum {

    JOB_ID,
    JOB_HANDLER_NAME,
    JOB_HANDLER_PARAM,
    JOB_RETRY_COUNT, // Maximum number of retries
    JOB_RETRY_INTERVAL, // Interval between retries

}
