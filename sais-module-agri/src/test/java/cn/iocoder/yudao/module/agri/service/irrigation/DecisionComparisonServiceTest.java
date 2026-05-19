package cn.iocoder.yudao.module.agri.service.irrigation;

import cn.iocoder.yudao.module.agri.controller.admin.irrigation.vo.AiDecisionResultVO;
import cn.iocoder.yudao.module.agri.controller.admin.irrigation.vo.DecisionComparisonVO;
import cn.iocoder.yudao.module.agri.dal.dataobject.evaluation.DecisionEvaluationRecordDO;
import cn.iocoder.yudao.module.agri.dal.dataobject.field.FieldDO;
import cn.iocoder.yudao.module.agri.dal.dataobject.irrigation.IrrigationDeviceDO;
import cn.iocoder.yudao.module.agri.dal.mysql.crop.CropPlanMapper;
import cn.iocoder.yudao.module.agri.dal.mysql.evaluation.DecisionEvaluationRecordMapper;
import cn.iocoder.yudao.module.agri.dal.mysql.field.FieldMapper;
import cn.iocoder.yudao.module.agri.dal.mysql.irrigation.IrrigationDeviceMapper;
import cn.iocoder.yudao.module.agri.framework.deepseek.DeepSeekClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DecisionComparisonServiceTest {

    @Mock
    private FieldMapper fieldMapper;
    @Mock
    private CropPlanMapper cropPlanMapper;
    @Mock
    private IrrigationDeviceMapper irrigationDeviceMapper;
    @Mock
    private IrrigationEvaluationHelper helper;
    @Mock
    private DecisionEvaluationRecordMapper decisionEvaluationRecordMapper;
    @Mock
    private DeepSeekClient deepSeekClient;

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
    }

    @Test
    void compareAll_noCurrentCropPlanFields_returnsEmptyList() {
        when(cropPlanMapper.selectCurrentFieldIds()).thenReturn(List.of());

        List<DecisionComparisonVO> results = service.compareAll();

        assertTrue(results.isEmpty());
        verifyNoInteractions(helper, irrigationDeviceMapper);
    }

    @Test
    void compareAll_fieldWithoutDevices_returnsNoDeviceRow() {
        mockCurrentField();
        when(irrigationDeviceMapper.selectListByFieldId(1L)).thenReturn(List.of());
        when(deepSeekClient.isConfigured()).thenReturn(true);

        List<DecisionComparisonVO> results = service.compareAll();

        assertEquals(1, results.size());
        DecisionComparisonVO row = results.get(0);
        assertEquals(1L, row.getFieldId());
        assertEquals("NO_DATA", row.getRuleDecision());
        assertEquals("NO_DATA", row.getAiDecision());
        assertTrue(row.getRuleReason().contains("No irrigation devices"));
        assertTrue(row.getAligned());
        assertTrue(row.getAiAvailable());
        verifyNoInteractions(helper);
        verify(decisionEvaluationRecordMapper).insert(any(DecisionEvaluationRecordDO.class));
    }

    @Test
    void compareForDevice_noData_earlyExits_andAligned() {
        mockCurrentFieldWithDevice();
        AiDecisionResultVO noData = new AiDecisionResultVO();
        noData.setDecision("NO_DATA");
        noData.setReason("missing soil moisture");
        noData.setCropName("Tomato");
        noData.setStageName("Flowering");
        when(helper.gatherFieldDataForDevice(eq(field), eq(device))).thenReturn(noData);
        when(deepSeekClient.isConfigured()).thenReturn(true);

        DecisionComparisonVO row = service.compareAll().get(0);

        assertEquals("NO_DATA", row.getRuleDecision());
        assertEquals("NO_DATA", row.getAiDecision());
        assertEquals("missing soil moisture", row.getRuleReason());
        assertEquals("missing soil moisture", row.getAiReason());
        assertTrue(row.getAligned());
        verify(decisionEvaluationRecordMapper).insert(any(DecisionEvaluationRecordDO.class));
    }

    @Test
    void compareForDevice_deepSeekUnavailable_setsUnavailable_andAlignedNull() {
        ReflectionTestUtils.setField(service, "deepSeekClient", null);
        mockCurrentFieldWithDevice();
        when(helper.gatherFieldDataForDevice(eq(field), eq(device))).thenReturn(populatedCtx());
        when(helper.applyRules(any())).thenReturn(ruleResult("IRRIGATE", "Soil too dry."));

        DecisionComparisonVO row = service.compareAll().get(0);

        assertEquals("IRRIGATE", row.getRuleDecision());
        assertEquals("UNAVAILABLE", row.getAiDecision());
        assertEquals("DeepSeek not configured.", row.getAiReason());
        assertNull(row.getAligned());
        assertFalse(row.getAiAvailable());
    }

    @Test
    void compareForDevice_deepSeekThrows_setsError_andAlignedFalse() throws Exception {
        mockCurrentFieldWithDevice();
        when(helper.gatherFieldDataForDevice(eq(field), eq(device))).thenReturn(populatedCtx());
        when(helper.applyRules(any())).thenReturn(ruleResult("IRRIGATE", "Soil too dry."));
        when(deepSeekClient.isConfigured()).thenReturn(true);
        when(deepSeekClient.complete(any())).thenThrow(new RuntimeException("connection timeout"));

        DecisionComparisonVO row = service.compareAll().get(0);

        assertEquals("ERROR", row.getAiDecision());
        assertTrue(row.getAiReason().startsWith("DeepSeek error:"));
        assertTrue(row.getAiReason().contains("connection timeout"));
        assertFalse(row.getAligned());
    }

    @Test
    void fillAiResult_validIrrigateJson_setsDecisionDurationAndAlignment() throws Exception {
        mockCurrentFieldWithDevice();
        when(helper.gatherFieldDataForDevice(eq(field), eq(device))).thenReturn(populatedCtx());
        when(helper.applyRules(any())).thenReturn(ruleResult("IRRIGATE", "Soil too dry."));
        when(deepSeekClient.isConfigured()).thenReturn(true);
        when(deepSeekClient.complete(any()))
                .thenReturn("{\"decision\":\"IRRIGATE\",\"reason\":\"low moisture\",\"durationMinutes\":45}");

        DecisionComparisonVO row = service.compareAll().get(0);

        assertEquals("IRRIGATE", row.getRuleDecision());
        assertEquals("IRRIGATE", row.getAiDecision());
        assertEquals(45, row.getAiDurationMinutes());
        assertEquals("low moisture", row.getAiReason());
        assertTrue(row.getAligned());
    }

    @Test
    void fillAiResult_responseWithoutJson_setsParseError() throws Exception {
        mockCurrentFieldWithDevice();
        when(helper.gatherFieldDataForDevice(eq(field), eq(device))).thenReturn(populatedCtx());
        when(helper.applyRules(any())).thenReturn(ruleResult("IRRIGATE", "Soil too dry."));
        when(deepSeekClient.isConfigured()).thenReturn(true);
        when(deepSeekClient.complete(any())).thenReturn("sorry, no idea");

        DecisionComparisonVO row = service.compareAll().get(0);

        assertEquals("PARSE_ERROR", row.getAiDecision());
        assertTrue(row.getAiReason().contains("No JSON found"));
        assertFalse(row.getAligned());
    }

    private void mockCurrentField() {
        when(cropPlanMapper.selectCurrentFieldIds()).thenReturn(List.of(1L));
        when(fieldMapper.selectBatchIds(any())).thenReturn(List.of(field));
    }

    private void mockCurrentFieldWithDevice() {
        mockCurrentField();
        when(irrigationDeviceMapper.selectListByFieldId(1L)).thenReturn(List.of(device));
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
