package cn.iocoder.yudao.module.system.controller.admin.dept.vo.dept;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Management backend - department streamlined information Response VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeptSimpleRespVO {

    @Schema(description = "Department ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long id;

    @Schema(description = "Department name", requiredMode = Schema.RequiredMode.REQUIRED, example = "taro road")
    private String name;

    @Schema(description = "Parent department ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long parentId;

}
