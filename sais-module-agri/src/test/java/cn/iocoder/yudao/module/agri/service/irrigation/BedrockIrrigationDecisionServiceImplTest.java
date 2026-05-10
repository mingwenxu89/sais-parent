package cn.iocoder.yudao.module.agri.service.irrigation;

import cn.iocoder.yudao.module.agri.controller.admin.irrigation.vo.AiDecisionResultVO;
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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BedrockIrrigationDecisionServiceImplTest {

    @Mock
    private BedrockRuntimeClient bedrockRuntimeClient;
    @Mock
    private AwsBedrockProperties bedrockProperties;
    @Mock
    private FieldMapper fieldMapper;
    @Mock
    private IrrigationDeviceMapper irrigationDeviceMapper;
    @Mock
    private IrrigationEvaluationHelper helper;

    @InjectMocks
    private BedrockIrrigationDecisionServiceImpl service;

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

        when(bedrockProperties.getModelId()).thenReturn("anthropic.claude-test");
    }

    // ── Test 1 ───────────────────────────────────────────────────────────────

    @Test
    void noOngoingFields_returnsEmptyList() {
        when(fieldMapper.selectList(any())).thenReturn(List.of());

        assertTrue(service.runDecisionForAllFields().isEmpty());
        verifyNoInteractions(bedrockRuntimeClient);
    }

    // ── Test 2 ───────────────────────────────────────────────────────────────

    @Test
    void field_withNoData_returnsNoDataResult_withoutCallingBedrock() {
        when(fieldMapper.selectList(any())).thenReturn(List.of(field));
        when(irrigationDeviceMapper.selectListByFieldId(1L)).thenReturn(List.of(device));
        AiDecisionResultVO noData = new AiDecisionResultVO();
        noData.setDecision("NO_DATA");
        when(helper.gatherFieldDataForDevice(eq(field), eq(device))).thenReturn(noData);

        List<AiDecisionResultVO> results = service.runDecisionForAllFields();

        assertEquals("NO_DATA", results.get(0).getDecision());
        verifyNoInteractions(bedrockRuntimeClient);
    }

    // ── Test 3 ───────────────────────────────────────────────────────────────

    @Test
    void field_bedrockReturnsIrrigate_activatesDevice() {
        when(fieldMapper.selectList(any())).thenReturn(List.of(field));
        when(irrigationDeviceMapper.selectListByFieldId(1L)).thenReturn(List.of(device));
        when(helper.gatherFieldDataForDevice(eq(field), eq(device))).thenReturn(populatedCtx());
        when(bedrockRuntimeClient.converse(any(ConverseRequest.class))).thenReturn(
                converseResponse("{\"decision\":\"IRRIGATE\",\"reason\":\"Soil is too dry.\",\"durationMinutes\":30}"));

        List<AiDecisionResultVO> results = service.runDecisionForAllFields();

        assertEquals("IRRIGATE", results.get(0).getDecision());
        verify(helper).activateDevice(eq(field), eq(device), any());
    }

    // ── Test 4 ───────────────────────────────────────────────────────────────

    @Test
    void field_bedrockReturnsSkip_doesNotActivateDevice() {
        when(fieldMapper.selectList(any())).thenReturn(List.of(field));
        when(irrigationDeviceMapper.selectListByFieldId(1L)).thenReturn(List.of(device));
        when(helper.gatherFieldDataForDevice(eq(field), eq(device))).thenReturn(populatedCtx());
        when(bedrockRuntimeClient.converse(any(ConverseRequest.class))).thenReturn(
                converseResponse("{\"decision\":\"SKIP\",\"reason\":\"Rain expected tomorrow.\"}"));

        List<AiDecisionResultVO> results = service.runDecisionForAllFields();

        assertEquals("SKIP", results.get(0).getDecision());
        verify(helper, never()).activateDevice(any(), any(), any());
    }

    // ── Test 5 ───────────────────────────────────────────────────────────────

    @Test
    void field_bedrockReturnsNoAction_doesNotActivateDevice() {
        when(fieldMapper.selectList(any())).thenReturn(List.of(field));
        when(irrigationDeviceMapper.selectListByFieldId(1L)).thenReturn(List.of(device));
        when(helper.gatherFieldDataForDevice(eq(field), eq(device))).thenReturn(populatedCtx());
        when(bedrockRuntimeClient.converse(any(ConverseRequest.class))).thenReturn(
                converseResponse("{\"decision\":\"NO_ACTION\",\"reason\":\"Moisture is adequate.\"}"));

        List<AiDecisionResultVO> results = service.runDecisionForAllFields();

        assertEquals("NO_ACTION", results.get(0).getDecision());
        verify(helper, never()).activateDevice(any(), any(), any());
    }

    // ── Test 6 ───────────────────────────────────────────────────────────────

    @Test
    void field_bedrockResponseHasNoJson_fallsBackToRules() {
        when(fieldMapper.selectList(any())).thenReturn(List.of(field));
        when(irrigationDeviceMapper.selectListByFieldId(1L)).thenReturn(List.of(device));
        when(helper.gatherFieldDataForDevice(eq(field), eq(device))).thenReturn(populatedCtx());
        when(bedrockRuntimeClient.converse(any(ConverseRequest.class))).thenReturn(
                converseResponse("I recommend watering the field today."));
        when(helper.applyRules(any())).thenReturn(ruleResult("NO_ACTION", "Rule-based: moisture ok."));

        service.runDecisionForAllFields();

        verify(helper).applyRules(any());
    }

    // ── Test 7 ───────────────────────────────────────────────────────────────

    @Test
    void field_bedrockResponseHasUnknownDecision_fallsBackToRules() {
        when(fieldMapper.selectList(any())).thenReturn(List.of(field));
        when(irrigationDeviceMapper.selectListByFieldId(1L)).thenReturn(List.of(device));
        when(helper.gatherFieldDataForDevice(eq(field), eq(device))).thenReturn(populatedCtx());
        when(bedrockRuntimeClient.converse(any(ConverseRequest.class))).thenReturn(
                converseResponse("{\"decision\":\"WATER_NOW\",\"reason\":\"Custom decision.\"}"));
        when(helper.applyRules(any())).thenReturn(ruleResult("SKIP", "Rule fallback."));

        service.runDecisionForAllFields();

        verify(helper).applyRules(any());
    }

    // ── Test 8 ───────────────────────────────────────────────────────────────

    @Test
    void field_bedrockApiThrows_fallsBackToRulesWithFallbackPrefix() {
        when(fieldMapper.selectList(any())).thenReturn(List.of(field));
        when(irrigationDeviceMapper.selectListByFieldId(1L)).thenReturn(List.of(device));
        when(helper.gatherFieldDataForDevice(eq(field), eq(device))).thenReturn(populatedCtx());
        when(bedrockRuntimeClient.converse(any(ConverseRequest.class))).thenThrow(new RuntimeException("connection timeout"));
        when(helper.applyRules(any())).thenReturn(ruleResult("IRRIGATE", "Soil is too dry."));

        List<AiDecisionResultVO> results = service.runDecisionForAllFields();

        assertEquals("IRRIGATE", results.get(0).getDecision());
        assertTrue(results.get(0).getReason().startsWith("[Fallback]"));
        verify(helper).activateDevice(eq(field), eq(device), any());
    }

    // ── Test 9 ───────────────────────────────────────────────────────────────

    @Test
    void field_bedrockIrrigateWithDuration_appendsDurationToReason() {
        when(fieldMapper.selectList(any())).thenReturn(List.of(field));
        when(irrigationDeviceMapper.selectListByFieldId(1L)).thenReturn(List.of(device));
        when(helper.gatherFieldDataForDevice(eq(field), eq(device))).thenReturn(populatedCtx());
        when(bedrockRuntimeClient.converse(any(ConverseRequest.class))).thenReturn(
                converseResponse("{\"decision\":\"IRRIGATE\",\"reason\":\"Soil is dry.\",\"durationMinutes\":45}"));

        List<AiDecisionResultVO> results = service.runDecisionForAllFields();

        assertTrue(results.get(0).getReason().contains("45 min."));
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

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
