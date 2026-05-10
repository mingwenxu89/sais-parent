package cn.iocoder.yudao.module.agri.service.irrigation;

import cn.iocoder.yudao.module.agri.controller.admin.irrigation.vo.AiDecisionResultVO;
import cn.iocoder.yudao.module.agri.controller.admin.irrigation.vo.DecisionComparisonVO;
import cn.iocoder.yudao.module.agri.dal.dataobject.field.FieldDO;
import cn.iocoder.yudao.module.agri.dal.dataobject.irrigation.IrrigationDeviceDO;
import cn.iocoder.yudao.module.agri.dal.mysql.field.FieldMapper;
import cn.iocoder.yudao.module.agri.dal.mysql.irrigation.IrrigationDeviceMapper;
import cn.iocoder.yudao.module.agri.framework.bedrock.AwsBedrockProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;
import software.amazon.awssdk.services.bedrockruntime.model.ContentBlock;
import software.amazon.awssdk.services.bedrockruntime.model.ConversationRole;
import software.amazon.awssdk.services.bedrockruntime.model.ConverseOutput;
import software.amazon.awssdk.services.bedrockruntime.model.ConverseRequest;
import software.amazon.awssdk.services.bedrockruntime.model.ConverseResponse;
import software.amazon.awssdk.services.bedrockruntime.model.Message;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DecisionComparisonServiceTest {

    @Mock
    private FieldMapper fieldMapper;
    @Mock
    private IrrigationDeviceMapper irrigationDeviceMapper;
    @Mock
    private IrrigationEvaluationHelper helper;
    @Mock
    private BedrockRuntimeClient bedrockRuntimeClient;
    @Mock
    private AwsBedrockProperties bedrockProperties;

    @InjectMocks
    private DecisionComparisonService service;

    private FieldDO field;
    private IrrigationDeviceDO device;

    @BeforeEach
    void setUp() {
        field = new FieldDO();
        field.setId(1L);
        field.setFieldName("Field A");
        field.setFarmId(10L);

        device = new IrrigationDeviceDO();
        device.setId(100L);
        device.setDeviceCode("DEV-001");
        device.setFieldId(1L);

        // lenient: not all tests reach buildPrompt(); avoids UnnecessaryStubbingException.
        lenient().when(bedrockProperties.getModelId()).thenReturn("anthropic.claude-test");
    }

    // ── A. compareAll top-level structure ───────────────────────────────────

    @Test
    void compareAll_noOngoingFields_returnsEmptyList() {
        when(fieldMapper.selectList(any())).thenReturn(List.of());

        List<DecisionComparisonVO> results = service.compareAll();

        assertTrue(results.isEmpty());
        verifyNoInteractions(helper, irrigationDeviceMapper, bedrockRuntimeClient);
    }

    @Test
    void compareAll_fieldWithoutDevices_returnsNoDeviceRow() {
        when(fieldMapper.selectList(any())).thenReturn(List.of(field));
        when(irrigationDeviceMapper.selectListByFieldId(1L)).thenReturn(List.of());

        List<DecisionComparisonVO> results = service.compareAll();

        assertEquals(1, results.size());
        DecisionComparisonVO row = results.get(0);
        assertEquals(1L, row.getFieldId());
        assertEquals("NO_DATA", row.getRuleDecision());
        assertEquals("NO_DATA", row.getAiDecision());
        assertTrue(row.getRuleReason().contains("No irrigation devices"));
        assertTrue(row.getAligned());
        verifyNoInteractions(helper, bedrockRuntimeClient);
    }

    @Test
    void compareAll_multipleFieldsAndDevices_returnsOneRowPerDevice() {
        FieldDO fieldB = new FieldDO();
        fieldB.setId(2L);
        fieldB.setFieldName("Field B");
        IrrigationDeviceDO deviceA2 = new IrrigationDeviceDO();
        deviceA2.setId(101L);
        deviceA2.setFieldId(1L);
        IrrigationDeviceDO deviceB1 = new IrrigationDeviceDO();
        deviceB1.setId(200L);
        deviceB1.setFieldId(2L);

        when(fieldMapper.selectList(any())).thenReturn(List.of(field, fieldB));
        when(irrigationDeviceMapper.selectListByFieldId(1L)).thenReturn(List.of(device, deviceA2));
        when(irrigationDeviceMapper.selectListByFieldId(2L)).thenReturn(List.of(deviceB1));
        AiDecisionResultVO noData = new AiDecisionResultVO();
        noData.setDecision("NO_DATA");
        noData.setReason("no sensor data");
        when(helper.gatherFieldDataForDevice(any(), any())).thenReturn(noData);

        List<DecisionComparisonVO> results = service.compareAll();

        assertEquals(3, results.size());
    }

    // ── B. compareForDevice decision branches ──────────────────────────────

    @Test
    void compareForDevice_noData_earlyExits_andAligned() {
        AiDecisionResultVO noData = new AiDecisionResultVO();
        noData.setDecision("NO_DATA");
        noData.setReason("missing soil moisture");
        noData.setCropName("Tomato");
        noData.setStageName("Flowering");

        when(fieldMapper.selectList(any())).thenReturn(List.of(field));
        when(irrigationDeviceMapper.selectListByFieldId(1L)).thenReturn(List.of(device));
        when(helper.gatherFieldDataForDevice(eq(field), eq(device))).thenReturn(noData);

        DecisionComparisonVO row = service.compareAll().get(0);

        assertEquals("NO_DATA", row.getRuleDecision());
        assertEquals("NO_DATA", row.getAiDecision());
        assertEquals("missing soil moisture", row.getRuleReason());
        assertEquals("missing soil moisture", row.getAiReason());
        assertTrue(row.getAligned());
        verifyNoInteractions(bedrockRuntimeClient);
    }

    @Test
    void compareForDevice_bedrockUnavailable_setsUnavailable_andAlignedNull() {
        ReflectionTestUtils.setField(service, "bedrockRuntimeClient", null);

        when(fieldMapper.selectList(any())).thenReturn(List.of(field));
        when(irrigationDeviceMapper.selectListByFieldId(1L)).thenReturn(List.of(device));
        when(helper.gatherFieldDataForDevice(eq(field), eq(device))).thenReturn(populatedCtx());
        when(helper.applyRules(any())).thenReturn(ruleResult("IRRIGATE", "Soil too dry."));

        DecisionComparisonVO row = service.compareAll().get(0);

        assertEquals("IRRIGATE", row.getRuleDecision());
        assertEquals("UNAVAILABLE", row.getAiDecision());
        assertEquals("AWS Bedrock not configured.", row.getAiReason());
        assertNull(row.getAligned());
        assertFalse(row.getAiAvailable());
    }

    @Test
    void compareForDevice_bedrockThrows_setsError_andAlignedFalse() {
        when(fieldMapper.selectList(any())).thenReturn(List.of(field));
        when(irrigationDeviceMapper.selectListByFieldId(1L)).thenReturn(List.of(device));
        when(helper.gatherFieldDataForDevice(eq(field), eq(device))).thenReturn(populatedCtx());
        when(helper.applyRules(any())).thenReturn(ruleResult("IRRIGATE", "Soil too dry."));
        when(bedrockRuntimeClient.converse(any(ConverseRequest.class)))
                .thenThrow(new RuntimeException("connection timeout"));

        DecisionComparisonVO row = service.compareAll().get(0);

        assertEquals("ERROR", row.getAiDecision());
        assertTrue(row.getAiReason().startsWith("Bedrock error:"));
        assertTrue(row.getAiReason().contains("connection timeout"));
        assertFalse(row.getAligned());
    }

    // ── C. fillAiResult JSON parsing branches ──────────────────────────────

    @Test
    void fillAiResult_validIrrigateJson_setsDecision_andDuration_andAlignment() {
        when(fieldMapper.selectList(any())).thenReturn(List.of(field));
        when(irrigationDeviceMapper.selectListByFieldId(1L)).thenReturn(List.of(device));
        when(helper.gatherFieldDataForDevice(eq(field), eq(device))).thenReturn(populatedCtx());
        when(helper.applyRules(any())).thenReturn(ruleResult("IRRIGATE", "Soil too dry."));
        when(bedrockRuntimeClient.converse(any(ConverseRequest.class))).thenReturn(
                converseResponse("{\"decision\":\"IRRIGATE\",\"reason\":\"low moisture\",\"durationMinutes\":45}"));

        DecisionComparisonVO row = service.compareAll().get(0);

        assertEquals("IRRIGATE", row.getRuleDecision());
        assertEquals("IRRIGATE", row.getAiDecision());
        assertEquals(45, row.getAiDurationMinutes());
        assertEquals("low moisture", row.getAiReason());
        assertTrue(row.getAligned());
    }

    @Test
    void fillAiResult_validSkipJson_andRuleIrrigate_setsAlignedFalse() {
        when(fieldMapper.selectList(any())).thenReturn(List.of(field));
        when(irrigationDeviceMapper.selectListByFieldId(1L)).thenReturn(List.of(device));
        when(helper.gatherFieldDataForDevice(eq(field), eq(device))).thenReturn(populatedCtx());
        when(helper.applyRules(any())).thenReturn(ruleResult("IRRIGATE", "Soil too dry."));
        when(bedrockRuntimeClient.converse(any(ConverseRequest.class))).thenReturn(
                converseResponse("{\"decision\":\"SKIP\",\"reason\":\"rain coming\"}"));

        DecisionComparisonVO row = service.compareAll().get(0);

        assertEquals("IRRIGATE", row.getRuleDecision());
        assertEquals("SKIP", row.getAiDecision());
        assertEquals("rain coming", row.getAiReason());
        assertFalse(row.getAligned());
    }

    @Test
    void fillAiResult_responseWithoutJson_setsParseError() {
        when(fieldMapper.selectList(any())).thenReturn(List.of(field));
        when(irrigationDeviceMapper.selectListByFieldId(1L)).thenReturn(List.of(device));
        when(helper.gatherFieldDataForDevice(eq(field), eq(device))).thenReturn(populatedCtx());
        when(helper.applyRules(any())).thenReturn(ruleResult("IRRIGATE", "Soil too dry."));
        when(bedrockRuntimeClient.converse(any(ConverseRequest.class))).thenReturn(
                converseResponse("sorry, no idea"));

        DecisionComparisonVO row = service.compareAll().get(0);

        assertEquals("PARSE_ERROR", row.getAiDecision());
        assertTrue(row.getAiReason().contains("No JSON found"));
        // rule != "PARSE_ERROR" → not aligned
        assertFalse(row.getAligned());
    }

    @Test
    void fillAiResult_unrecognisedDecisionValue_setsParseError() {
        when(fieldMapper.selectList(any())).thenReturn(List.of(field));
        when(irrigationDeviceMapper.selectListByFieldId(1L)).thenReturn(List.of(device));
        when(helper.gatherFieldDataForDevice(eq(field), eq(device))).thenReturn(populatedCtx());
        when(helper.applyRules(any())).thenReturn(ruleResult("IRRIGATE", "Soil too dry."));
        when(bedrockRuntimeClient.converse(any(ConverseRequest.class))).thenReturn(
                converseResponse("{\"decision\":\"MAYBE\",\"reason\":\"unsure\"}"));

        DecisionComparisonVO row = service.compareAll().get(0);

        assertEquals("PARSE_ERROR", row.getAiDecision());
        assertTrue(row.getAiReason().contains("Unrecognised decision value"));
    }

    @Test
    void fillAiResult_malformedJson_setsParseError() {
        when(fieldMapper.selectList(any())).thenReturn(List.of(field));
        when(irrigationDeviceMapper.selectListByFieldId(1L)).thenReturn(List.of(device));
        when(helper.gatherFieldDataForDevice(eq(field), eq(device))).thenReturn(populatedCtx());
        when(helper.applyRules(any())).thenReturn(ruleResult("IRRIGATE", "Soil too dry."));
        // start { and end } are present so extractJson returns substring,
        // but the inner content is not valid JSON → readTree throws.
        when(bedrockRuntimeClient.converse(any(ConverseRequest.class))).thenReturn(
                converseResponse("{decision: IRRIGATE without quotes}"));

        DecisionComparisonVO row = service.compareAll().get(0);

        assertEquals("PARSE_ERROR", row.getAiDecision());
        assertTrue(row.getAiReason().startsWith("JSON parse error"));
    }

    // ── Helpers ─────────────────────────────────────────────────────────────

    private AiDecisionResultVO populatedCtx() {
        AiDecisionResultVO ctx = new AiDecisionResultVO();
        ctx.setFieldId(1L);
        ctx.setFieldName("Field A");
        ctx.setCropName("Tomato");
        ctx.setStageName("Flowering");
        ctx.setCurrentMoisture(new BigDecimal("35.0"));
        ctx.setMoistureMin(new BigDecimal("40.0"));
        ctx.setMoistureMax(new BigDecimal("70.0"));
        ctx.setMoistureOptimal(new BigDecimal("55.0"));
        ctx.setTomorrowRainfall(new BigDecimal("1.0"));
        return ctx;
    }

    private AiDecisionResultVO ruleResult(String decision, String reason) {
        AiDecisionResultVO r = populatedCtx();
        r.setDecision(decision);
        r.setReason(reason);
        return r;
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
