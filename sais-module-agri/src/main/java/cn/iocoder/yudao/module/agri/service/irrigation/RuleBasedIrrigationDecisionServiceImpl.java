package cn.iocoder.yudao.module.agri.service.irrigation;

import cn.iocoder.yudao.module.agri.controller.admin.irrigation.vo.AiDecisionResultVO;
import cn.iocoder.yudao.module.agri.dal.dataobject.field.FieldDO;
import cn.iocoder.yudao.module.agri.dal.dataobject.irrigation.IrrigationDeviceDO;
import cn.iocoder.yudao.module.agri.dal.mysql.crop.CropPlanMapper;
import cn.iocoder.yudao.module.agri.dal.mysql.field.FieldMapper;
import cn.iocoder.yudao.module.agri.dal.mysql.irrigation.IrrigationDeviceMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Rule-based implementation. Used directly when AI is disabled, and as fallback logic by AI flows.
 * All decision logic lives in IrrigationEvaluationHelper.
 */
@Service("ruleBasedIrrigationDecisionService")
@Validated
@Slf4j
public class RuleBasedIrrigationDecisionServiceImpl implements IrrigationDecisionService {

    @Resource
    private FieldMapper fieldMapper;
    @Resource
    private CropPlanMapper cropPlanMapper;
    @Resource
    private IrrigationDeviceMapper irrigationDeviceMapper;
    @Resource
    private IrrigationEvaluationHelper helper;

    @Override
    public List<AiDecisionResultVO> runDecisionForAllFields() {
        List<Long> currentFieldIds = cropPlanMapper.selectCurrentFieldIds();
        List<FieldDO> currentFields = currentFieldIds.isEmpty()
                ? new ArrayList<>()
                : fieldMapper.selectBatchIds(currentFieldIds);
        log.info("[RuleBasedDecision] Evaluating {} fields with current crop plans", currentFields.size());

        List<AiDecisionResultVO> results = new ArrayList<>();
        for (FieldDO field : currentFields) {
            try {
                AiDecisionResultVO result = evaluateField(field);
                results.add(result);
                log.info("[RuleBasedDecision] Field {} ({}): {} — {}",
                        field.getId(), field.getFieldName(), result.getDecision(), result.getReason());
            } catch (Exception e) {
                log.error("[RuleBasedDecision] Error evaluating field {}", field.getId(), e);
                AiDecisionResultVO error = new AiDecisionResultVO();
                error.setFieldId(field.getId());
                error.setFieldName(field.getFieldName());
                error.setDecision("ERROR");
                error.setReason("Evaluation error: " + e.getMessage());
                results.add(error);
            }
        }
        return results;
    }

    private AiDecisionResultVO evaluateField(FieldDO field) {
        List<IrrigationDeviceDO> devices = irrigationDeviceMapper.selectListByFieldId(field.getId());
        if (devices.isEmpty()) {
            AiDecisionResultVO noDevice = new AiDecisionResultVO();
            noDevice.setFieldId(field.getId());
            noDevice.setFieldName(field.getFieldName());
            noDevice.setDecision("NO_DATA");
            noDevice.setReason("No irrigation devices configured for this field.");
            return noDevice;
        }
        AiDecisionResultVO last = null;
        for (IrrigationDeviceDO device : devices) {
            AiDecisionResultVO ctx = helper.gatherFieldDataForDevice(field, device);
            if ("NO_DATA".equals(ctx.getDecision())) {
                last = ctx;
                continue;
            }
            AiDecisionResultVO result = helper.applyRules(ctx);
            if ("IRRIGATE".equals(result.getDecision())) {
                helper.activateDevice(field, device, result);
            }
            last = result;
        }
        return last;
    }

}
