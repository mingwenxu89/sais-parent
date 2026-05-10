package cn.iocoder.yudao.module.agri.controller.admin.crop.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "Admin - Crop Growth Stage Page Request VO")
@Data
public class CropGrowthStagePageReqVO extends PageParam {

    @Schema(description = "Crop ID", example = "1")
    private Long cropId;

    @Schema(description = "Stage name (fuzzy match)", example = "Seedling")
    private String stageName;

    @Schema(description = "Stage order", example = "1")
    private Integer stageOrder;

}
