package cn.iocoder.yudao.module.agri.controller.admin.irrigation.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Schema(description = "Admin - Save Irrigation Plan Request VO")
@Data
public class IrrigationPlanSaveReqVO {

    @Schema(description = "ID (required when updating)", example = "1")
    private Long id;

    @Schema(description = "Field ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "Field ID is required")
    private Long fieldId;

    @Schema(description = "Device ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "Device ID is required")
    private Long deviceId;

    @Schema(description = "Crop plan ID", example = "1")
    private Long cropPlanId;

    @Schema(description = "Decision source MANUAL / AI", example = "MANUAL")
    private String decisionSource;

    @Schema(description = "Decision reason / notes")
    private String decisionReason;

    @Schema(description = "Planned start time", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Planned start time is required")
    private LocalDateTime plannedStartTime;

    @Schema(description = "Planned irrigation duration (min)", requiredMode = Schema.RequiredMode.REQUIRED, example = "30")
    @NotNull(message = "Planned duration is required")
    @Min(value = 1, message = "Planned duration must be at least 1 minute")
    private Integer plannedDuration;

}
