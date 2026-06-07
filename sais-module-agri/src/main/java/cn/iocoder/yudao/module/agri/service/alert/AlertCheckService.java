package cn.iocoder.yudao.module.agri.service.alert;

import cn.iocoder.yudao.module.agri.dal.dataobject.crop.CropGrowthStageDO;
import cn.iocoder.yudao.module.agri.dal.dataobject.crop.CropDO;
import cn.iocoder.yudao.module.agri.dal.dataobject.crop.CropPlanDO;
import cn.iocoder.yudao.module.agri.dal.dataobject.field.FieldDO;
import cn.iocoder.yudao.module.agri.dal.dataobject.irrigation.IrrigationDeviceDO;
import cn.iocoder.yudao.module.agri.dal.dataobject.irrigation.IrrigationPlanDO;
import cn.iocoder.yudao.module.agri.dal.dataobject.sensordata.SensorDataDO;
import cn.iocoder.yudao.module.agri.dal.dataobject.weather.WeatherDataDO;
import cn.iocoder.yudao.module.agri.dal.mysql.crop.CropGrowthStageMapper;
import cn.iocoder.yudao.module.agri.dal.mysql.crop.CropMapper;
import cn.iocoder.yudao.module.agri.dal.mysql.crop.CropPlanMapper;
import cn.iocoder.yudao.module.agri.dal.mysql.field.FieldMapper;
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
import java.util.ArrayList;
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
    private static final BigDecimal WEATHER_HEAT_CRITICAL = new BigDecimal("32");
    private static final BigDecimal RAIN_HEAVY_WARN     = new BigDecimal("25");
    private static final BigDecimal RAIN_HEAVY_CRITICAL = new BigDecimal("50");
    private static final BigDecimal RAIN_DROUGHT_THREE_DAY = new BigDecimal("5");
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
    private CropMapper cropMapper;
    @Resource
    private FieldMapper fieldMapper;
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
                        "Field %d soil moisture %.1f%% is critically low - more than %.0f%% below the minimum threshold %.1f%% for growth stage '%s'.",
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

    // ── Weather forecast checks ────────────────────────────────────────────────

    public void checkWeatherForecast(Long farmId) {
        if (farmId == null) {
            return;
        }
        try {
            LocalDate today = LocalDate.now();
            List<WeatherDataDO> forecasts = new ArrayList<>();
            for (int i = 1; i <= 3; i++) {
                WeatherDataDO forecast = weatherDataMapper.selectByFarmIdAndDate(farmId, today.plusDays(i));
                if (forecast != null) {
                    forecasts.add(forecast);
                    checkForecastDay(farmId, forecast);
                }
            }
            checkCropWaterRisk(farmId, forecasts);
        } catch (Exception e) {
            log.warn("[AlertCheck] Weather forecast check failed for farm {}: {}", farmId, e.getMessage());
        }
    }

    private void checkForecastDay(Long farmId, WeatherDataDO forecast) {
        LocalDate date = forecast.getForecastDate();

        if (forecast.getTempMin() != null) {
            BigDecimal tempMin = forecast.getTempMin();
            if (tempMin.compareTo(TEMP_FROST_CRITICAL) < 0) {
                raiseIfNew("EXTREME_WEATHER", "CRITICAL", farmId, null,
                        String.format("Frost risk on %s: minimum temperature forecast %.1f°C. Protect crops immediately.", date, tempMin));
            }
        }

        if (forecast.getTempMax() != null) {
            BigDecimal tempMax = forecast.getTempMax();
            if (tempMax.compareTo(WEATHER_HEAT_CRITICAL) >= 0) {
                raiseIfNew("EXTREME_WEATHER", "CRITICAL", farmId, null,
                        String.format("Extreme heat on %s: maximum temperature forecast %.1f°C. Crops at high risk of heat stress.", date, tempMax));
            }
        }
    }

    private void checkCropWaterRisk(Long farmId, List<WeatherDataDO> forecasts) {
        if (forecasts.isEmpty()) {
            return;
        }
        BigDecimal totalRain = BigDecimal.ZERO;
        BigDecimal maxDailyRain = BigDecimal.ZERO;
        for (WeatherDataDO forecast : forecasts) {
            if (forecast.getRainfall() == null) {
                continue;
            }
            totalRain = totalRain.add(forecast.getRainfall());
            if (forecast.getRainfall().compareTo(maxDailyRain) > 0) {
                maxDailyRain = forecast.getRainfall();
            }
        }

        List<CropPlanDO> currentPlans = cropPlanMapper.selectCurrentList();
        if (currentPlans == null || currentPlans.isEmpty()) {
            return;
        }
        for (CropPlanDO plan : currentPlans) {
            FieldDO field = fieldMapper.selectById(plan.getFieldId());
            if (field == null || !farmId.equals(field.getFarmId())) {
                continue;
            }
            CropDO crop = cropMapper.selectById(plan.getCropId());
            if (crop == null) {
                continue;
            }
            checkCropDroughtRisk(farmId, field, crop, totalRain);
            checkCropWaterloggingRisk(farmId, field, crop, totalRain, maxDailyRain);
        }
    }

    private void checkCropDroughtRisk(Long farmId, FieldDO field, CropDO crop, BigDecimal totalRain) {
        if (!Integer.valueOf(1).equals(crop.getDroughtResistance())) {
            return;
        }
        if (totalRain.compareTo(RAIN_DROUGHT_THREE_DAY) >= 0) {
            return;
        }
        String context = String.format(
                "Field %d (%s) is growing %s, which has weak drought resistance. Only %.1fmm rain is forecast over the next 3 days, so soil moisture may become insufficient.",
                field.getId(), field.getFieldName(), crop.getCropName(), totalRain);
        raiseIfNew("CROP_WATER_RISK", "WARN", farmId, field.getId(), context);
    }

    private void checkCropWaterloggingRisk(Long farmId, FieldDO field, CropDO crop,
                                           BigDecimal totalRain, BigDecimal maxDailyRain) {
        if (Integer.valueOf(3).equals(crop.getWaterloggingTolerance())) {
            return;
        }
        boolean heavyDailyRain = maxDailyRain.compareTo(RAIN_HEAVY_WARN) >= 0;
        boolean extremeThreeDayRain = totalRain.compareTo(RAIN_HEAVY_CRITICAL) >= 0;
        if (!heavyDailyRain && !extremeThreeDayRain) {
            return;
        }
        boolean weakTolerance = Integer.valueOf(1).equals(crop.getWaterloggingTolerance());
        String level = weakTolerance || extremeThreeDayRain ? "CRITICAL" : "WARN";
        String context = String.format(
                "Field %d (%s) is growing %s, whose waterlogging tolerance is %s. Forecast rainfall is %.1fmm over 3 days with a maximum daily rainfall of %.1fmm, increasing the risk of excessive soil moisture.",
                field.getId(), field.getFieldName(), crop.getCropName(),
                toleranceLabel(crop.getWaterloggingTolerance()), totalRain, maxDailyRain);
        raiseIfNew("CROP_WATER_RISK", level, farmId, field.getId(), context);
    }

    private String toleranceLabel(Integer value) {
        if (Integer.valueOf(1).equals(value)) {
            return "weak";
        }
        if (Integer.valueOf(2).equals(value)) {
            return "medium";
        }
        if (Integer.valueOf(3).equals(value)) {
            return "strong";
        }
        return "unknown";
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
            log.info("[AlertCheck] Weather notification sent: {} - {}", level, context);
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
