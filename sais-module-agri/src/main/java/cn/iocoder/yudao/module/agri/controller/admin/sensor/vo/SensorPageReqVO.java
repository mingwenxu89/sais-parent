package cn.iocoder.yudao.module.agri.controller.admin.sensor.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(description = "Sensor Page Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class SensorPageReqVO extends PageParam {

    @Schema(description = "Sensor code")
    private String sensorCode;

    @Schema(description = "Sensor type: 1=Soil Moisture, 2=Humidity, 3=Temperature")
    private Integer sensorType;

    @Schema(description = "Farm ID")
    private Long farmId;

    @Schema(description = "Field ID")
    private Long fieldId;

    @Schema(description = "Status: 1=Active, 2=Inactive, 3=Fault")
    private Integer status;

    @Schema(description = "Exclude sensors already bound to other devices; pass the current device ID (0 for create)")
    private Long excludeDeviceId;

}
