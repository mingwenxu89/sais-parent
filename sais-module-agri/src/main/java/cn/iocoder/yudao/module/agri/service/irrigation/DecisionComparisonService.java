package cn.iocoder.yudao.module.agri.service.irrigation;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.agri.controller.admin.evaluation.vo.DecisionEvaluationPageReqVO;
import cn.iocoder.yudao.module.agri.controller.admin.irrigation.vo.AiDecisionResultVO;
import cn.iocoder.yudao.module.agri.controller.admin.irrigation.vo.DecisionComparisonVO;
import cn.iocoder.yudao.module.agri.dal.dataobject.evaluation.DecisionEvaluationRecordDO;
import cn.iocoder.yudao.module.agri.dal.dataobject.field.FieldDO;
import cn.iocoder.yudao.module.agri.dal.dataobject.irrigation.IrrigationDeviceDO;
import cn.iocoder.yudao.module.agri.dal.mysql.crop.CropPlanMapper;
import cn.iocoder.yudao.module.agri.dal.mysql.evaluation.DecisionEvaluationRecordMapper;
import cn.iocoder.yudao.module.agri.dal.mysql.field.FieldMapper;
import cn.iocoder.yudao.module.agri.dal.mysql.irrigation.IrrigationDeviceMapper;
import cn.iocoder.yudao.module.agri.framework.deepseek.DeepSeekClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Dry-run comparison: runs rule-based and AI decision logic on the same inputs
 * without creating irrigation plans, then stores the comparison rows for review.
 */
@Service
@Slf4j
public class DecisionComparisonService {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Resource
    private FieldMapper fieldMapper;
    @Resource
    private CropPlanMapper cropPlanMapper;
    @Resource
    private IrrigationDeviceMapper irrigationDeviceMapper;
    @Resource
    private IrrigationEvaluationHelper helper;
    @Resource
    private DecisionEvaluationRecordMapper decisionEvaluationRecordMapper;

    /** Injected only when DeepSeek is enabled. */
    @Autowired(required = false)
    private DeepSeekClient deepSeekClient;

    /**
     * Compares rule-based vs AI decisions for fields with a current crop plan.
     * Never creates irrigation plans. The comparison rows are persisted as evaluation records.
     */
    public List<DecisionComparisonVO> compareAll() {
        List<Long> currentFieldIds = cropPlanMapper.selectCurrentFieldIds();
        List<FieldDO> currentFields = currentFieldIds.isEmpty()
                ? new ArrayList<>()
                : fieldMapper.selectBatchIds(currentFieldIds);
        log.info("[DecisionComparison] Comparing {} fields with current crop plans (aiAvailable={})",
                currentFields.size(), isAiAvailable());

        List<DecisionComparisonVO> results = new ArrayList<>();
        for (FieldDO field : currentFields) {
            List<IrrigationDeviceDO> devices = irrigationDeviceMapper.selectListByFieldId(field.getId());
            if (devices.isEmpty()) {
                results.add(noDeviceRow(field));
                continue;
            }
            for (IrrigationDeviceDO device : devices) {
                results.add(compareForDevice(field, device));
            }
        }
        persistResults(results);
        return results;
    }

    public PageResult<DecisionComparisonVO> getRecordPage(DecisionEvaluationPageReqVO pageReqVO) {
        PageResult<DecisionEvaluationRecordDO> pageResult = decisionEvaluationRecordMapper.selectPage(pageReqVO);
        List<DecisionComparisonVO> list = pageResult.getList().stream()
                .map(this::toVO)
                .collect(Collectors.toList());
        return new PageResult<>(list, pageResult.getTotal());
    }

    private DecisionComparisonVO compareForDevice(FieldDO field, IrrigationDeviceDO device) {
        AiDecisionResultVO ctx = helper.gatherFieldDataForDevice(field, device);

        DecisionComparisonVO row = new DecisionComparisonVO();
        row.setFieldId(field.getId());
        row.setFieldName(field.getFieldName());
        row.setAiAvailable(isAiAvailable());

        if ("NO_DATA".equals(ctx.getDecision())) {
            row.setCropName(ctx.getCropName());
            row.setStageName(ctx.getStageName());
            row.setRuleDecision("NO_DATA");
            row.setRuleReason(ctx.getReason());
            row.setAiDecision("NO_DATA");
            row.setAiReason(ctx.getReason());
            row.setAligned(true);
            return row;
        }

        row.setCropName(ctx.getCropName());
        row.setStageName(ctx.getStageName());
        row.setCurrentMoisture(ctx.getCurrentMoisture());
        row.setMoistureMin(ctx.getMoistureMin());
        row.setMoistureOptimal(ctx.getMoistureOptimal());
        row.setTomorrowRainfall(ctx.getTomorrowRainfall());

        // Rule-based decision (no side effects)
        AiDecisionResultVO ruleResult = helper.applyRules(copyCtx(ctx));
        row.setRuleDecision(ruleResult.getDecision());
        row.setRuleReason(ruleResult.getReason());
        if ("IRRIGATE".equals(ruleResult.getDecision())) {
            row.setRuleDurationMinutes(helper.estimateDuration(ctx.getCurrentMoisture(), ctx.getMoistureOptimal()));
        }

        // AI decision - call DeepSeek if available, otherwise mark unavailable
        if (!isAiAvailable()) {
            row.setAiDecision("UNAVAILABLE");
            row.setAiReason("DeepSeek not configured.");
            row.setAligned(null);
            return row;
        }

        try {
            String prompt = buildPrompt(ctx);
            String rawResponse = deepSeekClient.complete(prompt);
            fillAiResult(row, rawResponse, ctx);
        } catch (Exception e) {
            log.warn("[DecisionComparison] DeepSeek call failed for field {}: {}", field.getId(), e.getMessage());
            row.setAiDecision("ERROR");
            row.setAiReason("DeepSeek error: " + e.getMessage());
            row.setAligned(false);
            return row;
        }

        row.setAligned(ruleResult.getDecision().equals(row.getAiDecision()));
        return row;
    }

    private void fillAiResult(DecisionComparisonVO row, String raw, AiDecisionResultVO ctx) {
        String jsonStr = extractJson(raw);
        if (jsonStr == null) {
            row.setAiDecision("PARSE_ERROR");
            row.setAiReason("No JSON found in response: " + raw);
            return;
        }
        try {
            JsonNode node = OBJECT_MAPPER.readTree(jsonStr);
            String decision = node.path("decision").asText("").trim().toUpperCase();
            if (!decision.matches("IRRIGATE|SKIP|NO_ACTION")) {
                row.setAiDecision("PARSE_ERROR");
                row.setAiReason("Unrecognised decision value: " + decision);
                return;
            }
            row.setAiDecision(decision);
            row.setAiReason(node.path("reason").asText("(no reason)"));
            if ("IRRIGATE".equals(decision) && node.has("durationMinutes")) {
                row.setAiDurationMinutes(node.path("durationMinutes").asInt());
            }
        } catch (Exception e) {
            row.setAiDecision("PARSE_ERROR");
            row.setAiReason("JSON parse error: " + e.getMessage());
        }
    }

    private String buildPrompt(AiDecisionResultVO ctx) {
        return "You are an agricultural irrigation advisor for a smart farming system.\n" +
                "Based on the following real-time sensor and weather data, decide whether to irrigate this field today.\n\n" +
                "Field: " + ctx.getFieldName() + "\n" +
                "Crop: " + nvl(ctx.getCropName()) + " | Growth Stage: " + nvl(ctx.getStageName()) + "\n" +
                "Soil Moisture Thresholds (this stage):" +
                " Min " + fmt(ctx.getMoistureMin()) + "%," +
                " Max " + fmt(ctx.getMoistureMax()) + "%," +
                " Optimal " + fmt(ctx.getMoistureOptimal()) + "%\n" +
                "Current Soil Moisture: " + fmt(ctx.getCurrentMoisture()) + "%\n" +
                "Rainfall forecast tomorrow: " + fmt(ctx.getTomorrowRainfall()) + " mm\n\n" +
                "Decision rules:\n" +
                "- NO_ACTION: soil moisture >= minimum threshold\n" +
                "- SKIP: soil moisture < minimum BUT rainfall >= 5 mm forecast tomorrow\n" +
                "- IRRIGATE: soil moisture < minimum AND rainfall < 5 mm tomorrow\n\n" +
                "Respond with ONLY a valid JSON object:\n" +
                "{\n" +
                "  \"decision\": \"IRRIGATE\" | \"SKIP\" | \"NO_ACTION\",\n" +
                "  \"reason\": \"<1-3 sentences>\",\n" +
                "  \"durationMinutes\": <integer 15-120, only when IRRIGATE>\n" +
                "}";
    }

    private DecisionComparisonVO noDeviceRow(FieldDO field) {
        DecisionComparisonVO row = new DecisionComparisonVO();
        row.setFieldId(field.getId());
        row.setFieldName(field.getFieldName());
        row.setRuleDecision("NO_DATA");
        row.setRuleReason("No irrigation devices configured.");
        row.setAiDecision("NO_DATA");
        row.setAiReason("No irrigation devices configured.");
        row.setAligned(true);
        row.setAiAvailable(isAiAvailable());
        return row;
    }

    private void persistResults(List<DecisionComparisonVO> results) {
        LocalDateTime evaluatedAt = LocalDateTime.now();
        for (DecisionComparisonVO row : results) {
            DecisionEvaluationRecordDO record = toRecord(row, evaluatedAt);
            decisionEvaluationRecordMapper.insert(record);
            row.setId(record.getId());
            row.setEvaluatedAt(record.getEvaluatedAt());
            row.setCreateTime(record.getCreateTime());
        }
    }

    private DecisionEvaluationRecordDO toRecord(DecisionComparisonVO row, LocalDateTime evaluatedAt) {
        DecisionEvaluationRecordDO record = new DecisionEvaluationRecordDO();
        record.setFieldId(row.getFieldId());
        record.setFieldName(row.getFieldName());
        record.setCropName(row.getCropName());
        record.setStageName(row.getStageName());
        record.setCurrentMoisture(row.getCurrentMoisture());
        record.setMoistureMin(row.getMoistureMin());
        record.setMoistureOptimal(row.getMoistureOptimal());
        record.setTomorrowRainfall(row.getTomorrowRainfall());
        record.setRuleDecision(row.getRuleDecision());
        record.setRuleReason(row.getRuleReason());
        record.setRuleDurationMinutes(row.getRuleDurationMinutes());
        record.setAiDecision(row.getAiDecision());
        record.setAiReason(row.getAiReason());
        record.setAiDurationMinutes(row.getAiDurationMinutes());
        record.setAligned(row.getAligned());
        record.setAiAvailable(row.getAiAvailable());
        record.setEvaluatedAt(evaluatedAt);
        return record;
    }

    private DecisionComparisonVO toVO(DecisionEvaluationRecordDO record) {
        DecisionComparisonVO row = new DecisionComparisonVO();
        row.setId(record.getId());
        row.setFieldId(record.getFieldId());
        row.setFieldName(record.getFieldName());
        row.setCropName(record.getCropName());
        row.setStageName(record.getStageName());
        row.setCurrentMoisture(record.getCurrentMoisture());
        row.setMoistureMin(record.getMoistureMin());
        row.setMoistureOptimal(record.getMoistureOptimal());
        row.setTomorrowRainfall(record.getTomorrowRainfall());
        row.setRuleDecision(record.getRuleDecision());
        row.setRuleReason(record.getRuleReason());
        row.setRuleDurationMinutes(record.getRuleDurationMinutes());
        row.setAiDecision(record.getAiDecision());
        row.setAiReason(record.getAiReason());
        row.setAiDurationMinutes(record.getAiDurationMinutes());
        row.setAligned(record.getAligned());
        row.setAiAvailable(record.getAiAvailable());
        row.setEvaluatedAt(record.getEvaluatedAt());
        row.setCreateTime(record.getCreateTime());
        return row;
    }

    /** Shallow copy so applyRules doesn't mutate the original context. */
    private AiDecisionResultVO copyCtx(AiDecisionResultVO src) {
        AiDecisionResultVO copy = new AiDecisionResultVO();
        copy.setFieldId(src.getFieldId());
        copy.setFieldName(src.getFieldName());
        copy.setCropName(src.getCropName());
        copy.setStageName(src.getStageName());
        copy.setCurrentMoisture(src.getCurrentMoisture());
        copy.setMoistureMin(src.getMoistureMin());
        copy.setMoistureMax(src.getMoistureMax());
        copy.setMoistureOptimal(src.getMoistureOptimal());
        copy.setTomorrowRainfall(src.getTomorrowRainfall());
        return copy;
    }

    private static String extractJson(String raw) {
        if (raw == null) return null;
        int start = raw.indexOf('{');
        int end   = raw.lastIndexOf('}');
        if (start == -1 || end <= start) return null;
        return raw.substring(start, end + 1);
    }

    private static String fmt(BigDecimal v) { return v != null ? String.format("%.1f", v) : "N/A"; }
    private static String nvl(String s)      { return s != null ? s : "Unknown"; }

    private boolean isAiAvailable() {
        return deepSeekClient != null && deepSeekClient.isConfigured();
    }
}
