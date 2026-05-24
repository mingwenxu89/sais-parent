package cn.iocoder.yudao.module.agri.service.irrigation;

import cn.iocoder.yudao.module.agri.controller.admin.irrigation.vo.AiDecisionResultVO;
import cn.iocoder.yudao.module.agri.dal.dataobject.field.FieldDO;
import cn.iocoder.yudao.module.agri.dal.dataobject.irrigation.IrrigationDeviceDO;
import cn.iocoder.yudao.module.agri.dal.mysql.crop.CropPlanMapper;
import cn.iocoder.yudao.module.agri.dal.mysql.field.FieldMapper;
import cn.iocoder.yudao.module.agri.dal.mysql.irrigation.IrrigationDeviceMapper;
import cn.iocoder.yudao.module.agri.framework.deepseek.DeepSeekClient;
import cn.iocoder.yudao.module.agri.framework.deepseek.DeepSeekProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * DeepSeek-powered irrigation decision engine.
 * Falls back to rule-based logic per field if the DeepSeek call fails.
 */
@Service
@Primary
@ConditionalOnProperty(prefix = "yudao.agri.deepseek", name = "enabled", havingValue = "true")
@Validated
@Slf4j
public class DeepSeekIrrigationDecisionServiceImpl implements IrrigationDecisionService {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Resource
    private DeepSeekClient deepSeekClient;
    @Resource
    private DeepSeekProperties deepSeekProperties;
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
        log.info("[DeepSeekDecision] Evaluating {} fields with current crop plans via model={}",
                currentFields.size(), deepSeekProperties.getModel());

        List<AiDecisionResultVO> results = new ArrayList<>();
        for (FieldDO field : currentFields) {
            try {
                AiDecisionResultVO result = evaluateField(field);
                results.add(result);
                log.info("[DeepSeekDecision] Field {} ({}): {} - {}",
                        field.getId(), field.getFieldName(), result.getDecision(), result.getReason());
            } catch (Exception e) {
                log.error("[DeepSeekDecision] Unexpected error evaluating field {}", field.getId(), e);
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

            AiDecisionResultVO result;
            try {
                String rawResponse = deepSeekClient.complete(buildPrompt(ctx));
                result = parseResponse(rawResponse, ctx);
            } catch (Exception e) {
                log.error("[DeepSeekDecision] DeepSeek call failed for field {} device {}, falling back to rule-based logic",
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
                "Respond with ONLY a valid JSON object - no markdown fences, no extra text:\n" +
                "{\n" +
                "  \"decision\": \"IRRIGATE\" | \"SKIP\" | \"NO_ACTION\",\n" +
                "  \"reason\": \"<1-3 sentences referencing the actual sensor values and explaining the recommendation>\",\n" +
                "  \"durationMinutes\": <integer 15-120, include only when decision is IRRIGATE>\n" +
                "}";
    }

    private AiDecisionResultVO parseResponse(String raw, AiDecisionResultVO ctx) {
        String jsonStr = extractJson(raw);
        if (jsonStr == null) {
            log.warn("[DeepSeekDecision] No JSON found in response for field {}: raw='{}'",
                    ctx.getFieldId(), raw);
            return helper.applyRules(ctx);
        }
        try {
            JsonNode node = OBJECT_MAPPER.readTree(jsonStr);
            String decision = node.path("decision").asText("").trim().toUpperCase();
            if (!decision.matches("IRRIGATE|SKIP|NO_ACTION")) {
                log.warn("[DeepSeekDecision] Unrecognised decision '{}' for field {}, using rules",
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
            log.warn("[DeepSeekDecision] JSON parse error for field {}: {}", ctx.getFieldId(), e.getMessage());
            return helper.applyRules(ctx);
        }
    }

    private static String extractJson(String raw) {
        if (raw == null) return null;
        int start = raw.indexOf('{');
        int end = raw.lastIndexOf('}');
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
