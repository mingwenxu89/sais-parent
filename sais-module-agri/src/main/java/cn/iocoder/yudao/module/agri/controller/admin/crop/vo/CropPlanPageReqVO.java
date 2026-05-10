package cn.iocoder.yudao.module.agri.controller.admin.crop.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "Crop Plan Page Request VO")
@Data
public class CropPlanPageReqVO extends PageParam {

    @Schema(description = "Crop ID")
    private Long cropId;

    @Schema(description = "Field ID")
    private Long fieldId;

    @Schema(description = "Growth status: 1=Unstarted, 2=Ongoing, 3=Finished")
    private Integer growStatus;

}
