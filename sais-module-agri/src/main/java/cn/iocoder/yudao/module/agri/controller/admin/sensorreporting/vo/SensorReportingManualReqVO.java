package cn.iocoder.yudao.module.agri.controller.admin.sensorreporting.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Sensor Reporting Manual Request VO")
@Data
public class SensorReportingManualReqVO {

    @Schema(description = "Field ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "Field ID cannot be empty")
    private Long fieldId;

    @Schema(description = "Sensor ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "Sensor ID cannot be empty")
    private Long sensorId;

    @Schema(description = "Data type", requiredMode = Schema.RequiredMode.REQUIRED, example = "SOIL_MOISTURE")
    @NotBlank(message = "Data type cannot be empty")
    private String dataType;

    @Schema(description = "Sensor value", requiredMode = Schema.RequiredMode.REQUIRED, example = "42.5")
    @NotNull(message = "Sensor value cannot be empty")
    @DecimalMin(value = "0.0", message = "Sensor value must be greater than or equal to 0")
    private BigDecimal value;

    @Schema(description = "Collection time; defaults to now when empty")
    private LocalDateTime collectedAt;

}
