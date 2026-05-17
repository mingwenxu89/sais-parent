package cn.iocoder.yudao.module.system.controller.admin.permission.vo.permission;

import cn.iocoder.yudao.framework.common.validation.InEnum;
import cn.iocoder.yudao.module.system.enums.permission.DataScopeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.util.Collections;
import java.util.Set;

@Schema(description = "Management backend - Grant role data permissions Request VO")
@Data
public class PermissionAssignRoleDataScopeReqVO {

    @Schema(description = "role ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "Role ID cannot be empty")
    private Long roleId;

    @Schema(description = "Data range, see DataScopeEnum enumeration class", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "Data range cannot be empty")
    @InEnum(value = DataScopeEnum.class, message = "Data range must be {value}")
    private Integer dataScope;

    @Schema(description = "List of department IDs, this field is only required when the range type is DEPT_CUSTOM", example = "1,3,5")
    private Set<Long> dataScopeDeptIds = Collections.emptySet(); // Keep everything in mind

}
