package cn.iocoder.yudao.module.system.controller.admin.permission.vo.role;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Management backend - role streamlining information Response VO")
@Data
public class RoleSimpleRespVO {

    @Schema(description = "role ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long id;

    @Schema(description = "Character name", requiredMode = Schema.RequiredMode.REQUIRED, example = "taro road")
    private String name;

}
