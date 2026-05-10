package cn.iocoder.yudao.module.agri.service.crop;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.agri.controller.admin.crop.vo.CropPageReqVO;
import cn.iocoder.yudao.module.agri.controller.admin.crop.vo.CropSaveReqVO;
import cn.iocoder.yudao.module.agri.dal.dataobject.crop.CropDO;

import jakarta.validation.Valid;

/**
 * Crop Service interface
 */
public interface CropService {

    Long createCrop(@Valid CropSaveReqVO createReqVO);

    void updateCrop(@Valid CropSaveReqVO updateReqVO);

    void deleteCrop(Long id);

    CropDO getCrop(Long id);

    PageResult<CropDO> getCropPage(CropPageReqVO pageReqVO);

}
