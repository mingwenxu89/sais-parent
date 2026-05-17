package cn.iocoder.yudao.module.agri.service.sensorreporting;

import cn.iocoder.yudao.framework.tenant.core.context.TenantContextHolder;
import cn.iocoder.yudao.framework.tenant.core.service.TenantFrameworkService;
import cn.iocoder.yudao.framework.tenant.core.util.TenantUtils;
import cn.iocoder.yudao.module.agri.controller.admin.sensorreporting.vo.SensorReportingManualReqVO;
import cn.iocoder.yudao.module.agri.controller.admin.sensorreporting.vo.SensorReportingStatusRespVO;
import cn.iocoder.yudao.module.agri.dal.dataobject.sensor.SensorDO;
import cn.iocoder.yudao.module.agri.dal.mysql.field.FieldMapper;
import cn.iocoder.yudao.module.agri.dal.mysql.sensor.SensorMapper;
import cn.iocoder.yudao.module.agri.framework.iot.AwsIotMqttClient;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.agri.enums.ErrorCodeConstants.FIELD_NOT_EXISTS;
import static cn.iocoder.yudao.module.agri.enums.ErrorCodeConstants.SENSOR_NOT_EXISTS;
import static cn.iocoder.yudao.module.agri.enums.ErrorCodeConstants.SENSOR_REPORTING_FIELD_MISMATCH;
import static cn.iocoder.yudao.module.agri.enums.ErrorCodeConstants.SENSOR_REPORTING_INTERVAL_INVALID;
import static cn.iocoder.yudao.module.agri.enums.ErrorCodeConstants.SENSOR_REPORTING_MQTT_UNAVAILABLE;
import static cn.iocoder.yudao.module.agri.enums.ErrorCodeConstants.SENSOR_REPORTING_SENSOR_INACTIVE;

@Service
@Validated
@Slf4j
public class SensorReportingServiceImpl implements SensorReportingService {

    private static final int SENSOR_STATUS_ACTIVE = 1;
    private static final int MIN_INTERVAL_SECONDS = 5;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private final Random random = new Random();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
        Thread thread = new Thread(r, "sensor-reporting-scheduler");
        thread.setDaemon(true);
        return thread;
    });

    @Resource
    private SensorMapper sensorMapper;
    @Resource
    private FieldMapper fieldMapper;
    @Resource
    private TenantFrameworkService tenantFrameworkService;
    @Autowired(required = false)
    private AwsIotMqttClient awsIotMqttClient;

    private ScheduledFuture<?> scheduledFuture;
    private Integer intervalSeconds;
    private LocalDateTime startedAt;
    private LocalDateTime lastRunAt;
    private int lastSuccessCount;
    private int lastFailureCount;

    @Override
    public synchronized SensorReportingStatusRespVO getStatus() {
        return buildStatus();
    }

    @Override
    public synchronized SensorReportingStatusRespVO start(Integer intervalSeconds) {
        if (intervalSeconds == null || intervalSeconds < MIN_INTERVAL_SECONDS) {
            throw exception(SENSOR_REPORTING_INTERVAL_INVALID);
        }
        if (awsIotMqttClient == null) {
            throw exception(SENSOR_REPORTING_MQTT_UNAVAILABLE);
        }
        cancelCurrentTask();
        this.intervalSeconds = intervalSeconds;
        this.startedAt = LocalDateTime.now();
        this.scheduledFuture = scheduler.scheduleAtFixedRate(this::safeReportAllTenantsRandom,
                0, intervalSeconds, TimeUnit.SECONDS);
        log.info("[SensorReporting] Started periodic reporting, interval={} seconds", intervalSeconds);
        return buildStatus();
    }

    @Override
    public synchronized SensorReportingStatusRespVO stop() {
        cancelCurrentTask();
        this.intervalSeconds = null;
        this.startedAt = null;
        log.info("[SensorReporting] Stopped periodic reporting");
        return buildStatus();
    }

    @Override
    public SensorReportingStatusRespVO reportAllTenantsRandom() {
        ReportResult result = reportAllTenantsRandomInternal();
        synchronized (this) {
            this.lastRunAt = result.getRunAt();
            this.lastSuccessCount = result.getSuccessCount();
            this.lastFailureCount = result.getFailureCount();
            return buildStatus();
        }
    }

    @Override
    public void reportManual(SensorReportingManualReqVO reqVO) {
        if (fieldMapper.selectById(reqVO.getFieldId()) == null) {
            throw exception(FIELD_NOT_EXISTS);
        }
        SensorDO sensor = sensorMapper.selectById(reqVO.getSensorId());
        if (sensor == null) {
            throw exception(SENSOR_NOT_EXISTS);
        }
        if (!Objects.equals(sensor.getFieldId(), reqVO.getFieldId())) {
            throw exception(SENSOR_REPORTING_FIELD_MISMATCH);
        }
        if (!Objects.equals(sensor.getStatus(), SENSOR_STATUS_ACTIVE)) {
            throw exception(SENSOR_REPORTING_SENSOR_INACTIVE);
        }
        publish(sensor, reqVO.getDataType(), reqVO.getValue(),
                reqVO.getCollectedAt() != null ? reqVO.getCollectedAt() : LocalDateTime.now());
    }

    @PreDestroy
    public void destroy() {
        cancelCurrentTask();
        scheduler.shutdownNow();
    }

    private void safeReportAllTenantsRandom() {
        try {
            reportAllTenantsRandom();
        } catch (Exception e) {
            log.error("[SensorReporting] Periodic reporting failed", e);
        }
    }

    private ReportResult reportAllTenantsRandomInternal() {
        List<Long> tenantIds = TenantUtils.executeIgnore(() -> tenantFrameworkService.getTenantIds());
        LocalDateTime runAt = LocalDateTime.now();
        ReportCounter counter = new ReportCounter();
        for (Long tenantId : tenantIds) {
            TenantUtils.execute(tenantId, () -> reportCurrentTenantRandom(runAt, counter));
        }
        log.info("[SensorReporting] Published random data, success={}, failure={}",
                counter.successCount, counter.failureCount);
        return new ReportResult(runAt, counter.successCount, counter.failureCount);
    }

    private void reportCurrentTenantRandom(LocalDateTime collectedAt, ReportCounter counter) {
        List<SensorDO> activeSensors = sensorMapper.selectList(
                new LambdaQueryWrapper<SensorDO>().eq(SensorDO::getStatus, SENSOR_STATUS_ACTIVE));
        for (SensorDO sensor : activeSensors) {
            try {
                publish(sensor, resolveDataType(sensor), randomValue(sensor), collectedAt);
                counter.successCount++;
            } catch (Exception e) {
                counter.failureCount++;
                log.error("[SensorReporting] Failed to publish data for tenant {}, sensor {}",
                        TenantContextHolder.getTenantId(), sensor.getId(), e);
            }
        }
    }

    private void publish(SensorDO sensor, String dataType, BigDecimal value, LocalDateTime collectedAt) {
        if (awsIotMqttClient == null) {
            throw exception(SENSOR_REPORTING_MQTT_UNAVAILABLE);
        }
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("sensorId", sensor.getId());
            payload.put("farmId", sensor.getFarmId());
            payload.put("fieldId", sensor.getFieldId());
            payload.put("dataType", dataType);
            payload.put("value", value);
            payload.put("collectedAt", collectedAt.toString());
            payload.put("tenantId", TenantContextHolder.getTenantId());

            String topic = awsIotMqttClient.getTopicPrefix() + "/" + sensor.getId() + "/data";
            awsIotMqttClient.publish(topic, objectMapper.writeValueAsString(payload));
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to publish sensor data", e);
        }
    }

    private String resolveDataType(SensorDO sensor) {
        if (Objects.equals(sensor.getSensorType(), 2)) {
            return "HUMIDITY";
        }
        if (Objects.equals(sensor.getSensorType(), 3)) {
            return "TEMPERATURE";
        }
        return "SOIL_MOISTURE";
    }

    private BigDecimal randomValue(SensorDO sensor) {
        if (Objects.equals(sensor.getSensorType(), 2)) {
            return randomValue(30.0, 95.0);
        }
        if (Objects.equals(sensor.getSensorType(), 3)) {
            return randomValue(10.0, 35.0);
        }
        return randomValue(20.0, 80.0);
    }

    private BigDecimal randomValue(double min, double max) {
        double value = min + random.nextDouble() * (max - min);
        return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP);
    }

    private synchronized void cancelCurrentTask() {
        if (scheduledFuture != null) {
            scheduledFuture.cancel(false);
            scheduledFuture = null;
        }
    }

    private SensorReportingStatusRespVO buildStatus() {
        SensorReportingStatusRespVO status = new SensorReportingStatusRespVO();
        status.setRunning(scheduledFuture != null && !scheduledFuture.isCancelled());
        status.setIntervalSeconds(intervalSeconds);
        status.setStartedAt(startedAt);
        status.setLastRunAt(lastRunAt);
        status.setLastSuccessCount(lastSuccessCount);
        status.setLastFailureCount(lastFailureCount);
        return status;
    }

    private static class ReportCounter {
        private int successCount;
        private int failureCount;
    }

    @Getter
    private static class ReportResult {
        private final LocalDateTime runAt;
        private final int successCount;
        private final int failureCount;

        private ReportResult(LocalDateTime runAt, int successCount, int failureCount) {
            this.runAt = runAt;
            this.successCount = successCount;
            this.failureCount = failureCount;
        }
    }

}
