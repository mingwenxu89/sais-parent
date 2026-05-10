package cn.iocoder.yudao.module.agri.service.crop;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.agri.controller.admin.crop.vo.CropGrowthStagePageReqVO;
import cn.iocoder.yudao.module.agri.controller.admin.crop.vo.CropGrowthStageSaveReqVO;
import cn.iocoder.yudao.module.agri.convert.crop.CropGrowthStageConvert;
import cn.iocoder.yudao.module.agri.dal.dataobject.crop.CropGrowthStageDO;
import cn.iocoder.yudao.module.agri.dal.mysql.crop.CropGrowthStageMapper;
import com.google.common.annotations.VisibleForTesting;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.agri.enums.ErrorCodeConstants.CROP_GROWTH_STAGE_NOT_EXISTS;
import static cn.iocoder.yudao.module.agri.enums.ErrorCodeConstants.CROP_NOT_EXISTS;

@Service
@Validated
@Slf4j
public class CropGrowthStageServiceImpl implements CropGrowthStageService {

    @Resource
    private CropGrowthStageMapper cropGrowthStageMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createCropGrowthStage(CropGrowthStageSaveReqVO createReqVO) {
        if (createReqVO.getCropId() == null) {
            throw exception(CROP_NOT_EXISTS);
        }
        CropGrowthStageDO cropGrowthStage = CropGrowthStageConvert.INSTANCE.convert(createReqVO);
        cropGrowthStageMapper.insert(cropGrowthStage);
        return cropGrowthStage.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCropGrowthStage(CropGrowthStageSaveReqVO updateReqVO) {
        validateCropGrowthStageExists(updateReqVO.getId());
        CropGrowthStageDO updateObj = CropGrowthStageConvert.INSTANCE.convert(updateReqVO);
        cropGrowthStageMapper.updateById(updateObj);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCropGrowthStage(Long id) {
        validateCropGrowthStageExists(id);
        cropGrowthStageMapper.deleteById(id);
    }

    @Override
    public CropGrowthStageDO getCropGrowthStage(Long id) {
        return cropGrowthStageMapper.selectById(id);
    }

    @Override
    public PageResult<CropGrowthStageDO> getCropGrowthStagePage(CropGrowthStagePageReqVO pageReqVO) {
        return cropGrowthStageMapper.selectPage(pageReqVO);
    }

    @Override
    public List<CropGrowthStageDO> getCropGrowthStageListByCropId(Long cropId) {
        return cropGrowthStageMapper.selectListByCropId(cropId);
    }

    @Override
    public void deleteByCropId(Long cropId) {
        cropGrowthStageMapper.deleteByCropIdPhysically(cropId);
    }

    @VisibleForTesting
    public CropGrowthStageDO validateCropGrowthStageExists(Long id) {
        if (id == null) {
            return null;
        }
        CropGrowthStageDO cropGrowthStage = cropGrowthStageMapper.selectById(id);
        if (cropGrowthStage == null) {
            throw exception(CROP_GROWTH_STAGE_NOT_EXISTS);
        }
        return cropGrowthStage;
    }

}
