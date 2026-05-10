package cn.iocoder.yudao.module.agri.framework.iot;

import cn.iocoder.yudao.framework.tenant.core.util.TenantUtils;
import cn.iocoder.yudao.module.agri.dal.dataobject.irrigation.IrrigationPlanDO;
import cn.iocoder.yudao.module.agri.dal.dataobject.sensordata.SensorDataDO;
import cn.iocoder.yudao.module.agri.dal.mysql.irrigation.IrrigationPlanMapper;
import cn.iocoder.yudao.module.agri.service.sensordata.SensorDataService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Configuration
@EnableConfigurationProperties(AwsIotProperties.class)
@ConditionalOnProperty(prefix = "yudao.agri.iot", name = "endpoint")
@Slf4j
public class AwsIotConfiguration {

    @Resource
    private AwsIotProperties properties;

    @Resource
    private SensorDataService sensorDataService;
    @Resource
    private IrrigationPlanMapper irrigationPlanMapper;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    @Bean(destroyMethod = "disconnect")
    public AwsIotMqttClient awsIotMqttClient() throws Exception {
        AwsIotMqttClient mqttClient = new AwsIotMqttClient(properties);

        mqttClient.subscribe(properties.getTopicPrefix() + "/+/data", (topic, payload) -> {
            try {
                String body = new String(payload, StandardCharsets.UTF_8);
                log.debug("[AwsIot] Received on {}: {}", topic, body);
                JsonNode node = objectMapper.readTree(body);
                Long tenantId = node.has("tenantId") ? node.get("tenantId").asLong() : null;
                if (tenantId == null) {
                    log.warn("[AwsIot] Missing tenantId in payload from topic {}, skipping", topic);
                    return;
                }
                SensorDataDO data = objectMapper.treeToValue(node, SensorDataDO.class);
                TenantUtils.execute(tenantId, () -> sensorDataService.recordSensorData(data));
            } catch (Exception e) {
                log.error("[AwsIot] Failed to process message from topic {}", topic, e);
            }
        });

        mqttClient.subscribe(properties.getTopicPrefix() + "/+/ack", (topic, payload) -> {
            try {
                JsonNode node = objectMapper.readTree(new String(payload, StandardCharsets.UTF_8));
                Long planId = node.has("planId") ? node.get("planId").asLong() : null;
                Long tenantId = node.has("tenantId") ? node.get("tenantId").asLong() : null;
                if (planId == null || tenantId == null) {
                    log.warn("[AwsIot] ACK message missing planId or tenantId on topic {}", topic);
                    return;
                }
                TenantUtils.execute(tenantId, () -> {
                    IrrigationPlanDO update = new IrrigationPlanDO();
                    update.setId(planId);
                    update.setAckReceivedAt(LocalDateTime.now());
                    irrigationPlanMapper.updateById(update);
                    log.info("[AwsIot] ACK recorded for irrigation plan id={}", planId);
                });
            } catch (Exception e) {
                log.error("[AwsIot] Failed to process ACK message from topic {}", topic, e);
            }
        });

        return mqttClient;
    }
}
