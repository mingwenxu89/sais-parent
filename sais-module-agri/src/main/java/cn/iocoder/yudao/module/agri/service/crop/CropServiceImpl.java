package cn.iocoder.yudao.module.agri.service.crop;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.agri.controller.admin.crop.vo.CropPageReqVO;
import cn.iocoder.yudao.module.agri.controller.admin.crop.vo.CropSaveReqVO;
import cn.iocoder.yudao.module.agri.convert.crop.CropConvert;
import cn.iocoder.yudao.module.agri.dal.dataobject.crop.CropDO;
import cn.iocoder.yudao.module.agri.dal.mysql.crop.CropMapper;
import com.google.common.annotations.VisibleForTesting;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.agri.enums.ErrorCodeConstants.CROP_NOT_EXISTS;

import cn.iocoder.yudao.module.agri.dal.dataobject.crop.CropGrowthStageDO;
import java.util.List;

@Service
@Validated
@Slf4j
public class CropServiceImpl implements CropService {

    @Resource
    private CropMapper cropMapper;

    @Resource
    private CropGrowthStageService cropGrowthStageService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createCrop(CropSaveReqVO createReqVO) {
        CropDO crop = CropConvert.INSTANCE.convert(createReqVO);
        cropMapper.insert(crop);

        if (createReqVO.getGrowthStages() != null && !createReqVO.getGrowthStages().isEmpty()) {
            createReqVO.getGrowthStages().forEach(stage -> {
                stage.setCropId(crop.getId());
                cropGrowthStageService.createCropGrowthStage(stage);
            });
        }

        return crop.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCrop(CropSaveReqVO updateReqVO) {
        validateCropExists(updateReqVO.getId());
        CropDO updateObj = CropConvert.INSTANCE.convert(updateReqVO);
        cropMapper.updateById(updateObj);

        // Update growth stages
        if (updateReqVO.getGrowthStages() != null) {
            cropGrowthStageService.deleteByCropId(updateObj.getId());

            // Insert new stages
            if (!updateReqVO.getGrowthStages().isEmpty()) {
                updateReqVO.getGrowthStages().forEach(stage -> {
                    stage.setCropId(updateObj.getId());
                    stage.setId(null); // Force new ID
                    cropGrowthStageService.createCropGrowthStage(stage);
                });
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCrop(Long id) {
        validateCropExists(id);
        // Delete associated growth stages
        List<CropGrowthStageDO> stages = cropGrowthStageService.getCropGrowthStageListByCropId(id);
        if (stages != null && !stages.isEmpty()) {
            stages.forEach(stage -> cropGrowthStageService.deleteCropGrowthStage(stage.getId()));
        }
        cropMapper.deleteById(id);
    }

    @Override
    public CropDO getCrop(Long id) {
        return cropMapper.selectById(id);
    }

    @Override
    public PageResult<CropDO> getCropPage(CropPageReqVO pageReqVO) {
        return cropMapper.selectPage(pageReqVO);
    }

    @VisibleForTesting
    public CropDO validateCropExists(Long id) {
        if (id == null) {
            return null;
        }
        CropDO crop = cropMapper.selectById(id);
        if (crop == null) {
            throw exception(CROP_NOT_EXISTS);
        }
        return crop;
    }

}
