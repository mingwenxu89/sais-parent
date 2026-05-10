package cn.iocoder.yudao.module.agri.controller.admin.device.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "Admin - IoT Device Page Request VO")
@Data
public class DevicePageReqVO extends PageParam {

    @Schema(description = "Device name (fuzzy match)", example = "Sensor")
    private String name;

    @Schema(description = "Device code (fuzzy match)", example = "DEV")
    private String deviceCode;

    @Schema(description = "Device type", example = "1")
    private Integer deviceType;

    @Schema(description = "Field ID", example = "1")
    private Long fieldId;

    @Schema(description = "Status", example = "1")
    private Integer status;

}
