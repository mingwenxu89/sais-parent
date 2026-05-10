package cn.iocoder.yudao.module.agri.controller.admin.sensor.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Sensor Create/Update Request VO")
@Data
public class SensorSaveReqVO {

    private Long id;

    @Schema(description = "Sensor type: 1=Soil Moisture, 2=Humidity, 3=Temperature", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Sensor type cannot be empty")
    private Integer sensorType;

    @Schema(description = "Model")
    private String model;

    @Schema(description = "Farm ID")
    private Long farmId;

    @Schema(description = "Field ID")
    private Long fieldId;

    @Schema(description = "Status: 1=Active, 2=Inactive, 3=Fault", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Status cannot be empty")
    private Integer status;

}
