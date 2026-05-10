package cn.iocoder.yudao.module.agri.service.irrigation;

import cn.iocoder.yudao.module.agri.controller.admin.irrigation.vo.AiDecisionResultVO;
import cn.iocoder.yudao.module.agri.dal.dataobject.crop.CropDO;
import cn.iocoder.yudao.module.agri.dal.dataobject.crop.CropGrowthStageDO;
import cn.iocoder.yudao.module.agri.dal.dataobject.crop.CropPlanDO;
import cn.iocoder.yudao.module.agri.dal.dataobject.field.FieldDO;
import cn.iocoder.yudao.module.agri.dal.dataobject.irrigation.IrrigationDeviceDO;
import cn.iocoder.yudao.module.agri.dal.dataobject.sensordata.SensorDataDO;
import cn.iocoder.yudao.module.agri.dal.dataobject.weather.WeatherDataDO;
import cn.iocoder.yudao.module.agri.dal.mysql.crop.CropGrowthStageMapper;
import cn.iocoder.yudao.module.agri.dal.mysql.crop.CropMapper;
import cn.iocoder.yudao.module.agri.dal.mysql.crop.CropPlanMapper;
import cn.iocoder.yudao.module.agri.dal.mysql.sensordata.SensorDataMapper;
import cn.iocoder.yudao.module.agri.dal.mysql.weather.WeatherDataMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Shared helper used by both the rule-based and Bedrock irrigation decision implementations.
 * Handles data-gathering, rule application, and device activation so neither impl duplicates logic.
 */
@Component
@Slf4j
public class IrrigationEvaluationHelper {

    private static final BigDecimal RAIN_SKIP_THRESHOLD = new BigDecimal("5");

    @Resource
    private CropPlanMapper cropPlanMapper;
    @Resource
    private CropMapper cropMapper;
    @Resource
    private CropGrowthStageMapper cropGrowthStageMapper;
    @Resource
    private SensorDataMapper sensorDataMapper;
    @Resource
    private WeatherDataMapper weatherDataMapper;
    @Resource
    private IrrigationPlanService irrigationPlanService;

    /**
     * Steps 1–5: load crop plan, crop, current growth stage, soil moisture reading, and tomorrow's weather.
     * Uses device's associated sensor if configured; falls back to field-level sensor data otherwise.
     * Returns a partially populated VO. If any required data is missing, sets decision=NO_DATA and returns early.
     */
    public AiDecisionResultVO gatherFieldDataForDevice(FieldDO field, IrrigationDeviceDO device) {
        AiDecisionResultVO result = new AiDecisionResultVO();
        result.setFieldId(field.getId());
        result.setFieldName(field.getFieldName());

        CropPlanDO plan = cropPlanMapper.selectCurrentByFieldId(field.getId());
        if (plan == null) {
            return noData(result, "No active crop plan found for this field.");
        }
        result.setCropPlanId(plan.getId());

        CropDO crop = cropMapper.selectById(plan.getCropId());
        if (crop != null) {
            result.setCropName(crop.getCropName());
        }

        List<CropGrowthStageDO> stages = cropGrowthStageMapper.selectListByCropId(plan.getCropId());
        CropGrowthStageDO stage = resolveCurrentStage(stages, plan.getStartDate());
        if (stage == null) {
            String label = crop != null ? "'" + crop.getCropName() + "'" : "ID " + plan.getCropId();
            return noData(result, "No growth stages configured for crop " + label + ".");
        }
        result.setStageName(stage.getStageName());
        result.setMoistureMin(stage.getSoilMoistureMin());
        result.setMoistureMax(stage.getSoilMoistureMax());
        result.setMoistureOptimal(stage.getSoilMoistureOptimal());

        List<SensorDataDO> latestReadings = device.getSensorId() != null
                ? sensorDataMapper.selectLatestBySensorId(device.getSensorId())
                : sensorDataMapper.selectLatestByFieldId(field.getId());
        BigDecimal soilMoisture = latestReadings.stream()
                .filter(sd -> "SOIL_MOISTURE".equals(sd.getDataType()))
                .map(SensorDataDO::getValue)
                .findFirst()
                .orElse(null);
        if (soilMoisture == null) {
            return noData(result, "No soil moisture sensor data available for device " + device.getDeviceCode() + ".");
        }
        result.setCurrentMoisture(soilMoisture);

        LocalDate tomorrow = LocalDate.now().plusDays(1);
        WeatherDataDO tomorrowWeather = weatherDataMapper.selectByFarmIdAndDate(field.getFarmId(), tomorrow);
        BigDecimal rainfall = (tomorrowWeather != null && tomorrowWeather.getRainfall() != null)
                ? tomorrowWeather.getRainfall() : BigDecimal.ZERO;
        result.setTomorrowRainfall(rainfall);

        return result;
    }

    /**
     * Step 6 (rule-based path): applies threshold logic to produce IRRIGATE / SKIP / NO_ACTION.
     * The ctx must have been fully populated by gatherFieldData (decision != NO_DATA).
     */
    public AiDecisionResultVO applyRules(AiDecisionResultVO ctx) {
        BigDecimal soilMoisture = ctx.getCurrentMoisture();
        BigDecimal moistureMin = ctx.getMoistureMin();
        BigDecimal moistureOptimal = ctx.getMoistureOptimal() != null
                ? ctx.getMoistureOptimal() : ctx.getMoistureMax();
        BigDecimal tomorrowRainfall = ctx.getTomorrowRainfall() != null
                ? ctx.getTomorrowRainfall() : BigDecimal.ZERO;

        if (soilMoisture.compareTo(moistureMin) >= 0) {
            BigDecimal displayMax = ctx.getMoistureMax() != null ? ctx.getMoistureMax() : moistureOptimal;
            ctx.setDecision("NO_ACTION");
            ctx.setReason(String.format(
                    "Soil moisture (%.1f%%) is within acceptable range [%.1f%%, %.1f%%] for growth stage '%s'. No irrigation needed.",
                    soilMoisture, moistureMin, displayMax, ctx.getStageName()));
            return ctx;
        }

        if (tomorrowRainfall.compareTo(RAIN_SKIP_THRESHOLD) >= 0) {
            ctx.setDecision("SKIP");
            ctx.setReason(String.format(
                    "Soil moisture (%.1f%%) is below minimum (%.1f%%) for stage '%s', but %.1fmm of rain is forecast tomorrow — skipping irrigation to avoid over-watering.",
                    soilMoisture, moistureMin, ctx.getStageName(), tomorrowRainfall));
            return ctx;
        }

        int duration = estimateDuration(soilMoisture, moistureOptimal);
        StringBuilder reason = new StringBuilder();
        reason.append(String.format(
                "Soil moisture (%.1f%%) is below minimum threshold (%.1f%%) for growth stage '%s'.",
                soilMoisture, moistureMin, ctx.getStageName()));
        if (tomorrowRainfall.compareTo(BigDecimal.ZERO) > 0) {
            reason.append(String.format(" Forecast rainfall (%.1fmm) is below the %.0fmm skip threshold.",
                    tomorrowRainfall, RAIN_SKIP_THRESHOLD));
        }
        reason.append(String.format(" Target optimal moisture: %.1f%%. Recommended irrigation duration: %d min.",
                moistureOptimal, duration));
        ctx.setDecision("IRRIGATE");
        ctx.setReason(reason.toString());
        return ctx;
    }

    /**
     * Creates an irrigation plan for the given device and sets irrigationPlanId on result.
     * Should only be called when result.decision == "IRRIGATE".
     * Execution (MQTT dispatch) is handled asynchronously by IrrigationPlanExecutionJob.
     */
    public void activateDevice(FieldDO field, IrrigationDeviceDO device, AiDecisionResultVO result) {
        if (Boolean.TRUE.equals(device.getIsWatering())) {
            result.setReason(result.getReason() + " Device " + device.getDeviceCode() + " is already irrigating.");
            return;
        }
        if (device.getStatus() == null || device.getStatus() != 1) {
            result.setReason(result.getReason() + " Device " + device.getDeviceCode() + " is offline or inactive — plan not created.");
            return;
        }
        int duration = estimateDuration(result.getCurrentMoisture(), result.getMoistureOptimal());
        Long planId = irrigationPlanService.createPlanFromAI(
                device.getId(), field.getFarmId(), field.getId(),
                result.getCropPlanId(), result.getReason(),
                LocalDateTime.now(), duration);
        result.setIrrigationPlanId(planId);
    }

    /**
     * Walks growth stages in order and returns the one covering the current day.
     * Falls back to the last stage if the plan has run longer than all defined stages.
     */
    public CropGrowthStageDO resolveCurrentStage(List<CropGrowthStageDO> stages, LocalDate startDate) {
        if (stages == null || stages.isEmpty()) return null;
        long daysElapsed = ChronoUnit.DAYS.between(startDate, LocalDate.now());
        long accumulated = 0;
        for (CropGrowthStageDO stage : stages) {
            if (stage.getDurationDays() == null) continue;
            accumulated += stage.getDurationDays();
            if (daysElapsed < accumulated) return stage;
        }
        return stages.get(stages.size() - 1);
    }

    /** 2 minutes per percentage-point deficit, clamped to [15, 120]. */
    public int estimateDuration(BigDecimal current, BigDecimal optimal) {
        if (optimal == null) return 30;
        double deficit = optimal.subtract(current).doubleValue();
        return Math.max(15, Math.min((int) Math.round(deficit * 2.0), 120));
    }

    private AiDecisionResultVO noData(AiDecisionResultVO result, String reason) {
        result.setDecision("NO_DATA");
        result.setReason(reason);
        return result;
    }

}
