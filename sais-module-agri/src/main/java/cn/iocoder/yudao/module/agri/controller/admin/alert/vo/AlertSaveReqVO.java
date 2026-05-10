package cn.iocoder.yudao.module.agri.controller.admin.alert.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Admin - Save Alert Request VO")
@Data
public class AlertSaveReqVO {

    @Schema(description = "ID (required when updating)", example = "1")
    private Long id;

    @Schema(description = "Farm ID", example = "1")
    private Long farmId;

    @Schema(description = "Field ID", example = "1")
    private Long fieldId;

    @Schema(description = "Related irrigation plan ID", example = "5")
    private Long irrigationPlanId;

    @Schema(description = "Alert type (1=SENSOR_ABNORMAL 2=EXTREME_WEATHER 3=IRRIGATION_ABNORMAL)",
            requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "Alert type is required")
    private Integer alertType;

    @Schema(description = "Alert level (1=INFO 2=WARN 3=CRITICAL)",
            requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "Alert level is required")
    private Integer level;

    @Schema(description = "Alert description", requiredMode = Schema.RequiredMode.REQUIRED,
            example = "Soil moisture 28.0% is below minimum threshold 40.0%")
    @NotBlank(message = "Alert description is required")
    private String context;

    @Schema(description = "Status (0=UNHANDLED 1=HANDLING 2=RESOLVED 3=IGNORED)", example = "0")
    private Integer status;

}
