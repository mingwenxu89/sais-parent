package cn.iocoder.yudao.module.agri.controller.admin.irrigation.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Schema(description = "Irrigation Device Create/Update Request VO")
@Data
public class IrrigationDeviceSaveReqVO {

    private Long id;

    @Schema(description = "Field ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Field cannot be empty")
    private Long fieldId;

    @Schema(description = "Flow rate")
    private BigDecimal flowRate;

    @Schema(description = "Status: 1=Active, 2=Inactive, 3=Fault", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Status cannot be empty")
    private Integer status;

    @Schema(description = "Associated sensor ID (optional)")
    private Long sensorId;

    @Schema(description = "Simulate device fault for demo — MockDeviceAckJob withholds ACK when true")
    private Boolean simulateFault;

}
