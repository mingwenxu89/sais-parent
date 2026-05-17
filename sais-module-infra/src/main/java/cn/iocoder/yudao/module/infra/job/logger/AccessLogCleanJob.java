package cn.iocoder.yudao.module.infra.job.logger;

import cn.iocoder.yudao.framework.quartz.core.handler.JobHandler;
import cn.iocoder.yudao.framework.tenant.core.aop.TenantIgnore;
import cn.iocoder.yudao.module.infra.service.logger.ApiAccessLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;

/**
 * Job to physically delete access logs N days ago
 *
 * @author j-sentinel
 */
@Component
@Slf4j
public class AccessLogCleanJob implements JobHandler {

    @Resource
    private ApiAccessLogService apiAccessLogService;

    /**
     * Clean logs older than (14) days
     */
    private static final Integer JOB_CLEAN_RETAIN_DAY = 14;

    /**
     * The ID of entries between each deletion. If the value is too high, it may cause excessive pressure on the database.
     */
    private static final Integer DELETE_LIMIT = 100;

    @Override
    @TenantIgnore
    public String execute(String param) {
        Integer count = apiAccessLogService.cleanAccessLog(JOB_CLEAN_RETAIN_DAY, DELETE_LIMIT);
        log.info("[execute][ID ({}) of regularly executed cleanup access logs]", count);
        return String.format("The ID of access logs to be cleaned regularly is %s", count);
    }

}
