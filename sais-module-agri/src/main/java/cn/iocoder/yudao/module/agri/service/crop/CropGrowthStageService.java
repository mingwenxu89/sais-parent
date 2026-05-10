package cn.iocoder.yudao.module.agri.service.crop;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.agri.controller.admin.crop.vo.CropGrowthStagePageReqVO;
import cn.iocoder.yudao.module.agri.controller.admin.crop.vo.CropGrowthStageSaveReqVO;
import cn.iocoder.yudao.module.agri.dal.dataobject.crop.CropGrowthStageDO;

import jakarta.validation.Valid;
import java.util.List;

/**
 * Crop Growth Stage Service interface
 */
public interface CropGrowthStageService {

    Long createCropGrowthStage(@Valid CropGrowthStageSaveReqVO createReqVO);

    void updateCropGrowthStage(@Valid CropGrowthStageSaveReqVO updateReqVO);

    void deleteCropGrowthStage(Long id);

    CropGrowthStageDO getCropGrowthStage(Long id);

    PageResult<CropGrowthStageDO> getCropGrowthStagePage(CropGrowthStagePageReqVO pageReqVO);

    List<CropGrowthStageDO> getCropGrowthStageListByCropId(Long cropId);

    void deleteByCropId(Long cropId);

}
