package cn.iocoder.yudao.module.agri.service.irrigation;

import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.agri.controller.admin.irrigation.vo.AiDecisionResultVO;
import cn.iocoder.yudao.module.agri.dal.dataobject.field.FieldDO;
import cn.iocoder.yudao.module.agri.dal.dataobject.irrigation.IrrigationDeviceDO;
import cn.iocoder.yudao.module.agri.dal.mysql.field.FieldMapper;
import cn.iocoder.yudao.module.agri.dal.mysql.irrigation.IrrigationDeviceMapper;
import cn.iocoder.yudao.module.agri.framework.bedrock.AwsBedrockProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;
import software.amazon.awssdk.services.bedrockruntime.model.ContentBlock;
import software.amazon.awssdk.services.bedrockruntime.model.ConversationRole;
import software.amazon.awssdk.services.bedrockruntime.model.ConverseRequest;
import software.amazon.awssdk.services.bedrockruntime.model.ConverseResponse;
import software.amazon.awssdk.services.bedrockruntime.model.InferenceConfiguration;
import software.amazon.awssdk.services.bedrockruntime.model.Message;

import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * AWS Bedrock-powered irrigation decision engine.
 * Active only when a BedrockRuntimeClient bean is present (i.e., credentials are configured).
 * Falls back to rule-based logic per field if the Bedrock call fails.
 */
@Service
@ConditionalOnBean(BedrockRuntimeClient.class)
@Validated
@Slf4j
public class BedrockIrrigationDecisionServiceImpl implements IrrigationDecisionService {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Resource
    private BedrockRuntimeClient bedrockRuntimeClient;
    @Resource
    private AwsBedrockProperties bedrockProperties;
    @Resource
    private FieldMapper fieldMapper;
    @Resource
    private IrrigationDeviceMapper irrigationDeviceMapper;
    @Resource
    private IrrigationEvaluationHelper helper;

    @Override
    public List<AiDecisionResultVO> runDecisionForAllFields() {
        List<FieldDO> ongoingFields = fieldMapper.selectList(
                new LambdaQueryWrapperX<FieldDO>().eq(FieldDO::getGrowStatus, "ONGOING"));
        log.info("[BedrockDecision] Evaluating {} ONGOING fields via model={}",
                ongoingFields.size(), bedrockProperties.getModelId());

        List<AiDecisionResultVO> results = new ArrayList<>();
        for (FieldDO field : ongoingFields) {
            try {
                AiDecisionResultVO result = evaluateField(field);
                results.add(result);
                log.info("[BedrockDecision] Field {} ({}): {} — {}",
                        field.getId(), field.getFieldName(), result.getDecision(), result.getReason());
            } catch (Exception e) {
                log.error("[BedrockDecision] Unexpected error evaluating field {}", field.getId(), e);
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
            // Steps 1–5: gather all context data for this device
            AiDecisionResultVO ctx = helper.gatherFieldDataForDevice(field, device);
            if ("NO_DATA".equals(ctx.getDecision())) {
                last = ctx;
                continue;
            }

            // Step 6: call Bedrock; fall back to rules on any failure
            AiDecisionResultVO result;
            try {
                String prompt = buildPrompt(ctx);
                String rawResponse = callBedrock(prompt);
                result = parseResponse(rawResponse, ctx);
            } catch (Exception e) {
                log.error("[BedrockDecision] Bedrock call failed for field {} device {}, falling back to rule-based logic",
                        field.getId(), device.getDeviceCode(), e);
                result = helper.applyRules(ctx);
                result.setReason("[Fallback] " + result.getReason());
            }

            if ("IRRIGATE".equals(result.getDecision())) {
                helper.activateDevice(field, device, result);
            }
            last = result;
        }
        return last;
    }

    // ── Prompt construction ──────────────────────────────────────────────────

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
                "- NO_ACTION: soil moisture >= minimum threshold (crop is adequately watered)\n" +
                "- SKIP: soil moisture < minimum BUT rainfall >= 5 mm forecast tomorrow (natural rain expected)\n" +
                "- IRRIGATE: soil moisture < minimum AND rainfall < 5 mm tomorrow\n" +
                "For borderline cases, use agronomic judgment about the crop's water needs.\n\n" +
                "Respond with ONLY a valid JSON object — no markdown fences, no extra text:\n" +
                "{\n" +
                "  \"decision\": \"IRRIGATE\" | \"SKIP\" | \"NO_ACTION\",\n" +
                "  \"reason\": \"<1-3 sentences referencing the actual sensor values and explaining the recommendation>\",\n" +
                "  \"durationMinutes\": <integer 15-120, include only when decision is IRRIGATE>\n" +
                "}";
    }

    // ── Bedrock Converse API call ────────────────────────────────────────────

    private String callBedrock(String userPrompt) {
        Message userMessage = Message.builder()
                .role(ConversationRole.USER)
                .content(ContentBlock.fromText(userPrompt))
                .build();

        ConverseRequest request = ConverseRequest.builder()
                .modelId(bedrockProperties.getModelId())
                .messages(userMessage)
                .inferenceConfig(InferenceConfiguration.builder()
                        .maxTokens(512)
                        .temperature(0.0f)   // deterministic for structured JSON
                        .build())
                .build();

        ConverseResponse response = bedrockRuntimeClient.converse(request);
        return response.output().message().content().get(0).text();
    }

    // ── Response parsing ────────────────────────────────────────────────────

    /**
     * Extracts the JSON block from the model's response and validates the decision value.
     * Falls back to rule-based logic if parsing fails or the decision is unrecognised.
     */
    private AiDecisionResultVO parseResponse(String raw, AiDecisionResultVO ctx) {
        String jsonStr = extractJson(raw);
        if (jsonStr == null) {
            log.warn("[BedrockDecision] No JSON found in response for field {}: raw='{}'",
                    ctx.getFieldId(), raw);
            return helper.applyRules(ctx);
        }
        try {
            JsonNode node = OBJECT_MAPPER.readTree(jsonStr);
            String decision = node.path("decision").asText("").trim().toUpperCase();
            if (!decision.matches("IRRIGATE|SKIP|NO_ACTION")) {
                log.warn("[BedrockDecision] Unrecognised decision '{}' for field {}, using rules",
                        decision, ctx.getFieldId());
                return helper.applyRules(ctx);
            }
            ctx.setDecision(decision);
            ctx.setReason(node.path("reason").asText("(no reason provided)"));
            if ("IRRIGATE".equals(decision) && node.has("durationMinutes")) {
                int mins = node.path("durationMinutes").asInt(30);
                ctx.setReason(ctx.getReason() + " Estimated irrigation duration: " + mins + " min.");
            }
            return ctx;
        } catch (Exception e) {
            log.warn("[BedrockDecision] JSON parse error for field {}: {}", ctx.getFieldId(), e.getMessage());
            return helper.applyRules(ctx);
        }
    }

    /** Finds the outermost {...} block in the model's text output. */
    private static String extractJson(String raw) {
        if (raw == null) return null;
        int start = raw.indexOf('{');
        int end   = raw.lastIndexOf('}');
        if (start == -1 || end <= start) return null;
        return raw.substring(start, end + 1);
    }

    private static String fmt(java.math.BigDecimal v) {
        return v != null ? String.format("%.1f", v) : "N/A";
    }

    private static String nvl(String s) {
        return s != null ? s : "Unknown";
    }

}
