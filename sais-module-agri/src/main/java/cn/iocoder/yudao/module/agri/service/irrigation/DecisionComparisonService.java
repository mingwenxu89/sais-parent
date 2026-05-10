package cn.iocoder.yudao.module.agri.service.irrigation;

import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.agri.controller.admin.irrigation.vo.AiDecisionResultVO;
import cn.iocoder.yudao.module.agri.controller.admin.irrigation.vo.DecisionComparisonVO;
import cn.iocoder.yudao.module.agri.dal.dataobject.field.FieldDO;
import cn.iocoder.yudao.module.agri.dal.dataobject.irrigation.IrrigationDeviceDO;
import cn.iocoder.yudao.module.agri.dal.mysql.field.FieldMapper;
import cn.iocoder.yudao.module.agri.dal.mysql.irrigation.IrrigationDeviceMapper;
import cn.iocoder.yudao.module.agri.framework.bedrock.AwsBedrockProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;
import software.amazon.awssdk.services.bedrockruntime.model.ContentBlock;
import software.amazon.awssdk.services.bedrockruntime.model.ConversationRole;
import software.amazon.awssdk.services.bedrockruntime.model.ConverseRequest;
import software.amazon.awssdk.services.bedrockruntime.model.ConverseResponse;
import software.amazon.awssdk.services.bedrockruntime.model.InferenceConfiguration;
import software.amazon.awssdk.services.bedrockruntime.model.Message;

import jakarta.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Dry-run comparison: runs rule-based and AI decision logic on the same inputs
 * without creating any irrigation plans. Used to measure AI alignment with rules.
 */
@Service
@Slf4j
public class DecisionComparisonService {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Resource
    private FieldMapper fieldMapper;
    @Resource
    private IrrigationDeviceMapper irrigationDeviceMapper;
    @Resource
    private IrrigationEvaluationHelper helper;

    /** Injected only when AWS Bedrock credentials are configured */
    @Autowired(required = false)
    private BedrockRuntimeClient bedrockRuntimeClient;

    @Autowired(required = false)
    private AwsBedrockProperties bedrockProperties;

    /**
     * Compares rule-based vs AI decisions for all ONGOING fields.
     * Never creates irrigation plans — purely for accuracy evaluation.
     */
    public List<DecisionComparisonVO> compareAll() {
        List<FieldDO> ongoingFields = fieldMapper.selectList(
                new LambdaQueryWrapperX<FieldDO>().eq(FieldDO::getGrowStatus, "ONGOING"));
        log.info("[DecisionComparison] Comparing {} ONGOING fields (aiAvailable={})",
                ongoingFields.size(), bedrockRuntimeClient != null);

        List<DecisionComparisonVO> results = new ArrayList<>();
        for (FieldDO field : ongoingFields) {
            List<IrrigationDeviceDO> devices = irrigationDeviceMapper.selectListByFieldId(field.getId());
            if (devices.isEmpty()) {
                results.add(noDeviceRow(field));
                continue;
            }
            for (IrrigationDeviceDO device : devices) {
                results.add(compareForDevice(field, device));
            }
        }
        return results;
    }

    private DecisionComparisonVO compareForDevice(FieldDO field, IrrigationDeviceDO device) {
        AiDecisionResultVO ctx = helper.gatherFieldDataForDevice(field, device);

        DecisionComparisonVO row = new DecisionComparisonVO();
        row.setFieldId(field.getId());
        row.setFieldName(field.getFieldName());
        row.setAiAvailable(bedrockRuntimeClient != null);

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

        // AI decision — call Bedrock if available, otherwise mark unavailable
        if (bedrockRuntimeClient == null) {
            row.setAiDecision("UNAVAILABLE");
            row.setAiReason("AWS Bedrock not configured.");
            row.setAligned(null);
            return row;
        }

        try {
            String prompt = buildPrompt(ctx);
            String rawResponse = callBedrock(prompt);
            fillAiResult(row, rawResponse, ctx);
        } catch (Exception e) {
            log.warn("[DecisionComparison] Bedrock call failed for field {}: {}", field.getId(), e.getMessage());
            row.setAiDecision("ERROR");
            row.setAiReason("Bedrock error: " + e.getMessage());
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

    private String callBedrock(String prompt) {
        Message msg = Message.builder()
                .role(ConversationRole.USER)
                .content(ContentBlock.fromText(prompt))
                .build();
        ConverseRequest req = ConverseRequest.builder()
                .modelId(bedrockProperties.getModelId())
                .messages(msg)
                .inferenceConfig(InferenceConfiguration.builder()
                        .maxTokens(512).temperature(0.0f).build())
                .build();
        ConverseResponse resp = bedrockRuntimeClient.converse(req);
        return resp.output().message().content().get(0).text();
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
        row.setAiAvailable(bedrockRuntimeClient != null);
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
}
