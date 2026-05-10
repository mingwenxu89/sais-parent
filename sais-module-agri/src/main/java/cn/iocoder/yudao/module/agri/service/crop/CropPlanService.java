package cn.iocoder.yudao.module.agri.service.crop;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.agri.controller.admin.crop.vo.CropPlanPageReqVO;
import cn.iocoder.yudao.module.agri.controller.admin.crop.vo.CropPlanRespVO;
import cn.iocoder.yudao.module.agri.controller.admin.crop.vo.CropPlanSaveReqVO;

import jakarta.validation.Valid;

public interface CropPlanService {

    Long createCropPlan(@Valid CropPlanSaveReqVO createReqVO);

    void updateCropPlan(@Valid CropPlanSaveReqVO updateReqVO);

    void deleteCropPlan(Long id);

    CropPlanRespVO getCropPlan(Long id);

    PageResult<CropPlanRespVO> getCropPlanPage(CropPlanPageReqVO pageReqVO);

    /** Get the currently ongoing crop plan for a field, or null if none */
    CropPlanRespVO getCurrentCropPlanByField(Long fieldId);

}
