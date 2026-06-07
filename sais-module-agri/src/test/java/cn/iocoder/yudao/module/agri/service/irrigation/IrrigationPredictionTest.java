package cn.iocoder.yudao.module.agri.service.irrigation;

import cn.iocoder.yudao.module.agri.controller.admin.irrigation.vo.AiDecisionResultVO;
import cn.iocoder.yudao.module.agri.controller.admin.irrigation.vo.DecisionComparisonVO;
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
import cn.iocoder.yudao.module.agri.dal.mysql.field.FieldMapper;
import cn.iocoder.yudao.module.agri.dal.mysql.irrigation.IrrigationDeviceMapper;
import cn.iocoder.yudao.module.agri.dal.mysql.sensordata.SensorDataMapper;
import cn.iocoder.yudao.module.agri.dal.mysql.weather.WeatherDataMapper;
import cn.iocoder.yudao.module.agri.dal.mysql.evaluation.DecisionEvaluationRecordMapper;
import cn.iocoder.yudao.module.agri.framework.deepseek.DeepSeekClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Data-driven tests: drives sensor moisture readings + tomorrow's rainfall through the prediction
 * pipeline and asserts the resulting decision. Two parameterized methods share the same scenario
 * matrix - one exercises the rule path, the other the AI (DeepSeek) path.
 */
@ExtendWith(MockitoExtension.class)
class IrrigationPredictionTest {

    // ── Mappers shared by both helper and DecisionComparisonService ─────────

    @Mock private SensorDataMapper sensorDataMapper;
    @Mock private WeatherDataMapper weatherDataMapper;
    @Mock private CropPlanMapper cropPlanMapper;
    @Mock private CropMapper cropMapper;
    @Mock private CropGrowthStageMapper cropGrowthStageMapper;
    @Mock private FieldMapper fieldMapper;
    @Mock private IrrigationDeviceMapper irrigationDeviceMapper;

    // AI path only

    @Mock private DeepSeekClient deepSeekClient;
    @Mock private DecisionEvaluationRecordMapper decisionEvaluationRecordMapper;

    private IrrigationEvaluationHelper helper;
    private DecisionComparisonService service;

    private FieldDO field;
    private IrrigationDeviceDO device;

    @BeforeEach
    void setUp() {
        // Real helper instance with mocked mappers - required so the AI path also runs the gather logic.
        helper = new IrrigationEvaluationHelper();
        ReflectionTestUtils.setField(helper, "cropPlanMapper", cropPlanMapper);
        ReflectionTestUtils.setField(helper, "cropMapper", cropMapper);
        ReflectionTestUtils.setField(helper, "cropGrowthStageMapper", cropGrowthStageMapper);
        ReflectionTestUtils.setField(helper, "sensorDataMapper", sensorDataMapper);
        ReflectionTestUtils.setField(helper, "weatherDataMapper", weatherDataMapper);

        service = new DecisionComparisonService();
        ReflectionTestUtils.setField(service, "fieldMapper", fieldMapper);
        ReflectionTestUtils.setField(service, "cropPlanMapper", cropPlanMapper);
        ReflectionTestUtils.setField(service, "irrigationDeviceMapper", irrigationDeviceMapper);
        ReflectionTestUtils.setField(service, "helper", helper);
        ReflectionTestUtils.setField(service, "deepSeekClient", deepSeekClient);
        ReflectionTestUtils.setField(service, "decisionEvaluationRecordMapper", decisionEvaluationRecordMapper);

        field = new FieldDO();
        field.setId(1L);
        field.setFieldName("Field A");
        field.setFarmId(10L);

        device = new IrrigationDeviceDO();
        device.setId(100L);
        device.setDeviceCode("DEV-001");
        device.setFieldId(1L);
        device.setSensorId(200L);

        // lenient: not all tests reach DeepSeek; avoids UnnecessaryStubbingException for rule tests.
        lenient().when(deepSeekClient.isConfigured()).thenReturn(true);
    }

    // ── Scenario matrix: one source for both rule and AI tests ─────────────

    static Stream<Arguments> predictionCases() {
        return Stream.of(
                Arguments.of("adequate moisture, no rain -> NO_ACTION",                  "55.0", "0.0",  "NO_ACTION"),
                Arguments.of("adequate moisture, heavy rain -> NO_ACTION",               "55.0", "20.0", "NO_ACTION"),
                Arguments.of("moisture exactly at min -> NO_ACTION",                     "40.0", "0.0",  "NO_ACTION"),
                Arguments.of("below min, rain at threshold (5mm) -> SKIP",               "30.0", "5.0",  "SKIP"),
                Arguments.of("below min, heavy rain -> SKIP",                            "25.0", "15.0", "SKIP"),
                Arguments.of("below min, no rain -> IRRIGATE",                           "30.0", "0.0",  "IRRIGATE"),
                Arguments.of("below min, rain just below threshold (4.9mm) -> IRRIGATE", "35.0", "4.9",  "IRRIGATE"),
                Arguments.of("zero moisture, no rain -> IRRIGATE",                       "0.0",  "0.0",  "IRRIGATE")
        );
    }

    // ── Rule path ───────────────────────────────────────────────────────────

    @ParameterizedTest(name = "[rule] {0}")
    @MethodSource("predictionCases")
    void rulePrediction_forSensorAndWeather(String name, String moisture, String rainfall, String expected) {
        stubMappers(new BigDecimal(moisture), new BigDecimal(rainfall));

        AiDecisionResultVO ctx = helper.gatherFieldDataForDevice(field, device);
        AiDecisionResultVO result = helper.applyRules(ctx);

        assertEquals(expected, result.getDecision(),
                "rule decision mismatch for [" + name + "]");
        assertEquals(new BigDecimal(moisture), result.getCurrentMoisture());
        assertEquals(new BigDecimal(rainfall), result.getTomorrowRainfall());
    }

    // ── AI path ─────────────────────────────────────────────────────────────

    @ParameterizedTest(name = "[ai] {0}")
    @MethodSource("predictionCases")
    void aiPrediction_forSensorAndWeather(String name, String moisture, String rainfall, String expected) throws Exception {
        stubMappers(new BigDecimal(moisture), new BigDecimal(rainfall));
        when(cropPlanMapper.selectCurrentFieldIds()).thenReturn(List.of(1L));
        when(fieldMapper.selectBatchIds(any())).thenReturn(List.of(field));
        when(irrigationDeviceMapper.selectListByFieldId(1L)).thenReturn(List.of(device));
        when(deepSeekClient.complete(any())).thenReturn(jsonForExpected(expected));

        DecisionComparisonVO row = service.compareAll().get(0);

        assertEquals(expected, row.getAiDecision(),
                "AI decision mismatch for [" + name + "]");

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(deepSeekClient).complete(captor.capture());
        String prompt = captor.getValue();
        // Verify the prompt actually carries the sensor reading and weather forecast we mocked.
        assertTrue(prompt.contains(String.format("%.1f%%", new BigDecimal(moisture))),
                "prompt should contain moisture " + moisture + ", got: " + prompt);
        assertTrue(prompt.contains(String.format("%.1f mm", new BigDecimal(rainfall))),
                "prompt should contain rainfall " + rainfall + " mm, got: " + prompt);
    }

    // ── Shared mapper stubbing ─────────────────────────────────────────────

    private void stubMappers(BigDecimal moisture, BigDecimal rainfall) {
        CropPlanDO plan = new CropPlanDO();
        plan.setId(500L);
        plan.setCropId(300L);
        plan.setFieldId(1L);
        plan.setStartDate(LocalDate.now().minusDays(30));

        CropDO crop = new CropDO();
        crop.setId(300L);
        crop.setCropName("Tomato");

        CropGrowthStageDO stage = new CropGrowthStageDO();
        stage.setId(400L);
        stage.setCropId(300L);
        stage.setStageName("Flowering");
        stage.setDurationDays(180);
        stage.setSoilMoistureMin(new BigDecimal("40.0"));
        stage.setSoilMoistureOptimal(new BigDecimal("55.0"));
        stage.setSoilMoistureMax(new BigDecimal("70.0"));

        SensorDataDO reading = new SensorDataDO();
        reading.setSensorId(200L);
        reading.setFieldId(1L);
        reading.setDataType("SOIL_MOISTURE");
        reading.setValue(moisture);

        WeatherDataDO weather = new WeatherDataDO();
        weather.setFarmId(10L);
        weather.setForecastDate(LocalDate.now().plusDays(1));
        weather.setRainfall(rainfall);

        when(cropPlanMapper.selectCurrentByFieldId(1L)).thenReturn(plan);
        when(cropMapper.selectById(300L)).thenReturn(crop);
        when(cropGrowthStageMapper.selectListByCropId(300L)).thenReturn(List.of(stage));
        when(sensorDataMapper.selectLatestBySensorId(200L)).thenReturn(List.of(reading));
        when(weatherDataMapper.selectByFarmIdAndDate(eq(10L), eq(LocalDate.now().plusDays(1))))
                .thenReturn(weather);
    }

    // ── Helpers ─────────────────────────────────────────────────────────────

    private static String jsonForExpected(String decision) {
        if ("IRRIGATE".equals(decision)) {
            return "{\"decision\":\"IRRIGATE\",\"reason\":\"AI says soil is dry.\",\"durationMinutes\":30}";
        }
        if ("SKIP".equals(decision)) {
            return "{\"decision\":\"SKIP\",\"reason\":\"AI says rain expected.\"}";
        }
        return "{\"decision\":\"NO_ACTION\",\"reason\":\"AI says moisture adequate.\"}";
    }

}
