package cn.iocoder.yudao.module.infra.job.job;

import cn.iocoder.yudao.framework.quartz.core.handler.JobHandler;
import cn.iocoder.yudao.framework.tenant.core.aop.TenantIgnore;
import cn.iocoder.yudao.module.infra.service.job.JobLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import jakarta.annotation.Resource;

/**
 * Job that physically deletes task logs N days ago
 *
 * @author j-sentinel
 */
@Slf4j
@Component
public class JobLogCleanJob implements JobHandler {

    @Resource
    private JobLogService jobLogService;

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
        Integer count = jobLogService.cleanJobLog(JOB_CLEAN_RETAIN_DAY, DELETE_LIMIT);
        log.info("[execute][ID of scheduled execution cleanup scheduled task logs ({})]", count);
        return String.format("The ID of logs for scheduled cleaning tasks is %s", count);
    }

}
