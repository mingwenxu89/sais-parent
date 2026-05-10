package cn.iocoder.yudao.module.agri.job;

import cn.iocoder.yudao.framework.tenant.core.context.TenantContextHolder;
import cn.iocoder.yudao.framework.tenant.core.job.TenantJob;
import cn.iocoder.yudao.module.agri.dal.dataobject.sensor.SensorDO;
import cn.iocoder.yudao.module.agri.dal.mysql.sensor.SensorMapper;
import cn.iocoder.yudao.module.agri.framework.iot.AwsIotMqttClient;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Mock sensor data generation job.
 * Publishes simulated sensor readings to AWS IoT Core every 30 minutes.
 */
@Component
@ConditionalOnBean(AwsIotMqttClient.class)
@Slf4j
public class MockSensorDataJob {

    private static final int SENSOR_STATUS_ACTIVE = 1;

    private final Random random = new Random();
    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    @Resource
    private SensorMapper sensorMapper;

    @Resource
    private AwsIotMqttClient awsIotMqttClient;

    @Scheduled(cron = "0 0/10 * * * ?")
    @TenantJob
    public void generateMockData() {
        log.info("[MockSensorDataJob] Starting mock sensor data generation");
        List<SensorDO> activeSensors = sensorMapper.selectList(
                new LambdaQueryWrapper<SensorDO>().eq(SensorDO::getStatus, SENSOR_STATUS_ACTIVE)
        );
        if (activeSensors.isEmpty()) {
            log.info("[MockSensorDataJob] No active sensors found, skipping");
            return;
        }
        LocalDateTime now = LocalDateTime.now();
        int successCount = 0;
        for (SensorDO sensor : activeSensors) {
            try {
                String dataType = "SOIL_MOISTURE";
                BigDecimal value = randomValue(20.0, 80.0);

                Map<String, Object> payload = new HashMap<>();
                payload.put("sensorId", sensor.getId());
                payload.put("farmId", sensor.getFarmId());
                payload.put("fieldId", sensor.getFieldId());
                payload.put("dataType", dataType);
                payload.put("value", value);
                payload.put("collectedAt", now.toString());
                payload.put("tenantId", TenantContextHolder.getTenantId());

                String topic = awsIotMqttClient.getTopicPrefix() + "/" + sensor.getId() + "/data";
                awsIotMqttClient.publish(topic, objectMapper.writeValueAsString(payload));
                successCount++;
            } catch (Exception e) {
                log.error("[MockSensorDataJob] Failed to publish data for sensor {}", sensor.getId(), e);
            }
        }
        log.info("[MockSensorDataJob] Published data for {}/{} sensors", successCount, activeSensors.size());
    }

    private BigDecimal randomValue(double min, double max) {
        double value = min + random.nextDouble() * (max - min);
        return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP);
    }

}
