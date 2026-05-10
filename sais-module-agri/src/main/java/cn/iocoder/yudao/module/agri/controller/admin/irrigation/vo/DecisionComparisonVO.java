package cn.iocoder.yudao.module.agri.controller.admin.irrigation.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Schema(description = "Side-by-side comparison of rule-based vs AI irrigation decision (dry-run, no plan created)")
@Data
public class DecisionComparisonVO {

    @Schema(description = "Field ID")
    private Long fieldId;

    @Schema(description = "Field name")
    private String fieldName;

    @Schema(description = "Crop name")
    private String cropName;

    @Schema(description = "Current growth stage")
    private String stageName;

    // ── Input context ──────────────────────────────────────────────────────────

    @Schema(description = "Current soil moisture (%)")
    private BigDecimal currentMoisture;

    @Schema(description = "Soil moisture minimum threshold (%)")
    private BigDecimal moistureMin;

    @Schema(description = "Soil moisture optimal (%)")
    private BigDecimal moistureOptimal;

    @Schema(description = "Tomorrow's rainfall forecast (mm)")
    private BigDecimal tomorrowRainfall;

    // ── Rule-based decision ────────────────────────────────────────────────────

    @Schema(description = "Rule-based decision: IRRIGATE / SKIP / NO_ACTION / NO_DATA")
    private String ruleDecision;

    @Schema(description = "Rule-based decision explanation")
    private String ruleReason;

    // ── AI (Bedrock) decision ─────────────────────────────────────────────────

    @Schema(description = "AI decision: IRRIGATE / SKIP / NO_ACTION / NO_DATA / UNAVAILABLE")
    private String aiDecision;

    @Schema(description = "AI decision explanation")
    private String aiReason;

    @Schema(description = "AI recommended irrigation duration in minutes (only for IRRIGATE)")
    private Integer aiDurationMinutes;

    // ── Accuracy signal ────────────────────────────────────────────────────────

    @Schema(description = "True if AI decision matches rule-based decision")
    private Boolean aligned;

    @Schema(description = "Whether AI was available for this comparison")
    private Boolean aiAvailable;
}
