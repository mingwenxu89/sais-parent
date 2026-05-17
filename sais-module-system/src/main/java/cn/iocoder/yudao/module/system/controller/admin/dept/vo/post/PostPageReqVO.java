package cn.iocoder.yudao.module.system.controller.admin.dept.vo.post;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(description = "Management backend - Job paging Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class PostPageReqVO extends PageParam {

    @Schema(description = "Position coding, fuzzy matching", example = "yudao")
    private String code;

    @Schema(description = "Job title, fuzzy matching", example = "taro road")
    private String name;

    @Schema(description = "Display status, see CommonStatusEnum enumeration class", example = "1")
    private Integer status;

}
