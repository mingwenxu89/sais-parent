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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlertCheckServiceTest {

    @Mock AlertService alertService;
    @Mock CropPlanMapper cropPlanMapper;
    @Mock CropGrowthStageMapper cropGrowthStageMapper;
    @Mock WeatherDataMapper weatherDataMapper;
    @Mock IrrigationEvaluationHelper irrigationEvaluationHelper;
    @Mock IrrigationDeviceMapper irrigationDeviceMapper;

    @InjectMocks AlertCheckService service;

    @BeforeEach
    void noActiveAlerts() {
        // lenient: most tests need this default, but irrigationFault_skipsDuplicate overrides it
        lenient().when(alertService.hasActiveAlert(any(), any(), any())).thenReturn(false);
    }

    // ── SOIL MOISTURE ─────────────────────────────────────────────────────────

    static Stream<Arguments> soilMoistureCases() {
        return Stream.of(
            // description,                   moisture, min,  max,  expectedLevel  (null = no alert)
            Arguments.of("within range",       "50.0",  "30.0", "70.0", null),
            Arguments.of("exactly at min",     "30.0",  "30.0", "70.0", null),
            Arguments.of("just below min",     "29.9",  "30.0", "70.0", "WARN"),
            Arguments.of("15% below min=WARN", "15.1",  "30.0", "70.0", "WARN"),
            Arguments.of("exactly 15% below",  "15.0",  "30.0", "70.0", "WARN"),    // 15.0 < 15.0 is false → WARN
            Arguments.of("critically low",     "5.0",   "30.0", "70.0", "CRITICAL"),
            Arguments.of("above max",          "75.0",  "30.0", "70.0", "WARN"),
            Arguments.of("exactly at max",     "70.0",  "30.0", "70.0", null)
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("soilMoistureCases")
    @DisplayName("checkSensorData — SOIL_MOISTURE thresholds")
    void soilMoisture_thresholds(String desc, String moisture, String min, String max, String expectedLevel) {
        SensorDataDO data = sensorData("SOIL_MOISTURE", moisture, 1L, 10L, 100L);
        CropPlanDO plan = cropPlan(1L, 42L);
        CropGrowthStageDO stage = growthStage("Seedling", min, max);

        when(cropPlanMapper.selectCurrentByFieldId(10L)).thenReturn(plan);
        when(cropGrowthStageMapper.selectListByCropId(42L)).thenReturn(List.of(stage));
        when(irrigationEvaluationHelper.resolveCurrentStage(anyList(), any())).thenReturn(stage);

        service.checkSensorData(data);

        if (expectedLevel == null) {
            verify(alertService, never()).raiseAlert(any(), any(), any(), any(), any(), any());
        } else {
            ArgumentCaptor<String> levelCaptor = ArgumentCaptor.forClass(String.class);
            verify(alertService).raiseAlert(eq("SENSOR_ABNORMAL"), levelCaptor.capture(), any(), any(), any(), any());
            assertEquals(expectedLevel, levelCaptor.getValue(), "Wrong level for: " + desc);
        }
    }

    // ── TEMPERATURE ───────────────────────────────────────────────────────────

    static Stream<Arguments> temperatureCases() {
        return Stream.of(
            // description,            temp,   expectedLevel (null = no alert)
            Arguments.of("below 0°C",   "-1.0", "CRITICAL"),
            Arguments.of("exactly 0°C", "0.0",  "WARN"),      // 0 < 0 is false → falls to < 5 → WARN
            Arguments.of("0–5°C",       "3.0",  "WARN"),
            Arguments.of("exactly 5°C", "5.0",  null),         // 5 < 5 is false → no frost alert
            Arguments.of("normal",      "20.0", null),
            Arguments.of("exactly 35°C","35.0", null),         // 35 > 35 is false → no heat alert
            Arguments.of("35.1°C",      "35.1", "WARN"),
            Arguments.of("above 38°C",  "39.0", "CRITICAL"),
            Arguments.of("exactly 38°C","38.0", "WARN")        // 38 > 38 is false, but 38 > 35 → WARN
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("temperatureCases")
    @DisplayName("checkSensorData — TEMPERATURE thresholds")
    void temperature_thresholds(String desc, String temp, String expectedLevel) {
        SensorDataDO data = sensorData("TEMPERATURE", temp, 1L, null, 100L);

        service.checkSensorData(data);

        if (expectedLevel == null) {
            verify(alertService, never()).raiseAlert(any(), any(), any(), any(), any(), any());
        } else {
            ArgumentCaptor<String> levelCaptor = ArgumentCaptor.forClass(String.class);
            verify(alertService).raiseAlert(eq("SENSOR_ABNORMAL"), levelCaptor.capture(), any(), any(), any(), any());
            assertEquals(expectedLevel, levelCaptor.getValue(), "Wrong level for: " + desc);
        }
    }

    // ── WEATHER FORECAST ──────────────────────────────────────────────────────

    @Test
    @DisplayName("checkWeatherForecast — heavy rain CRITICAL (>=50mm)")
    void weatherForecast_rainCritical() {
        mockForecast(100L, "55.0", null, null);

        service.checkWeatherForecast(100L);

        verify(alertService).raiseAlert(eq("EXTREME_WEATHER"), eq("CRITICAL"), eq(100L), isNull(), isNull(),
                argThat(ctx -> ctx.contains("flooding")));
    }

    @Test
    @DisplayName("checkWeatherForecast — heavy rain WARN (25–49.9mm)")
    void weatherForecast_rainWarn() {
        mockForecast(100L, "30.0", null, null);

        service.checkWeatherForecast(100L);

        verify(alertService).raiseAlert(eq("EXTREME_WEATHER"), eq("WARN"), eq(100L), isNull(), isNull(), any());
    }

    @Test
    @DisplayName("checkWeatherForecast — frost CRITICAL (tempMin < 0°C)")
    void weatherForecast_frostCritical() {
        mockForecast(100L, null, "-2.0", "25.0");

        service.checkWeatherForecast(100L);

        verify(alertService).raiseAlert(eq("EXTREME_WEATHER"), eq("CRITICAL"), eq(100L), isNull(), isNull(),
                argThat(ctx -> ctx.contains("Frost")));
    }

    @Test
    @DisplayName("checkWeatherForecast — heat WARN (tempMax > 35°C)")
    void weatherForecast_heatWarn() {
        mockForecast(100L, null, "10.0", "36.0");

        service.checkWeatherForecast(100L);

        verify(alertService).raiseAlert(eq("EXTREME_WEATHER"), eq("WARN"), eq(100L), isNull(), isNull(),
                argThat(ctx -> ctx.contains("Heat stress")));
    }

    @Test
    @DisplayName("checkWeatherForecast — normal conditions produce no alert")
    void weatherForecast_noAlert() {
        mockForecast(100L, "10.0", "10.0", "25.0");

        service.checkWeatherForecast(100L);

        verify(alertService, never()).raiseAlert(any(), any(), any(), any(), any(), any());
    }

    // ── IRRIGATION ABNORMAL ───────────────────────────────────────────────────

    @Test
    @DisplayName("checkIrrigationDeviceFault — raises CRITICAL and marks device FAULT")
    void irrigationFault_raisesAlertAndMarksDeviceFault() {
        IrrigationDeviceDO device = new IrrigationDeviceDO();
        device.setId(5L);
        device.setDeviceCode("IRR-001");
        device.setStatus(1); // 1 = ACTIVE

        IrrigationPlanDO plan = new IrrigationPlanDO();
        plan.setId(99L);
        plan.setFarmId(1L);
        plan.setFieldId(10L);
        plan.setDeviceId(5L);
        plan.setActualStartTime(LocalDateTime.now().minusMinutes(6));

        when(irrigationDeviceMapper.selectById(5L)).thenReturn(device);

        service.checkIrrigationDeviceFault(plan);

        // Device status updated to FAULT (3)
        ArgumentCaptor<IrrigationDeviceDO> deviceCaptor = ArgumentCaptor.forClass(IrrigationDeviceDO.class);
        verify(irrigationDeviceMapper).updateById((IrrigationDeviceDO) deviceCaptor.capture());
        assertEquals(3, deviceCaptor.getValue().getStatus());

        // IRRIGATION_ABNORMAL CRITICAL alert raised with plan ID
        verify(alertService).raiseAlert(eq("IRRIGATION_ABNORMAL"), eq("CRITICAL"),
                eq(1L), eq(10L), eq(99L), anyString());
    }

    @Test
    @DisplayName("checkIrrigationDeviceFault — dedup: skips if active alert already exists")
    void irrigationFault_skipsDuplicate() {
        when(alertService.hasActiveAlert("IRRIGATION_ABNORMAL", 1L, 10L)).thenReturn(true);

        IrrigationPlanDO plan = new IrrigationPlanDO();
        plan.setId(99L);
        plan.setFarmId(1L);
        plan.setFieldId(10L);
        plan.setDeviceId(5L);

        service.checkIrrigationDeviceFault(plan);

        verify(irrigationDeviceMapper, never()).updateById(any(IrrigationDeviceDO.class));
        verify(alertService, never()).raiseAlert(any(), any(), any(), any(), any(), any());
    }

    @Test
    @DisplayName("checkIrrigationDeviceFault — device already FAULT: no redundant update")
    void irrigationFault_deviceAlreadyFault_noRedundantUpdate() {
        IrrigationDeviceDO device = new IrrigationDeviceDO();
        device.setId(5L);
        device.setDeviceCode("IRR-001");
        device.setStatus(3); // 3 = FAULT

        IrrigationPlanDO plan = new IrrigationPlanDO();
        plan.setId(99L);
        plan.setFarmId(1L);
        plan.setFieldId(10L);
        plan.setDeviceId(5L);
        plan.setActualStartTime(LocalDateTime.now().minusMinutes(10));

        when(irrigationDeviceMapper.selectById(5L)).thenReturn(device);

        service.checkIrrigationDeviceFault(plan);

        // Should still raise alert but NOT update device status again
        verify(irrigationDeviceMapper, never()).updateById(any(IrrigationDeviceDO.class));
        verify(alertService).raiseAlert(eq("IRRIGATION_ABNORMAL"), eq("CRITICAL"), any(), any(), any(), any());
    }

    // ── null / unknown sensor type guards ─────────────────────────────────────

    @Test
    @DisplayName("checkSensorData — null data is silently ignored")
    void checkSensorData_nullData() {
        assertDoesNotThrow(() -> service.checkSensorData(null));
        verifyNoInteractions(alertService);
    }

    @Test
    @DisplayName("checkSensorData — unknown sensor type produces no alert")
    void checkSensorData_unknownType() {
        SensorDataDO data = sensorData("HUMIDITY", "60.0", 1L, 10L, 100L);

        service.checkSensorData(data);

        verifyNoInteractions(alertService);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private SensorDataDO sensorData(String type, String value, Long sensorId, Long fieldId, Long farmId) {
        SensorDataDO d = new SensorDataDO();
        d.setSensorId(sensorId);
        d.setFieldId(fieldId);
        d.setFarmId(farmId);
        d.setDataType(type);
        d.setValue(new BigDecimal(value));
        return d;
    }

    private CropPlanDO cropPlan(Long fieldId, Long cropId) {
        CropPlanDO p = new CropPlanDO();
        p.setFieldId(fieldId);
        p.setCropId(cropId);
        p.setStartDate(LocalDate.now().minusDays(10));
        return p;
    }

    private CropGrowthStageDO growthStage(String name, String min, String max) {
        CropGrowthStageDO s = new CropGrowthStageDO();
        s.setStageName(name);
        s.setSoilMoistureMin(new BigDecimal(min));
        s.setSoilMoistureMax(new BigDecimal(max));
        return s;
    }

    private void mockForecast(Long farmId, String rainfall, String tempMin, String tempMax) {
        WeatherDataDO forecast = new WeatherDataDO();
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        forecast.setForecastDate(tomorrow);
        if (rainfall != null) forecast.setRainfall(new BigDecimal(rainfall));
        if (tempMin  != null) forecast.setTempMin(new BigDecimal(tempMin));
        if (tempMax  != null) forecast.setTempMax(new BigDecimal(tempMax));
        // Only stub day+1; days +2 and +3 return null (no forecast) so only 1 alert fires
        when(weatherDataMapper.selectByFarmIdAndDate(eq(farmId), eq(tomorrow))).thenReturn(forecast);
    }
}
