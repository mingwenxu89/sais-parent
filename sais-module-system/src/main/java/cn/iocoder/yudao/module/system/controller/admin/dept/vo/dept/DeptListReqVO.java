package cn.iocoder.yudao.module.system.controller.admin.dept.vo.dept;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "Management backend - Department list Request VO")
@Data
public class DeptListReqVO {

    @Schema(description = "Department name, fuzzy matching", example = "taro road")
    private String name;

    @Schema(description = "Display status, see CommonStatusEnum enumeration class", example = "1")
    private Integer status;

}
