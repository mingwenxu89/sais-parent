package cn.iocoder.yudao.module.agri.controller.admin.irrigation.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(description = "Irrigation Device Page Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class IrrigationDevicePageReqVO extends PageParam {

    @Schema(description = "Field ID")
    private Long fieldId;

    @Schema(description = "Farm ID")
    private Long farmId;

    @Schema(description = "Status: 1=Active, 2=Inactive, 3=Fault")
    private Integer status;

    @Schema(description = "Is watering")
    private Boolean isWatering;

}
