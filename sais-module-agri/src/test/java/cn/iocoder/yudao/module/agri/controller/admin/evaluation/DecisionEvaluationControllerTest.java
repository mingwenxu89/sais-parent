package cn.iocoder.yudao.module.agri.controller.admin.evaluation;

import cn.iocoder.yudao.framework.common.exception.enums.GlobalErrorCodeConstants;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.agri.controller.admin.irrigation.vo.DecisionComparisonVO;
import cn.iocoder.yudao.module.agri.service.irrigation.DecisionComparisonService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DecisionEvaluationControllerTest {

    @Mock
    private DecisionComparisonService decisionComparisonService;

    @InjectMocks
    private DecisionEvaluationController controller;

    @Test
    void compareDecision_returnsServiceResult_wrappedInCommonResult() {
        DecisionComparisonVO row = new DecisionComparisonVO();
        row.setFieldId(1L);
        row.setFieldName("Field A");
        List<DecisionComparisonVO> serviceResult = List.of(row);
        when(decisionComparisonService.compareAll()).thenReturn(serviceResult);

        CommonResult<List<DecisionComparisonVO>> result = controller.compareDecision();

        assertTrue(result.isSuccess());
        assertEquals(GlobalErrorCodeConstants.SUCCESS.getCode(), result.getCode());
        assertSame(serviceResult, result.getData());
    }

    @Test
    void compareDecision_emptyList_returnsSuccessfulEmptyResult() {
        when(decisionComparisonService.compareAll()).thenReturn(List.of());

        CommonResult<List<DecisionComparisonVO>> result = controller.compareDecision();

        assertTrue(result.isSuccess());
        assertTrue(result.getData().isEmpty());
    }

    @Test
    void compareDecision_serviceThrows_propagatesException() {
        when(decisionComparisonService.compareAll()).thenThrow(new RuntimeException("bedrock down"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> controller.compareDecision());
        assertEquals("bedrock down", ex.getMessage());
    }
}
