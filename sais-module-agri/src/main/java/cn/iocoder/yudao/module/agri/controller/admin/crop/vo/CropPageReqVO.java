package cn.iocoder.yudao.module.agri.controller.admin.crop.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "Admin - Crop Page Request VO")
@Data
public class CropPageReqVO extends PageParam {

    @Schema(description = "Crop name (fuzzy match)", example = "Rice")
    private String cropName;

    @Schema(description = "Crop type", example = "2")
    private Integer cropType;

}
