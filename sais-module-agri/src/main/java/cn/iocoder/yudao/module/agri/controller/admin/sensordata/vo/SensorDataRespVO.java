package cn.iocoder.yudao.module.agri.controller.admin.sensordata.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Admin - Sensor Data Response VO")
@Data
public class SensorDataRespVO {

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "Sensor code")
    private String sensorCode;

    @Schema(description = "Field name")
    private String fieldName;

    @Schema(description = "Data type: SOIL_MOISTURE / HUMIDITY / TEMPERATURE")
    private String dataType;

    @Schema(description = "Sensor value")
    private BigDecimal value;

    @Schema(description = "Collected at")
    private LocalDateTime collectedAt;

}
