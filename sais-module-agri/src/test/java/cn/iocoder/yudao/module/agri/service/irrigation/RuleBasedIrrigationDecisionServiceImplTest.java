package cn.iocoder.yudao.module.agri.service.irrigation;

import cn.iocoder.yudao.module.agri.controller.admin.irrigation.vo.AiDecisionResultVO;
import cn.iocoder.yudao.module.agri.dal.dataobject.field.FieldDO;
import cn.iocoder.yudao.module.agri.dal.dataobject.irrigation.IrrigationDeviceDO;
import cn.iocoder.yudao.module.agri.dal.mysql.field.FieldMapper;
import cn.iocoder.yudao.module.agri.dal.mysql.irrigation.IrrigationDeviceMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RuleBasedIrrigationDecisionServiceImplTest {

    @Mock
    private FieldMapper fieldMapper;

    @Mock
    private IrrigationDeviceMapper irrigationDeviceMapper;

    @Mock
    private IrrigationEvaluationHelper helper;

    @InjectMocks
    private RuleBasedIrrigationDecisionServiceImpl service;

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

    // ── Test 1 ───────────────────────────────────────────────────────────────

    @Test
    void noOngoingFields_returnsEmptyList() {
        when(fieldMapper.selectList(any())).thenReturn(List.of());

        List<AiDecisionResultVO> results = service.runDecisionForAllFields();

        assertTrue(results.isEmpty());
    }

    // ── Test 2 ───────────────────────────────────────────────────────────────

    @Test
    void field_withNoData_returnsNoDataResult() {
        when(fieldMapper.selectList(any())).thenReturn(List.of(field));
        when(irrigationDeviceMapper.selectListByFieldId(1L)).thenReturn(List.of(device));
        AiDecisionResultVO noData = new AiDecisionResultVO();
        noData.setDecision("NO_DATA");
        noData.setReason("No active crop plan found for this field.");
        when(helper.gatherFieldDataForDevice(eq(field), eq(device))).thenReturn(noData);

        List<AiDecisionResultVO> results = service.runDecisionForAllFields();

        assertEquals(1, results.size());
        assertEquals("NO_DATA", results.get(0).getDecision());
        verify(helper, never()).applyRules(any());
        verify(helper, never()).activateDevice(any(), any(), any());
    }

    // ── Test 3 ───────────────────────────────────────────────────────────────

    @Test
    void field_withAdequateMoisture_returnsNoAction() {
        when(fieldMapper.selectList(any())).thenReturn(List.of(field));
        when(irrigationDeviceMapper.selectListByFieldId(1L)).thenReturn(List.of(device));
        when(helper.gatherFieldDataForDevice(eq(field), eq(device))).thenReturn(populatedCtx());
        AiDecisionResultVO noAction = populatedCtx();
        noAction.setDecision("NO_ACTION");
        noAction.setReason("Soil moisture is within range.");
        when(helper.applyRules(any())).thenReturn(noAction);

        List<AiDecisionResultVO> results = service.runDecisionForAllFields();

        assertEquals("NO_ACTION", results.get(0).getDecision());
        verify(helper, never()).activateDevice(any(), any(), any());
    }

    // ── Test 4 ───────────────────────────────────────────────────────────────

    @Test
    void field_withLowMoistureAndRain_returnsSkip() {
        when(fieldMapper.selectList(any())).thenReturn(List.of(field));
        when(irrigationDeviceMapper.selectListByFieldId(1L)).thenReturn(List.of(device));
        when(helper.gatherFieldDataForDevice(eq(field), eq(device))).thenReturn(populatedCtx());
        AiDecisionResultVO skip = populatedCtx();
        skip.setDecision("SKIP");
        skip.setReason("Rain expected tomorrow.");
        when(helper.applyRules(any())).thenReturn(skip);

        List<AiDecisionResultVO> results = service.runDecisionForAllFields();

        assertEquals("SKIP", results.get(0).getDecision());
        verify(helper, never()).activateDevice(any(), any(), any());
    }

    // ── Test 5 ───────────────────────────────────────────────────────────────

    @Test
    void field_withLowMoistureNoRain_returnsIrrigateAndActivatesDevice() {
        when(fieldMapper.selectList(any())).thenReturn(List.of(field));
        when(irrigationDeviceMapper.selectListByFieldId(1L)).thenReturn(List.of(device));
        when(helper.gatherFieldDataForDevice(eq(field), eq(device))).thenReturn(populatedCtx());
        AiDecisionResultVO irrigate = populatedCtx();
        irrigate.setDecision("IRRIGATE");
        irrigate.setReason("Soil moisture is too low.");
        when(helper.applyRules(any())).thenReturn(irrigate);

        List<AiDecisionResultVO> results = service.runDecisionForAllFields();

        assertEquals("IRRIGATE", results.get(0).getDecision());
        verify(helper).activateDevice(eq(field), eq(device), any());
    }

    // ── Test 6 ───────────────────────────────────────────────────────────────

    @Test
    void field_whenHelperThrows_returnsErrorResult() {
        when(fieldMapper.selectList(any())).thenReturn(List.of(field));
        when(irrigationDeviceMapper.selectListByFieldId(1L)).thenReturn(List.of(device));
        doThrow(new RuntimeException("db error")).when(helper).gatherFieldDataForDevice(any(), any());

        List<AiDecisionResultVO> results = service.runDecisionForAllFields();

        assertEquals(1, results.size());
        assertEquals("ERROR", results.get(0).getDecision());
        assertTrue(results.get(0).getReason().contains("db error"));
        assertFalse(results.isEmpty(), "Should return error result, not propagate exception");
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    /** Builds a fully populated context VO (simulates successful gatherFieldDataForDevice). */
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

}
