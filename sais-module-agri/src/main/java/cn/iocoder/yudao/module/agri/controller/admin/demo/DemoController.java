package cn.iocoder.yudao.module.agri.controller.admin.demo;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.agri.dal.dataobject.farm.FarmDO;
import cn.iocoder.yudao.module.agri.dal.dataobject.field.FieldDO;
import cn.iocoder.yudao.module.agri.dal.dataobject.sensor.SensorDO;
import cn.iocoder.yudao.module.agri.dal.dataobject.sensordata.SensorDataDO;
import cn.iocoder.yudao.module.agri.dal.dataobject.weather.WeatherDataDO;
import cn.iocoder.yudao.module.agri.dal.mysql.farm.FarmMapper;
import cn.iocoder.yudao.module.agri.dal.mysql.field.FieldMapper;
import cn.iocoder.yudao.module.agri.dal.mysql.sensor.SensorMapper;
import cn.iocoder.yudao.module.agri.dal.mysql.weather.WeatherDataMapper;
import cn.iocoder.yudao.module.agri.job.IrrigationDecisionJob;
import cn.iocoder.yudao.module.agri.job.IrrigationPlanExecutionJob;
import cn.iocoder.yudao.module.agri.job.MockSensorDataJob;
import cn.iocoder.yudao.module.agri.job.WeatherFetchJob;
import cn.iocoder.yudao.module.agri.service.alert.AlertCheckService;
import cn.iocoder.yudao.module.agri.service.alert.AlertService;
import cn.iocoder.yudao.module.agri.service.sensordata.SensorDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "Admin - Demo Task Trigger")
@RestController
@RequestMapping("/agri/demo")
@Validated
@Slf4j
public class DemoController {

    @Autowired(required = false)
    private MockSensorDataJob mockSensorDataJob;

    @Resource
    private IrrigationDecisionJob irrigationDecisionJob;

    @Resource
    private IrrigationPlanExecutionJob irrigationPlanExecutionJob;

    @Resource
    private WeatherFetchJob weatherFetchJob;

    @Resource
    private AlertCheckService alertCheckService;

    @Resource
    private AlertService alertService;

    @Resource
    private FieldMapper fieldMapper;

    @Resource
    private SensorMapper sensorMapper;

    @Resource
    private FarmMapper farmMapper;

    @Resource
    private WeatherDataMapper weatherDataMapper;

    @Resource
    private SensorDataService sensorDataService;

    @PostMapping("/trigger-sensor-report")
    @Operation(summary = "Manually trigger sensor data report")
    @PreAuthorize("@ss.hasPermission('agri:sensor:query')")
    public CommonResult<String> triggerSensorReport() {
        if (mockSensorDataJob == null) {
            return success("SKIP: AWS IoT not configured, MockSensorDataJob is inactive.");
        }
        log.info("[Demo] Manually triggering MockSensorDataJob");
        mockSensorDataJob.generateMockData();
        return success("Sensor data report triggered successfully.");
    }

    @PostMapping("/trigger-ai-irrigation")
    @Operation(summary = "Manually trigger AI irrigation decision")
    @PreAuthorize("@ss.hasPermission('agri:irrigation-plan:query')")
    public CommonResult<String> triggerAiIrrigation() {
        log.info("[Demo] Manually triggering IrrigationDecisionJob");
        irrigationDecisionJob.runDecision();
        return success("AI irrigation decision triggered successfully.");
    }

    @PostMapping("/trigger-irrigation-execution")
    @Operation(summary = "Manually trigger irrigation plan execution check")
    @PreAuthorize("@ss.hasPermission('agri:irrigation-plan:query')")
    public CommonResult<String> triggerIrrigationExecution() {
        log.info("[Demo] Manually triggering IrrigationPlanExecutionJob");
        irrigationPlanExecutionJob.executePlans();
        return success("Irrigation plan execution check triggered successfully.");
    }

    @PostMapping("/trigger-weather-fetch")
    @Operation(summary = "Manually trigger weather data fetch")
    @PreAuthorize("@ss.hasPermission('agri:sensor:query')")
    public CommonResult<String> triggerWeatherFetch() {
        log.info("[Demo] Manually triggering WeatherFetchJob");
        weatherFetchJob.fetchWeather();
        return success("Weather data fetch triggered successfully.");
    }

    @PostMapping("/trigger-alert-check")
    @Operation(summary = "Manually trigger alert detection (weather forecast)")
    @PreAuthorize("@ss.hasPermission('agri:alert:query')")
    public CommonResult<String> triggerAlertCheck(@RequestParam(required = false) Long farmId) {
        log.info("[Demo] Manually triggering alert check for farmId={}", farmId);
        alertCheckService.checkWeatherForecast(farmId);
        return success("Alert check triggered successfully.");
    }

    @PostMapping("/trigger-test-alert")
    @Operation(summary = "Trigger test alert")
    @PreAuthorize("@ss.hasPermission('agri:alert:create')")
    public CommonResult<String> triggerTestAlert(
            @RequestParam String type,
            @RequestParam(required = false) Long farmId) {
        log.info("[Demo] Triggering test alert type={} farmId={}", type, farmId);
        alertService.triggerTestAlert(type, farmId);
        return success("Test alert '" + type + "' triggered successfully.");
    }

    @PostMapping("/inject-sensor-data")
    @Operation(summary = "Inject a sensor reading into all sensors of all ONGOING fields (for evaluation)")
    @PreAuthorize("@ss.hasPermission('agri:sensor:create')")
    public CommonResult<String> injectSensorData(
            @RequestParam @NotBlank String dataType,
            @RequestParam @NotNull BigDecimal value) {
        List<FieldDO> ongoingFields = fieldMapper.selectList(
                new LambdaQueryWrapperX<FieldDO>().eq(FieldDO::getGrowStatus, "ONGOING"));
        if (ongoingFields.isEmpty()) {
            return success("SKIP: no ONGOING fields found.");
        }
        Set<Long> fieldIds = ongoingFields.stream().map(FieldDO::getId).collect(Collectors.toSet());
        List<SensorDO> sensors = sensorMapper.selectList(
                new LambdaQueryWrapperX<SensorDO>().in(SensorDO::getFieldId, fieldIds));
        if (sensors.isEmpty()) {
            return success("SKIP: no sensors bound to ONGOING fields.");
        }
        LocalDateTime now = LocalDateTime.now();
        for (SensorDO sensor : sensors) {
            SensorDataDO data = new SensorDataDO();
            data.setSensorId(sensor.getId());
            data.setFarmId(sensor.getFarmId());
            data.setFieldId(sensor.getFieldId());
            data.setDataType(dataType);
            data.setValue(value);
            data.setCollectedAt(now);
            sensorDataService.recordSensorData(data);
        }
        log.info("[Demo] Injected {}={} into {} sensors across {} ONGOING fields",
                dataType, value, sensors.size(), ongoingFields.size());
        return success("Injected " + dataType + "=" + value + " into " + sensors.size() + " sensor(s).");
    }

    @PostMapping("/inject-weather-forecast")
    @Operation(summary = "Upsert tomorrow's weather forecast for all farms (for evaluation)")
    @PreAuthorize("@ss.hasPermission('agri:sensor:create')")
    public CommonResult<String> injectWeatherForecast(
            @RequestParam @NotBlank String field,
            @RequestParam @NotNull BigDecimal value) {
        List<FarmDO> farms = farmMapper.selectList(new LambdaQueryWrapperX<>());
        if (farms.isEmpty()) {
            return success("SKIP: no farms found.");
        }
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        int upserted = 0;
        for (FarmDO farm : farms) {
            WeatherDataDO existing = weatherDataMapper.selectByFarmIdAndDate(farm.getId(), tomorrow);
            WeatherDataDO record = existing != null ? existing : new WeatherDataDO();
            if (existing == null) {
                record.setFarmId(farm.getId());
                record.setForecastDate(tomorrow);
                record.setRecordTime(LocalDateTime.now());
                record.setSource("demo-inject");
            }
            switch (field) {
                case "rainfall":
                    record.setRainfall(value);
                    break;
                case "tempMin":
                    record.setTempMin(value);
                    break;
                case "tempMax":
                    record.setTempMax(value);
                    break;
                case "temperature":
                    record.setTemperature(value);
                    break;
                case "humidity":
                    record.setHumidity(value);
                    break;
                default:
                    return success("SKIP: unsupported field '" + field
                            + "'. Allowed: rainfall / tempMin / tempMax / temperature / humidity.");
            }
            if (existing == null) {
                weatherDataMapper.insert(record);
            } else {
                weatherDataMapper.updateById(record);
            }
            upserted++;
        }
        log.info("[Demo] Upserted weather forecast {}={} for {} farms (tomorrow={})",
                field, value, upserted, tomorrow);
        return success("Upserted " + field + "=" + value + " for " + upserted + " farm(s) (tomorrow).");
    }
}
