package cn.iocoder.yudao.module.agri.job;

import cn.iocoder.yudao.framework.tenant.core.job.TenantJob;
import cn.iocoder.yudao.module.agri.dal.dataobject.irrigation.IrrigationDeviceDO;
import cn.iocoder.yudao.module.agri.dal.dataobject.irrigation.IrrigationPlanDO;
import cn.iocoder.yudao.module.agri.dal.mysql.irrigation.IrrigationDeviceMapper;
import cn.iocoder.yudao.module.agri.dal.mysql.irrigation.IrrigationPlanMapper;
import cn.iocoder.yudao.module.agri.framework.iot.AwsIotMqttClient;
import cn.iocoder.yudao.module.agri.service.alert.AlertCheckService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Scans pending irrigation plans every minute and dispatches MQTT commands to devices.
 *
 * Two phases per tick:
 *   1. START  — PENDING plans whose planned_start_time ≤ now → EXECUTING + MQTT START
 *   2. FINISH — EXECUTING plans whose planned duration has elapsed → COMPLETED + MQTT STOP
 */
@Component
@Slf4j
public class IrrigationPlanExecutionJob {

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Resource
    private IrrigationPlanMapper irrigationPlanMapper;
    @Resource
    private IrrigationDeviceMapper irrigationDeviceMapper;

    /** Optional — only present when AWS IoT Core is configured */
    @Autowired(required = false)
    private AwsIotMqttClient mqttClient;

    @Resource
    private AlertCheckService alertCheckService;

    /** Minutes after actualStartTime before treating missing ACK as a device fault */
    private static final long ACK_TIMEOUT_MINUTES = 5;

    @Scheduled(cron = "0 * * * * ?")
    @TenantJob
    public void executePlans() {
        LocalDateTime now = LocalDateTime.now();

        // Phase 1: start PENDING plans that are due
        List<IrrigationPlanDO> duePlans = irrigationPlanMapper.selectPendingDuePlans(now);
        for (IrrigationPlanDO plan : duePlans) {
            try {
                startPlan(plan, now);
            } catch (Exception e) {
                log.error("[IrrigationPlanJob] Failed to start plan id={}", plan.getId(), e);
            }
        }

        // Phase 2: complete EXECUTING plans whose duration has elapsed
        List<IrrigationPlanDO> timedOutPlans = irrigationPlanMapper.selectExecutingTimedOutPlans(now);
        for (IrrigationPlanDO plan : timedOutPlans) {
            try {
                completePlan(plan, now);
            } catch (Exception e) {
                log.error("[IrrigationPlanJob] Failed to complete plan id={}", plan.getId(), e);
            }
        }

        // Phase 3: detect device faults — EXECUTING plans with no ACK after timeout
        List<IrrigationPlanDO> ackTimeoutPlans = irrigationPlanMapper
                .selectExecutingAckTimeoutPlans(now.minusMinutes(ACK_TIMEOUT_MINUTES));
        for (IrrigationPlanDO plan : ackTimeoutPlans) {
            try {
                alertCheckService.checkIrrigationDeviceFault(plan);
            } catch (Exception e) {
                log.error("[IrrigationPlanJob] Failed to check device fault for plan id={}", plan.getId(), e);
            }
        }

        if (!duePlans.isEmpty() || !timedOutPlans.isEmpty() || !ackTimeoutPlans.isEmpty()) {
            log.info("[IrrigationPlanJob] Processed {} started, {} completed, {} ack-timeout",
                    duePlans.size(), timedOutPlans.size(), ackTimeoutPlans.size());
        }
    }

    private void startPlan(IrrigationPlanDO plan, LocalDateTime now) throws Exception {
        IrrigationDeviceDO device = irrigationDeviceMapper.selectById(plan.getDeviceId());
        if (device == null) {
            log.warn("[IrrigationPlanJob] Plan {} has no device (id={}), skipping", plan.getId(), plan.getDeviceId());
            return;
        }

        // Update plan to EXECUTING
        IrrigationPlanDO update = new IrrigationPlanDO();
        update.setId(plan.getId());
        update.setStatus("EXECUTING");
        update.setActualStartTime(now);
        irrigationPlanMapper.updateById(update);

        // Mark device as watering
        IrrigationDeviceDO deviceUpdate = new IrrigationDeviceDO();
        deviceUpdate.setId(device.getId());
        deviceUpdate.setIsWatering(true);
        irrigationDeviceMapper.updateById(deviceUpdate);

        // Publish MQTT START command
        publishCommand(device, plan, "START");

        log.info("[IrrigationPlanJob] Started plan id={} on device {} ({}), duration={}min",
                plan.getId(), device.getId(), device.getDeviceCode(), plan.getPlannedDuration());
    }

    private void completePlan(IrrigationPlanDO plan, LocalDateTime now) throws Exception {
        IrrigationDeviceDO device = irrigationDeviceMapper.selectById(plan.getDeviceId());

        // Calculate actual water usage: flow_rate(L/min) * duration(min)
        BigDecimal waterQuantity = null;
        if (device != null && device.getFlowRate() != null && plan.getActualStartTime() != null) {
            long actualMinutes = java.time.temporal.ChronoUnit.MINUTES.between(plan.getActualStartTime(), now);
            long clampedMinutes = Math.min(actualMinutes, plan.getPlannedDuration());
            waterQuantity = device.getFlowRate().multiply(BigDecimal.valueOf(clampedMinutes));
        }

        // Update plan to COMPLETED
        IrrigationPlanDO update = new IrrigationPlanDO();
        update.setId(plan.getId());
        update.setStatus("COMPLETED");
        update.setActualEndTime(now);
        update.setWaterQuantity(waterQuantity);
        irrigationPlanMapper.updateById(update);

        // Mark device as not watering
        if (device != null) {
            IrrigationDeviceDO deviceUpdate = new IrrigationDeviceDO();
            deviceUpdate.setId(device.getId());
            deviceUpdate.setIsWatering(false);
            irrigationDeviceMapper.updateById(deviceUpdate);

            // Publish MQTT STOP command
            publishCommand(device, plan, "STOP");
        }

        log.info("[IrrigationPlanJob] Completed plan id={}, waterQuantity={}L", plan.getId(), waterQuantity);
    }

    private void publishCommand(IrrigationDeviceDO device, IrrigationPlanDO plan, String action) {
        if (mqttClient == null) {
            log.debug("[IrrigationPlanJob] MQTT not configured — skipping {} command for plan {}", action, plan.getId());
            return;
        }
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("action", action);
            payload.put("planId", plan.getId());
            payload.put("deviceId", device.getId());
            payload.put("durationMinutes", plan.getPlannedDuration());

            String topic = mqttClient.getTopicPrefix() + "/" + device.getDeviceCode() + "/command";
            mqttClient.publish(topic, objectMapper.writeValueAsString(payload));
            log.info("[IrrigationPlanJob] Published {} to topic={}", action, topic);
        } catch (Exception e) {
            log.error("[IrrigationPlanJob] Failed to publish MQTT {} command for plan {}", action, plan.getId(), e);
        }
    }

}
