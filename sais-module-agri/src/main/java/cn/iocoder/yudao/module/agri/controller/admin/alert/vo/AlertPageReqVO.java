package cn.iocoder.yudao.module.agri.controller.admin.alert.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "Admin - Alert Page Request VO")
@Data
public class AlertPageReqVO extends PageParam {

    @Schema(description = "Farm ID", example = "1")
    private Long farmId;

    @Schema(description = "Field ID", example = "1")
    private Long fieldId;

    @Schema(description = "Alert type (1=SENSOR_ABNORMAL 2=EXTREME_WEATHER 3=IRRIGATION_ABNORMAL)", example = "1")
    private Integer alertType;

    @Schema(description = "Alert level (1=INFO 2=WARN 3=CRITICAL)", example = "2")
    private Integer level;

    @Schema(description = "Status (0=UNHANDLED 1=HANDLING 2=RESOLVED 3=IGNORED)", example = "0")
    private Integer status;

}
