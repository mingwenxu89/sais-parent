package cn.iocoder.yudao.module.agri.controller.admin.irrigation.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Schema(description = "AI Irrigation Decision Result VO")
@Data
public class AiDecisionResultVO {

    @Schema(description = "Field ID")
    private Long fieldId;

    @Schema(description = "Field name")
    private String fieldName;

    @Schema(description = "Crop name")
    private String cropName;

    @Schema(description = "Active crop plan ID")
    private Long cropPlanId;

    @Schema(description = "Current growth stage name")
    private String stageName;

    @Schema(description = "Current soil moisture reading (%)")
    private BigDecimal currentMoisture;

    @Schema(description = "Minimum soil moisture threshold for current stage (%)")
    private BigDecimal moistureMin;

    @Schema(description = "Maximum soil moisture threshold for current stage (%)")
    private BigDecimal moistureMax;

    @Schema(description = "Optimal soil moisture for current stage (%)")
    private BigDecimal moistureOptimal;

    @Schema(description = "Tomorrow's rainfall forecast (mm)")
    private BigDecimal tomorrowRainfall;

    @Schema(description = "Decision outcome: IRRIGATE / SKIP / NO_ACTION / NO_DATA / ERROR")
    private String decision;

    @Schema(description = "Human-readable explanation for the decision")
    private String reason;

    @Schema(description = "Created irrigation plan ID (only set when decision = IRRIGATE)")
    private Long irrigationPlanId;

}
