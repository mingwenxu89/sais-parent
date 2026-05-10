package cn.iocoder.yudao.module.agri.service.irrigation;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.agri.controller.admin.irrigation.vo.IrrigationPlanPageReqVO;
import cn.iocoder.yudao.module.agri.controller.admin.irrigation.vo.IrrigationPlanRespVO;
import cn.iocoder.yudao.module.agri.controller.admin.irrigation.vo.IrrigationPlanSaveReqVO;
import cn.iocoder.yudao.module.agri.convert.irrigation.IrrigationPlanConvert;
import cn.iocoder.yudao.module.agri.dal.dataobject.field.FieldDO;
import cn.iocoder.yudao.module.agri.dal.dataobject.irrigation.IrrigationPlanDO;
import cn.iocoder.yudao.module.agri.dal.mysql.field.FieldMapper;
import cn.iocoder.yudao.module.agri.dal.mysql.irrigation.IrrigationPlanMapper;
import com.google.common.annotations.VisibleForTesting;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.agri.enums.ErrorCodeConstants.IRRIGATION_PLAN_NOT_EXISTS;

@Service
@Validated
@Slf4j
public class IrrigationPlanServiceImpl implements IrrigationPlanService {

    @Resource
    private IrrigationPlanMapper irrigationPlanMapper;

    @Resource
    private FieldMapper fieldMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createIrrigationPlan(IrrigationPlanSaveReqVO createReqVO) {
        IrrigationPlanDO plan = IrrigationPlanConvert.INSTANCE.convert(createReqVO);
        if (plan.getDecisionSource() == null) {
            plan.setDecisionSource("MANUAL");
        }
        plan.setStatus("PENDING");
        irrigationPlanMapper.insert(plan);
        return plan.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateIrrigationPlan(IrrigationPlanSaveReqVO updateReqVO) {
        validatePlanExists(updateReqVO.getId());
        IrrigationPlanDO updateObj = IrrigationPlanConvert.INSTANCE.convert(updateReqVO);
        irrigationPlanMapper.updateById(updateObj);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteIrrigationPlan(Long id) {
        validatePlanExists(id);
        irrigationPlanMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelIrrigationPlan(Long id) {
        IrrigationPlanDO plan = validatePlanExists(id);
        if ("COMPLETED".equals(plan.getStatus()) || "CANCELLED".equals(plan.getStatus())) {
            return;
        }
        IrrigationPlanDO update = new IrrigationPlanDO();
        update.setId(id);
        update.setStatus("CANCELLED");
        irrigationPlanMapper.updateById(update);
    }

    @Override
    public IrrigationPlanRespVO getIrrigationPlan(Long id) {
        IrrigationPlanDO plan = irrigationPlanMapper.selectById(id);
        if (plan == null) return null;
        return enrichFieldName(IrrigationPlanConvert.INSTANCE.convert(plan));
    }

    @Override
    public PageResult<IrrigationPlanRespVO> getIrrigationPlanPage(IrrigationPlanPageReqVO pageReqVO) {
        PageResult<IrrigationPlanDO> page = irrigationPlanMapper.selectPage(pageReqVO);
        PageResult<IrrigationPlanRespVO> voPage = IrrigationPlanConvert.INSTANCE.convertPage(page);
        voPage.getList().forEach(this::enrichFieldName);
        return voPage;
    }

    private IrrigationPlanRespVO enrichFieldName(IrrigationPlanRespVO vo) {
        if (vo.getFieldId() != null) {
            FieldDO field = fieldMapper.selectById(vo.getFieldId());
            if (field != null) vo.setFieldName(field.getFieldName());
        }
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createPlanFromAI(Long deviceId, Long farmId, Long fieldId, Long cropPlanId,
                                 String reason, LocalDateTime plannedStart, int durationMinutes) {
        IrrigationPlanDO plan = new IrrigationPlanDO();
        plan.setDeviceId(deviceId);
        plan.setFarmId(farmId);
        plan.setFieldId(fieldId);
        plan.setCropPlanId(cropPlanId);
        plan.setDecisionSource("AI");
        plan.setDecisionReason(reason);
        plan.setPlannedStartTime(plannedStart);
        plan.setPlannedDuration(durationMinutes);
        plan.setStatus("PENDING");
        irrigationPlanMapper.insert(plan);
        return plan.getId();
    }

    @VisibleForTesting
    public IrrigationPlanDO validatePlanExists(Long id) {
        if (id == null) {
            return null;
        }
        IrrigationPlanDO plan = irrigationPlanMapper.selectById(id);
        if (plan == null) {
            throw exception(IRRIGATION_PLAN_NOT_EXISTS);
        }
        return plan;
    }

}
