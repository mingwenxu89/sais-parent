package cn.iocoder.yudao.module.system.controller.admin.permission.vo.permission;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.util.Collections;
import java.util.Set;

@Schema(description = "Management backend - role assignment menu Request VO")
@Data
public class PermissionAssignRoleMenuReqVO {

    @Schema(description = "role ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "Role ID cannot be empty")
    private Long roleId;

    @Schema(description = "menu ID list", example = "1,3,5")
    private Set<Long> menuIds = Collections.emptySet(); // Keep everything in mind

}
