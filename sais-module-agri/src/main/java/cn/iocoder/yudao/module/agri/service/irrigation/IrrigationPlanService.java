package cn.iocoder.yudao.module.agri.service.irrigation;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.agri.controller.admin.irrigation.vo.IrrigationPlanPageReqVO;
import cn.iocoder.yudao.module.agri.controller.admin.irrigation.vo.IrrigationPlanRespVO;
import cn.iocoder.yudao.module.agri.controller.admin.irrigation.vo.IrrigationPlanSaveReqVO;

import jakarta.validation.Valid;
import java.time.LocalDateTime;

/**
 * Irrigation Plan Service interface
 */
public interface IrrigationPlanService {

    Long createIrrigationPlan(@Valid IrrigationPlanSaveReqVO createReqVO);

    void updateIrrigationPlan(@Valid IrrigationPlanSaveReqVO updateReqVO);

    void deleteIrrigationPlan(Long id);

    void cancelIrrigationPlan(Long id);

    IrrigationPlanRespVO getIrrigationPlan(Long id);

    PageResult<IrrigationPlanRespVO> getIrrigationPlanPage(IrrigationPlanPageReqVO pageReqVO);

    /**
     * Creates an AI-generated irrigation plan.
     * Called by IrrigationEvaluationHelper when decision = IRRIGATE.
     */
    Long createPlanFromAI(Long deviceId, Long farmId, Long fieldId, Long cropPlanId,
                          String reason, LocalDateTime plannedStart, int durationMinutes);

}
