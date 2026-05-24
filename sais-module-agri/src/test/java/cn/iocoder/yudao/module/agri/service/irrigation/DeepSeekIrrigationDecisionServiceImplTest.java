package cn.iocoder.yudao.module.agri.service.irrigation;

import cn.iocoder.yudao.module.agri.controller.admin.irrigation.vo.AiDecisionResultVO;
import cn.iocoder.yudao.module.agri.dal.dataobject.field.FieldDO;
import cn.iocoder.yudao.module.agri.dal.dataobject.irrigation.IrrigationDeviceDO;
import cn.iocoder.yudao.module.agri.dal.mysql.crop.CropPlanMapper;
import cn.iocoder.yudao.module.agri.dal.mysql.field.FieldMapper;
import cn.iocoder.yudao.module.agri.dal.mysql.irrigation.IrrigationDeviceMapper;
import cn.iocoder.yudao.module.agri.framework.deepseek.DeepSeekClient;
import cn.iocoder.yudao.module.agri.framework.deepseek.DeepSeekProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
class DeepSeekIrrigationDecisionServiceImplTest {

    @Mock
    private DeepSeekClient deepSeekClient;
    @Mock
    private DeepSeekProperties deepSeekProperties;
    @Mock
    private FieldMapper fieldMapper;
    @Mock
    private CropPlanMapper cropPlanMapper;
    @Mock
    private IrrigationDeviceMapper irrigationDeviceMapper;
    @Mock
    private IrrigationEvaluationHelper helper;

    @InjectMocks
    private DeepSeekIrrigationDecisionServiceImpl service;

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

        when(deepSeekProperties.getModel()).thenReturn("deepseek-test");
    }

    @Test
    void noCurrentCropPlanFields_returnsEmptyList() {
        when(cropPlanMapper.selectCurrentFieldIds()).thenReturn(List.of());

        assertTrue(service.runDecisionForAllFields().isEmpty());
        verifyNoInteractions(deepSeekClient);
    }

    @Test
    void field_withNoData_returnsNoDataResult_withoutCallingDeepSeek() {
        mockCurrentField();
        when(helper.gatherFieldDataForDevice(eq(field), eq(device))).thenReturn(noDataCtx());

        List<AiDecisionResultVO> results = service.runDecisionForAllFields();

        assertEquals("NO_DATA", results.get(0).getDecision());
        verifyNoInteractions(deepSeekClient);
    }

    @Test
    void field_deepSeekReturnsIrrigate_activatesDevice() throws Exception {
        mockCurrentField();
        when(helper.gatherFieldDataForDevice(eq(field), eq(device))).thenReturn(populatedCtx());
        when(deepSeekClient.complete(any()))
                .thenReturn("{\"decision\":\"IRRIGATE\",\"reason\":\"Soil is too dry.\",\"durationMinutes\":30}");

        List<AiDecisionResultVO> results = service.runDecisionForAllFields();

        assertEquals("IRRIGATE", results.get(0).getDecision());
        verify(helper).activateDevice(eq(field), eq(device), any());
    }

    @Test
    void field_deepSeekReturnsSkip_doesNotActivateDevice() throws Exception {
        mockCurrentField();
        when(helper.gatherFieldDataForDevice(eq(field), eq(device))).thenReturn(populatedCtx());
        when(deepSeekClient.complete(any()))
                .thenReturn("{\"decision\":\"SKIP\",\"reason\":\"Rain expected tomorrow.\"}");

        List<AiDecisionResultVO> results = service.runDecisionForAllFields();

        assertEquals("SKIP", results.get(0).getDecision());
        verify(helper, never()).activateDevice(any(), any(), any());
    }

    @Test
    void field_deepSeekReturnsNoAction_doesNotActivateDevice() throws Exception {
        mockCurrentField();
        when(helper.gatherFieldDataForDevice(eq(field), eq(device))).thenReturn(populatedCtx());
        when(deepSeekClient.complete(any()))
                .thenReturn("{\"decision\":\"NO_ACTION\",\"reason\":\"Moisture is adequate.\"}");

        List<AiDecisionResultVO> results = service.runDecisionForAllFields();

        assertEquals("NO_ACTION", results.get(0).getDecision());
        verify(helper, never()).activateDevice(any(), any(), any());
    }

    @Test
    void field_deepSeekResponseHasNoJson_fallsBackToRules() throws Exception {
        mockCurrentField();
        when(helper.gatherFieldDataForDevice(eq(field), eq(device))).thenReturn(populatedCtx());
        when(deepSeekClient.complete(any())).thenReturn("I recommend watering the field today.");
        when(helper.applyRules(any())).thenReturn(ruleResult("NO_ACTION", "Rule-based: moisture ok."));

        service.runDecisionForAllFields();

        verify(helper).applyRules(any());
    }

    @Test
    void field_deepSeekResponseHasUnknownDecision_fallsBackToRules() throws Exception {
        mockCurrentField();
        when(helper.gatherFieldDataForDevice(eq(field), eq(device))).thenReturn(populatedCtx());
        when(deepSeekClient.complete(any()))
                .thenReturn("{\"decision\":\"WATER_NOW\",\"reason\":\"Custom decision.\"}");
        when(helper.applyRules(any())).thenReturn(ruleResult("SKIP", "Rule fallback."));

        service.runDecisionForAllFields();

        verify(helper).applyRules(any());
    }

    @Test
    void field_deepSeekApiThrows_fallsBackToRulesWithFallbackPrefix() throws Exception {
        mockCurrentField();
        when(helper.gatherFieldDataForDevice(eq(field), eq(device))).thenReturn(populatedCtx());
        when(deepSeekClient.complete(any())).thenThrow(new RuntimeException("connection timeout"));
        when(helper.applyRules(any())).thenReturn(ruleResult("IRRIGATE", "Soil is too dry."));

        List<AiDecisionResultVO> results = service.runDecisionForAllFields();

        assertEquals("IRRIGATE", results.get(0).getDecision());
        assertTrue(results.get(0).getReason().startsWith("[Fallback]"));
        verify(helper).activateDevice(eq(field), eq(device), any());
    }

    @Test
    void field_deepSeekIrrigateWithDuration_appendsDurationToReason() throws Exception {
        mockCurrentField();
        when(helper.gatherFieldDataForDevice(eq(field), eq(device))).thenReturn(populatedCtx());
        when(deepSeekClient.complete(any()))
                .thenReturn("{\"decision\":\"IRRIGATE\",\"reason\":\"Soil is dry.\",\"durationMinutes\":45}");

        List<AiDecisionResultVO> results = service.runDecisionForAllFields();

        assertTrue(results.get(0).getReason().contains("45 min."));
    }

    private void mockCurrentField() {
        when(cropPlanMapper.selectCurrentFieldIds()).thenReturn(List.of(1L));
        when(fieldMapper.selectBatchIds(any())).thenReturn(List.of(field));
        when(irrigationDeviceMapper.selectListByFieldId(1L)).thenReturn(List.of(device));
    }

    private AiDecisionResultVO noDataCtx() {
        AiDecisionResultVO ctx = new AiDecisionResultVO();
        ctx.setDecision("NO_DATA");
        return ctx;
    }

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

}
