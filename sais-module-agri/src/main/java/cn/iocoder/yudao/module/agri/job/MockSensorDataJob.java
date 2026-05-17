package cn.iocoder.yudao.module.agri.job;

import cn.iocoder.yudao.module.agri.framework.iot.AwsIotMqttClient;
import cn.iocoder.yudao.module.agri.service.sensorreporting.SensorReportingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;

/**
 * Mock sensor data generation job.
 * Kept for manual demo triggers; periodic execution is controlled from Dev Tools.
 */
@Component
@ConditionalOnBean(AwsIotMqttClient.class)
@Slf4j
public class MockSensorDataJob {

    @Resource
    private SensorReportingService sensorReportingService;

    public void generateMockData() {
        log.info("[MockSensorDataJob] Manual mock sensor data generation requested");
        sensorReportingService.reportAllTenantsRandom();
    }

}
