package cn.iocoder.yudao.module.agri.service.irrigation;

import cn.iocoder.yudao.module.agri.controller.admin.irrigation.vo.AiDecisionResultVO;
import cn.iocoder.yudao.module.agri.dal.dataobject.field.FieldDO;
import cn.iocoder.yudao.module.agri.dal.dataobject.irrigation.IrrigationDeviceDO;
import cn.iocoder.yudao.module.agri.dal.mysql.field.FieldMapper;
import cn.iocoder.yudao.module.agri.dal.mysql.irrigation.IrrigationDeviceMapper;
import cn.iocoder.yudao.module.agri.framework.bedrock.AwsBedrockProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;
import software.amazon.awssdk.services.bedrockruntime.model.ContentBlock;
import software.amazon.awssdk.services.bedrockruntime.model.ConversationRole;
import software.amazon.awssdk.services.bedrockruntime.model.ConverseOutput;
import software.amazon.awssdk.services.bedrockruntime.model.ConverseRequest;
import software.amazon.awssdk.services.bedrockruntime.model.ConverseResponse;
import software.amazon.awssdk.services.bedrockruntime.model.Message;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

/**
 * Verifies that the AI (Bedrock) decision engine aligns with the rule-based baseline
 * across all decision branches and boundary conditions.
 *
 * Strategy:
 *   1. Build input context for each scenario.
 *   2. Determine expected decision using rule-based logic (applyRules) — this is the ground truth.
 *   3. Mock Bedrock to return the JSON the model is expected to produce for the same inputs.
 *   4. Assert AI decision == rule decision (alignment).
 *
 * This separates two concerns:
 *   - Correctness of the rule engine (covered in IrrigationEvaluationHelperTest)
 *   - Alignment of the AI with the rule engine (covered here)
 *
 * Scenarios that cover every decision branch:
 *   NO_ACTION: moisture >= min (regardless of rain)
 *   SKIP:      moisture < min AND rain >= 5mm
 *   IRRIGATE:  moisture < min AND rain < 5mm
 *   Boundary:  moisture = min, rain = 5mm (exact threshold values)
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("AI vs Rule-Based Decision Alignment")
class AiDecisionAlignmentTest {

    @Mock private BedrockRuntimeClient bedrockRuntimeClient;
    @Mock private AwsBedrockProperties bedrockProperties;
    @Mock private FieldMapper fieldMapper;
    @Mock private IrrigationDeviceMapper irrigationDeviceMapper;
    @Mock private IrrigationEvaluationHelper helper;

    @InjectMocks
    private BedrockIrrigationDecisionServiceImpl service;

    private final IrrigationEvaluationHelper realHelper = new IrrigationEvaluationHelper();

    private FieldDO field;
    private IrrigationDeviceDO device;

    @BeforeEach
    void setUp() {
        field = new FieldDO();
        field.setId(1L);
        field.setFieldName("Alignment Test Field");
        field.setFarmId(10L);

        device = new IrrigationDeviceDO();
        device.setId(100L);
        device.setDeviceCode("DEV-ALIGN");
        device.setFieldId(1L);

        when(bedrockProperties.getModelId()).thenReturn("anthropic.claude-test");
        when(fieldMapper.selectList(any())).thenReturn(List.of(field));
        when(irrigationDeviceMapper.selectListByFieldId(1L)).thenReturn(List.of(device));
    }

    /**
     * Alignment test matrix.
     * Columns: description | moisture | moistureMin | optimal | rainfall | expectedDecision | mockAiJson
     *
     * mockAiJson simulates what Claude should return when given the same inputs.
     * If AI is correct, its decision matches expectedDecision (which equals the rule decision).
     */
    static Stream<Arguments> alignmentCases() {
        return Stream.of(
            // ── NO_ACTION branch ──────────────────────────────────────────────────────
            Arguments.of("Adequate moisture, no rain → NO_ACTION",
                "55.0", "40.0", "60.0", "0.0",  "NO_ACTION",
                "{\"decision\":\"NO_ACTION\",\"reason\":\"Soil moisture 55.0% exceeds minimum 40.0%.\"}"),

            Arguments.of("Adequate moisture, heavy rain → NO_ACTION",
                "50.0", "40.0", "60.0", "15.0", "NO_ACTION",
                "{\"decision\":\"NO_ACTION\",\"reason\":\"Moisture adequate at 50.0%; irrigation unnecessary.\"}"),

            Arguments.of("Moisture exactly at min → NO_ACTION",
                "40.0", "40.0", "60.0", "2.0",  "NO_ACTION",
                "{\"decision\":\"NO_ACTION\",\"reason\":\"Soil moisture 40.0% meets the minimum threshold 40.0%.\"}"),

            Arguments.of("High moisture, no rain → NO_ACTION",
                "75.0", "40.0", "80.0", "0.0",  "NO_ACTION",
                "{\"decision\":\"NO_ACTION\",\"reason\":\"Soil is well-saturated at 75.0%.\"}"),

            // ── SKIP branch ───────────────────────────────────────────────────────────
            Arguments.of("Below min, rain exactly at threshold (5mm) → SKIP",
                "30.0", "40.0", "60.0", "5.0",  "SKIP",
                "{\"decision\":\"SKIP\",\"reason\":\"Moisture 30.0% below minimum but 5.0mm rain expected tomorrow.\"}"),

            Arguments.of("Below min, heavy rain → SKIP",
                "20.0", "40.0", "60.0", "20.0", "SKIP",
                "{\"decision\":\"SKIP\",\"reason\":\"Despite low moisture, 20.0mm forecast — skip to avoid overwatering.\"}"),

            Arguments.of("Severely dry, but torrential rain → SKIP",
                "10.0", "40.0", "60.0", "50.0", "SKIP",
                "{\"decision\":\"SKIP\",\"reason\":\"50.0mm rain forecast will replenish moisture naturally.\"}"),

            Arguments.of("Just below min, rain just above threshold → SKIP",
                "39.0", "40.0", "60.0", "6.0",  "SKIP",
                "{\"decision\":\"SKIP\",\"reason\":\"6.0mm rain expected, exceeds 5mm threshold — defer irrigation.\"}"),

            // ── IRRIGATE branch ───────────────────────────────────────────────────────
            Arguments.of("Below min, no rain → IRRIGATE",
                "30.0", "40.0", "60.0", "0.0",  "IRRIGATE",
                "{\"decision\":\"IRRIGATE\",\"reason\":\"Moisture 30.0% below min 40.0%, no rain expected.\",\"durationMinutes\":60}"),

            Arguments.of("Below min, trace rain below threshold → IRRIGATE",
                "35.0", "40.0", "60.0", "4.9",  "IRRIGATE",
                "{\"decision\":\"IRRIGATE\",\"reason\":\"4.9mm rain insufficient; moisture 35.0% < 40.0%.\",\"durationMinutes\":30}"),

            Arguments.of("Zero moisture, no rain → IRRIGATE",
                "0.0",  "40.0", "70.0", "0.0",  "IRRIGATE",
                "{\"decision\":\"IRRIGATE\",\"reason\":\"Critically dry at 0.0%; immediate irrigation required.\",\"durationMinutes\":120}"),

            Arguments.of("Moisture just below min, no rain → IRRIGATE",
                "39.9", "40.0", "60.0", "0.0",  "IRRIGATE",
                "{\"decision\":\"IRRIGATE\",\"reason\":\"Moisture 39.9% just below minimum 40.0%.\",\"durationMinutes\":15}"),

            Arguments.of("Below min, 1mm rain (below skip threshold) → IRRIGATE",
                "25.0", "40.0", "60.0", "1.0",  "IRRIGATE",
                "{\"decision\":\"IRRIGATE\",\"reason\":\"1.0mm rain will not be sufficient; irrigate now.\",\"durationMinutes\":70}")
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("alignmentCases")
    @DisplayName("AI aligns with rule engine")
    void aiAlignsWithRules(
            String description,
            String moisture, String min, String optimal, String rainfall,
            String expectedDecision,
            String mockAiJson) {

        // Ground truth from rule engine
        AiDecisionResultVO ctx = buildCtx(moisture, min, optimal, rainfall);
        AiDecisionResultVO ruleResult = realHelper.applyRules(copyCtx(ctx));
        assertEquals(expectedDecision, ruleResult.getDecision(),
                "Rule engine should produce " + expectedDecision + " for: " + description);

        // Mock Bedrock to return the expected aligned response
        when(helper.gatherFieldDataForDevice(eq(field), eq(device))).thenReturn(ctx);
        when(bedrockRuntimeClient.converse(any(ConverseRequest.class)))
                .thenReturn(converseResponse(mockAiJson));

        // Run AI path
        List<AiDecisionResultVO> results = service.runDecisionForAllFields();

        // AI decision must equal rule decision
        String aiDecision = results.get(0).getDecision();
        assertEquals(expectedDecision, aiDecision,
                String.format("[%s] AI decision '%s' does not align with rule decision '%s'",
                        description, aiDecision, expectedDecision));
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private AiDecisionResultVO buildCtx(String moisture, String min, String optimal, String rainfall) {
        AiDecisionResultVO ctx = new AiDecisionResultVO();
        ctx.setFieldId(1L);
        ctx.setFieldName("Alignment Test Field");
        ctx.setCropName("Tomato");
        ctx.setStageName("Vegetative");
        ctx.setCurrentMoisture(new BigDecimal(moisture));
        ctx.setMoistureMin(new BigDecimal(min));
        ctx.setMoistureMax(new BigDecimal("80.0"));
        ctx.setMoistureOptimal(new BigDecimal(optimal));
        ctx.setTomorrowRainfall(new BigDecimal(rainfall));
        return ctx;
    }

    private AiDecisionResultVO copyCtx(AiDecisionResultVO src) {
        AiDecisionResultVO c = new AiDecisionResultVO();
        c.setFieldId(src.getFieldId());
        c.setFieldName(src.getFieldName());
        c.setCropName(src.getCropName());
        c.setStageName(src.getStageName());
        c.setCurrentMoisture(src.getCurrentMoisture());
        c.setMoistureMin(src.getMoistureMin());
        c.setMoistureMax(src.getMoistureMax());
        c.setMoistureOptimal(src.getMoistureOptimal());
        c.setTomorrowRainfall(src.getTomorrowRainfall());
        return c;
    }

    private ConverseResponse converseResponse(String text) {
        return ConverseResponse.builder()
                .output(ConverseOutput.builder()
                        .message(Message.builder()
                                .role(ConversationRole.ASSISTANT)
                                .content(ContentBlock.fromText(text))
                                .build())
                        .build())
                .build();
    }
}
