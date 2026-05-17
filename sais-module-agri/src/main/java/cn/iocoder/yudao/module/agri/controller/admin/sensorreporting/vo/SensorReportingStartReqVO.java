package cn.iocoder.yudao.module.agri.controller.admin.sensorreporting.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "Sensor Reporting Start Request VO")
@Data
public class SensorReportingStartReqVO {

    @Schema(description = "Reporting interval in seconds", requiredMode = Schema.RequiredMode.REQUIRED, example = "60")
    @NotNull(message = "Reporting interval cannot be empty")
    @Min(value = 5, message = "Reporting interval must be at least 5 seconds")
    private Integer intervalSeconds;

}
