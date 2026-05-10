package cn.iocoder.yudao.module.agri.service.crop;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.agri.controller.admin.crop.vo.CropPlanPageReqVO;
import cn.iocoder.yudao.module.agri.controller.admin.crop.vo.CropPlanRespVO;
import cn.iocoder.yudao.module.agri.controller.admin.crop.vo.CropPlanSaveReqVO;
import cn.iocoder.yudao.module.agri.convert.crop.CropPlanConvert;
import cn.iocoder.yudao.module.agri.dal.dataobject.crop.CropDO;
import cn.iocoder.yudao.module.agri.dal.dataobject.crop.CropPlanDO;
import cn.iocoder.yudao.module.agri.dal.dataobject.field.FieldDO;
import cn.iocoder.yudao.module.agri.dal.mysql.crop.CropMapper;
import cn.iocoder.yudao.module.agri.dal.mysql.crop.CropPlanMapper;
import cn.iocoder.yudao.module.agri.dal.mysql.field.FieldMapper;
import com.google.common.annotations.VisibleForTesting;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.agri.enums.ErrorCodeConstants.CROP_PLAN_NOT_EXISTS;
import static cn.iocoder.yudao.module.agri.enums.ErrorCodeConstants.CROP_PLAN_NOT_UNSTARTED;

@Service
@Validated
@Slf4j
public class CropPlanServiceImpl implements CropPlanService {

    @Resource
    private CropPlanMapper cropPlanMapper;
    @Resource
    private CropMapper cropMapper;
    @Resource
    private FieldMapper fieldMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createCropPlan(CropPlanSaveReqVO createReqVO) {
        CropPlanDO cropPlan = CropPlanConvert.INSTANCE.convert(createReqVO);
        calcEndDateAndStatus(cropPlan);
        cropPlanMapper.insert(cropPlan);
        return cropPlan.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCropPlan(CropPlanSaveReqVO updateReqVO) {
        CropPlanDO cropPlan = validateCropPlanExists(updateReqVO.getId());
        validateCropPlanUnstarted(cropPlan);
        CropPlanDO updateObj = CropPlanConvert.INSTANCE.convert(updateReqVO);
        calcEndDateAndStatus(updateObj);
        cropPlanMapper.updateById(updateObj);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCropPlan(Long id) {
        CropPlanDO cropPlan = validateCropPlanExists(id);
        validateCropPlanUnstarted(cropPlan);
        cropPlanMapper.deleteById(id);
    }

    @Override
    public CropPlanRespVO getCropPlan(Long id) {
        CropPlanDO cropPlan = cropPlanMapper.selectById(id);
        if (cropPlan == null) {
            return null;
        }
        return convertWithNames(cropPlan);
    }

    @Override
    public PageResult<CropPlanRespVO> getCropPlanPage(CropPlanPageReqVO pageReqVO) {
        PageResult<CropPlanDO> page = cropPlanMapper.selectPage(pageReqVO);
        List<CropPlanRespVO> list = new ArrayList<>();
        for (CropPlanDO cropPlan : page.getList()) {
            list.add(convertWithNames(cropPlan));
        }
        return new PageResult<>(list, page.getTotal());
    }

    @Override
    public CropPlanRespVO getCurrentCropPlanByField(Long fieldId) {
        CropPlanDO cropPlan = cropPlanMapper.selectCurrentByFieldId(fieldId);
        return cropPlan != null ? convertWithNames(cropPlan) : null;
    }

    private CropPlanRespVO convertWithNames(CropPlanDO cropPlan) {
        CropPlanRespVO resp = CropPlanConvert.INSTANCE.convert(cropPlan);
        CropDO crop = cropMapper.selectById(cropPlan.getCropId());
        FieldDO field = fieldMapper.selectById(cropPlan.getFieldId());
        if (crop != null) {
            resp.setCropName(crop.getCropName());
        }
        if (field != null) {
            resp.setFieldName(field.getFieldName());
        }
        // Always compute growStatus from dates, ignoring stored value
        resp.setGrowStatus(computeGrowStatus(cropPlan.getStartDate(), cropPlan.getEndDate()));
        return resp;
    }

    private int computeGrowStatus(LocalDate startDate, LocalDate endDate) {
        LocalDate today = LocalDate.now();
        if (startDate != null && today.isBefore(startDate)) return 1; // Unstarted
        if (endDate != null && today.isAfter(endDate)) return 3;       // Finished
        return 2;                                                        // Ongoing
    }

    @VisibleForTesting
    public CropPlanDO validateCropPlanExists(Long id) {
        if (id == null) {
            return null;
        }
        CropPlanDO cropPlan = cropPlanMapper.selectById(id);
        if (cropPlan == null) {
            throw exception(CROP_PLAN_NOT_EXISTS);
        }
        return cropPlan;
    }

    @VisibleForTesting
    public void validateCropPlanUnstarted(CropPlanDO cropPlan) {
        if (cropPlan.getGrowStatus() != 1) {
            throw exception(CROP_PLAN_NOT_UNSTARTED);
        }
    }

    private void calcEndDateAndStatus(CropPlanDO cropPlan) {
        CropDO crop = cropMapper.selectById(cropPlan.getCropId());
        if (crop == null || crop.getGrowthPeriod() == null) {
            return;
        }
        cropPlan.setEndDate(cropPlan.getStartDate().plusDays(crop.getGrowthPeriod()));
        LocalDate today = LocalDate.now();
        if (today.isBefore(cropPlan.getStartDate())) {
            cropPlan.setGrowStatus(1);
        } else if (today.isBefore(cropPlan.getEndDate()) || today.isEqual(cropPlan.getEndDate())) {
            cropPlan.setGrowStatus(2);
        } else {
            cropPlan.setGrowStatus(3);
        }
    }

}
