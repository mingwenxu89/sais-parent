package cn.iocoder.yudao.module.agri.service.alert;

import cn.iocoder.yudao.module.agri.dal.dataobject.crop.CropGrowthStageDO;
import cn.iocoder.yudao.module.agri.dal.dataobject.crop.CropPlanDO;
import cn.iocoder.yudao.module.agri.dal.dataobject.irrigation.IrrigationDeviceDO;
import cn.iocoder.yudao.module.agri.dal.dataobject.irrigation.IrrigationPlanDO;
import cn.iocoder.yudao.module.agri.dal.dataobject.sensordata.SensorDataDO;
import cn.iocoder.yudao.module.agri.dal.dataobject.weather.WeatherDataDO;
import cn.iocoder.yudao.module.agri.dal.mysql.crop.CropGrowthStageMapper;
import cn.iocoder.yudao.module.agri.dal.mysql.crop.CropPlanMapper;
import cn.iocoder.yudao.module.agri.dal.mysql.irrigation.IrrigationDeviceMapper;
import cn.iocoder.yudao.module.agri.dal.mysql.weather.WeatherDataMapper;
import cn.iocoder.yudao.module.agri.service.irrigation.IrrigationEvaluationHelper;
import cn.iocoder.yudao.module.system.api.notify.NotifyMessageSendApi;
import cn.iocoder.yudao.module.system.api.notify.dto.NotifySendSingleToUserReqDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Evaluates incoming sensor readings and weather forecasts against configured thresholds
 * and raises alerts when anomalies are detected.
 */
@Component
@Slf4j
public class AlertCheckService {

    private static final BigDecimal TEMP_FROST_CRITICAL = new BigDecimal("0");
    private static final BigDecimal TEMP_FROST_WARN     = new BigDecimal("5");
    private static final BigDecimal TEMP_HEAT_WARN      = new BigDecimal("35");
    private static final BigDecimal TEMP_HEAT_CRITICAL  = new BigDecimal("38");
    private static final BigDecimal RAIN_HEAVY_WARN     = new BigDecimal("25");
    private static final BigDecimal RAIN_HEAVY_CRITICAL = new BigDecimal("50");
    // How far below moistureMin triggers CRITICAL instead of WARN
    private static final BigDecimal MOISTURE_CRITICAL_MARGIN = new BigDecimal("15");

    private static final String NOTIFY_TEMPLATE_CODE = "agri_alert_raised";
    private static final Long NOTIFY_USER_ID = 1L;

    @Resource
    private AlertService alertService;
    @Resource
    private NotifyMessageSendApi notifyMessageSendApi;
    @Resource
    private CropPlanMapper cropPlanMapper;
    @Resource
    private CropGrowthStageMapper cropGrowthStageMapper;
    @Resource
    private WeatherDataMapper weatherDataMapper;
    @Resource
    private IrrigationEvaluationHelper irrigationEvaluationHelper;
    @Resource
    private IrrigationDeviceMapper irrigationDeviceMapper;

    // ── Sensor checks ─────────────────────────────────────────────────────────

    public void checkSensorData(SensorDataDO data) {
        if (data == null || data.getValue() == null) {
            return;
        }
        try {
            switch (data.getDataType()) {
                case "SOIL_MOISTURE":
                    checkSoilMoisture(data);
                    break;
                case "TEMPERATURE":
                    checkTemperature(data);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            log.warn("[AlertCheck] Sensor check failed for sensor {}: {}", data.getSensorId(), e.getMessage());
        }
    }

    private void checkSoilMoisture(SensorDataDO data) {
        if (data.getFieldId() == null) {
            return;
        }
        CropPlanDO plan = cropPlanMapper.selectCurrentByFieldId(data.getFieldId());
        if (plan == null) {
            return;
        }
        List<CropGrowthStageDO> stages = cropGrowthStageMapper.selectListByCropId(plan.getCropId());
        CropGrowthStageDO stage = irrigationEvaluationHelper.resolveCurrentStage(stages, plan.getStartDate());
        if (stage == null || stage.getSoilMoistureMin() == null) {
            return;
        }

        BigDecimal moisture = data.getValue();
        BigDecimal min = stage.getSoilMoistureMin();
        BigDecimal max = stage.getSoilMoistureMax();
        String stageName = stage.getStageName();

        String level = null;
        String context = null;

        if (moisture.compareTo(min) < 0) {
            if (moisture.compareTo(min.subtract(MOISTURE_CRITICAL_MARGIN)) < 0) {
                level = "CRITICAL";
                context = String.format(
                        "Field %d soil moisture %.1f%% is critically low — more than %.0f%% below the minimum threshold %.1f%% for growth stage '%s'.",
                        data.getFieldId(), moisture, MOISTURE_CRITICAL_MARGIN, min, stageName);
            } else {
                level = "WARN";
                context = String.format(
                        "Field %d soil moisture %.1f%% is below the minimum threshold %.1f%% for growth stage '%s'.",
                        data.getFieldId(), moisture, min, stageName);
            }
        } else if (max != null && moisture.compareTo(max) > 0) {
            level = "WARN";
            context = String.format(
                    "Field %d soil moisture %.1f%% exceeds the maximum threshold %.1f%% for growth stage '%s'. Risk of waterlogging.",
                    data.getFieldId(), moisture, max, stageName);
        }

        if (level != null) {
            raiseIfNew("SENSOR_ABNORMAL", level, data.getFarmId(), data.getFieldId(), context);
        }
    }

    private void checkTemperature(SensorDataDO data) {
        BigDecimal temp = data.getValue();
        String level = null;
        String context = null;

        if (temp.compareTo(TEMP_FROST_CRITICAL) < 0) {
            level = "CRITICAL";
            context = String.format(
                    "Temperature sensor %d reads %.1f°C — frost conditions detected. Crops may be severely damaged.",
                    data.getSensorId(), temp);
        } else if (temp.compareTo(TEMP_FROST_WARN) < 0) {
            level = "WARN";
            context = String.format(
                    "Temperature sensor %d reads %.1f°C — cold stress conditions for sensitive crops.",
                    data.getSensorId(), temp);
        } else if (temp.compareTo(TEMP_HEAT_CRITICAL) > 0) {
            level = "CRITICAL";
            context = String.format(
                    "Temperature sensor %d reads %.1f°C — extreme heat detected. Crops at high risk.",
                    data.getSensorId(), temp);
        } else if (temp.compareTo(TEMP_HEAT_WARN) > 0) {
            level = "WARN";
            context = String.format(
                    "Temperature sensor %d reads %.1f°C — heat stress conditions detected.",
                    data.getSensorId(), temp);
        }

        if (level != null) {
            raiseIfNew("SENSOR_ABNORMAL", level, data.getFarmId(), data.getFieldId(), context);
        }
    }

    // ── Weather forecast checks ────────────────────────────────────────────────

    public void checkWeatherForecast(Long farmId) {
        if (farmId == null) {
            return;
        }
        try {
            LocalDate today = LocalDate.now();
            for (int i = 1; i <= 3; i++) {
                WeatherDataDO forecast = weatherDataMapper.selectByFarmIdAndDate(farmId, today.plusDays(i));
                if (forecast != null) {
                    checkForecastDay(farmId, forecast);
                }
            }
        } catch (Exception e) {
            log.warn("[AlertCheck] Weather forecast check failed for farm {}: {}", farmId, e.getMessage());
        }
    }

    private void checkForecastDay(Long farmId, WeatherDataDO forecast) {
        LocalDate date = forecast.getForecastDate();

        if (forecast.getRainfall() != null) {
            BigDecimal rain = forecast.getRainfall();
            if (rain.compareTo(RAIN_HEAVY_CRITICAL) >= 0) {
                alertService.raiseAlert("EXTREME_WEATHER", "CRITICAL", farmId, null, null,
                        String.format("Extreme rainfall of %.1fmm forecast for %s. High risk of flooding and crop waterlogging.", rain, date));
            } else if (rain.compareTo(RAIN_HEAVY_WARN) >= 0) {
                alertService.raiseAlert("EXTREME_WEATHER", "WARN", farmId, null, null,
                        String.format("Heavy rainfall of %.1fmm forecast for %s. Review irrigation schedule to avoid over-watering.", rain, date));
            }
        }

        if (forecast.getTempMin() != null) {
            BigDecimal tempMin = forecast.getTempMin();
            if (tempMin.compareTo(TEMP_FROST_CRITICAL) < 0) {
                alertService.raiseAlert("EXTREME_WEATHER", "CRITICAL", farmId, null, null,
                        String.format("Frost risk on %s: minimum temperature forecast %.1f°C. Protect crops immediately.", date, tempMin));
            } else if (tempMin.compareTo(TEMP_FROST_WARN) < 0) {
                alertService.raiseAlert("EXTREME_WEATHER", "WARN", farmId, null, null,
                        String.format("Cold risk on %s: minimum temperature forecast %.1f°C. Monitor sensitive crops.", date, tempMin));
            }
        }

        if (forecast.getTempMax() != null) {
            BigDecimal tempMax = forecast.getTempMax();
            if (tempMax.compareTo(TEMP_HEAT_CRITICAL) >= 0) {
                alertService.raiseAlert("EXTREME_WEATHER", "CRITICAL", farmId, null, null,
                        String.format("Extreme heat on %s: maximum temperature forecast %.1f°C. Crops at high risk of heat stress.", date, tempMax));
            } else if (tempMax.compareTo(TEMP_HEAT_WARN) >= 0) {
                alertService.raiseAlert("EXTREME_WEATHER", "WARN", farmId, null, null,
                        String.format("Heat stress on %s: maximum temperature forecast %.1f°C. Consider increasing irrigation frequency.", date, tempMax));
            }
        }
    }

    private void sendWeatherNotification(String level, String context) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("level", level);
            params.put("alertType", "Extreme Weather");
            params.put("context", context);
            NotifySendSingleToUserReqDTO req = new NotifySendSingleToUserReqDTO();
            req.setUserId(NOTIFY_USER_ID);
            req.setTemplateCode(NOTIFY_TEMPLATE_CODE);
            req.setTemplateParams(params);
            notifyMessageSendApi.sendSingleMessageToAdmin(req);
            log.info("[AlertCheck] Weather notification sent: {} — {}", level, context);
        } catch (Exception e) {
            log.warn("[AlertCheck] Failed to send weather notification: {}", e.getMessage());
        }
    }

    // ── Irrigation device fault check ─────────────────────────────────────────

    /**
     * Called when an irrigation plan has been EXECUTING for too long with no device ACK.
     * Marks the device as FAULT and raises an IRRIGATION_ABNORMAL alert.
     */
    public void checkIrrigationDeviceFault(IrrigationPlanDO plan) {
        if (alertService.hasActiveAlert("IRRIGATION_ABNORMAL", plan.getFarmId(), plan.getFieldId())) {
            log.debug("[AlertCheck] Active IRRIGATION_ABNORMAL alert already exists for farm={} field={}, skipping",
                    plan.getFarmId(), plan.getFieldId());
            return;
        }
        IrrigationDeviceDO device = irrigationDeviceMapper.selectById(plan.getDeviceId());
        if (device != null && !Integer.valueOf(3).equals(device.getStatus())) {
            IrrigationDeviceDO update = new IrrigationDeviceDO();
            update.setId(device.getId());
            update.setStatus(3); // 3 = FAULT
            irrigationDeviceMapper.updateById(update);
        }
        String deviceCode = device != null ? device.getDeviceCode() : "unknown";
        String context = String.format(
                "Irrigation plan %d (field %d) started at %s but no acknowledgement received from device %s within 5 minutes. Device may be offline or faulty.",
                plan.getId(), plan.getFieldId(), plan.getActualStartTime(), deviceCode);
        alertService.raiseAlert("IRRIGATION_ABNORMAL", "CRITICAL", plan.getFarmId(), plan.getFieldId(),
                plan.getId(), context);
        log.warn("[AlertCheck] IRRIGATION_ABNORMAL raised for plan={} device={}", plan.getId(), deviceCode);
    }

    // ── Dedup helper ──────────────────────────────────────────────────────────

    private void raiseIfNew(String alertType, String level, Long farmId, Long fieldId, String context) {
        if (alertService.hasActiveAlert(alertType, farmId, fieldId)) {
            log.debug("[AlertCheck] Active {} alert already exists for farm={} field={}, skipping", alertType, farmId, fieldId);
            return;
        }
        alertService.raiseAlert(alertType, level, farmId, fieldId, null, context);
    }

}
