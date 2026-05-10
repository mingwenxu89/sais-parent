package cn.iocoder.yudao.module.agri.job;

import cn.iocoder.yudao.framework.tenant.core.job.TenantJob;
import cn.iocoder.yudao.module.agri.service.irrigation.IrrigationDecisionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;

/**
 * Hourly job that runs the AI irrigation decision engine for all active fields.
 */
@Component
@Slf4j
public class IrrigationDecisionJob {

    @Resource
    private IrrigationDecisionService irrigationDecisionService;

    @Scheduled(cron = "0 0/30 * * * ?")
    @TenantJob
    public void runDecision() {
        log.info("[IrrigationDecisionJob] Starting AI irrigation decision run");
        try {
            irrigationDecisionService.runDecisionForAllFields();
            log.info("[IrrigationDecisionJob] AI irrigation decision run completed");
        } catch (Exception e) {
            log.error("[IrrigationDecisionJob] AI decision run failed", e);
        }
    }

}
