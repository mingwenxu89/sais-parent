package cn.iocoder.yudao.module.agri.job;

import cn.iocoder.yudao.framework.tenant.core.context.TenantContextHolder;
import cn.iocoder.yudao.framework.tenant.core.job.TenantJob;
import cn.iocoder.yudao.module.agri.dal.dataobject.irrigation.IrrigationDeviceDO;
import cn.iocoder.yudao.module.agri.dal.dataobject.irrigation.IrrigationPlanDO;
import cn.iocoder.yudao.module.agri.dal.mysql.irrigation.IrrigationDeviceMapper;
import cn.iocoder.yudao.module.agri.dal.mysql.irrigation.IrrigationPlanMapper;
import cn.iocoder.yudao.module.agri.framework.iot.AwsIotMqttClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Simulates irrigation device ACK messages for mock/demo environments.
 *
 * Runs every minute. For each EXECUTING plan that has not yet received an ACK:
 *   - If the device has simulateFault=true → skip (no ACK sent, fault detection will trigger after 5 min)
 *   - Otherwise → publish a mock ACK to {topicPrefix}/{deviceCode}/ack
 *
 * This mirrors the behaviour of a real irrigation controller that sends an ACK
 * on the /ack topic after receiving and acting on the START command.
 */
@Component
@ConditionalOnBean(AwsIotMqttClient.class)
@Slf4j
public class MockDeviceAckJob {

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Resource
    private IrrigationPlanMapper irrigationPlanMapper;
    @Resource
    private IrrigationDeviceMapper irrigationDeviceMapper;
    @Resource
    private AwsIotMqttClient awsIotMqttClient;

    @Scheduled(cron = "30 * * * * ?")   // half-minute offset so plan is already EXECUTING
    @TenantJob
    public void publishMockAcks() {
        List<IrrigationPlanDO> plans = irrigationPlanMapper.selectExecutingWithoutAck();
        if (plans.isEmpty()) {
            return;
        }
        for (IrrigationPlanDO plan : plans) {
            try {
                IrrigationDeviceDO device = irrigationDeviceMapper.selectById(plan.getDeviceId());
                if (device == null) {
                    continue;
                }
                if (Boolean.TRUE.equals(device.getSimulateFault())) {
                    log.info("[MockDeviceAckJob] Device {} has simulateFault=true — withholding ACK for plan {}",
                            device.getDeviceCode(), plan.getId());
                    continue;
                }
                publishAck(device, plan);
            } catch (Exception e) {
                log.error("[MockDeviceAckJob] Failed to publish ACK for plan id={}", plan.getId(), e);
            }
        }
    }

    private void publishAck(IrrigationDeviceDO device, IrrigationPlanDO plan) throws Exception {
        Map<String, Object> payload = new HashMap<>();
        payload.put("planId", plan.getId());
        payload.put("deviceCode", device.getDeviceCode());
        payload.put("ackedAt", LocalDateTime.now().toString());
        payload.put("tenantId", TenantContextHolder.getTenantId());

        String topic = awsIotMqttClient.getTopicPrefix() + "/" + device.getDeviceCode() + "/ack";
        awsIotMqttClient.publish(topic, objectMapper.writeValueAsString(payload));
        log.info("[MockDeviceAckJob] Published ACK for plan={} on topic={}", plan.getId(), topic);
    }

}
