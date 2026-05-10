package cn.iocoder.yudao.module.agri.controller.admin.field.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "Admin - Field Page Request VO")
@Data
public class FieldPageReqVO extends PageParam {

    @Schema(description = "Field name (fuzzy match)", example = "East section")
    private String fieldName;

    @Schema(description = "Growth status", example = "ONGOING")
    private String growStatus;

    private Long farmId;

}
