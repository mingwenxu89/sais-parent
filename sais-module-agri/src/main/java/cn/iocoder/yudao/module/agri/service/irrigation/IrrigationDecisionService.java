package cn.iocoder.yudao.module.agri.service.irrigation;

import cn.iocoder.yudao.module.agri.controller.admin.irrigation.vo.AiDecisionResultVO;

import java.util.List;

public interface IrrigationDecisionService {

    /**
     * Evaluate fields with a current crop plan in the current tenant and make irrigation decisions.
     * Creates irrigation records with decisionSource=AI for fields that need watering.
     */
    List<AiDecisionResultVO> runDecisionForAllFields();

}
