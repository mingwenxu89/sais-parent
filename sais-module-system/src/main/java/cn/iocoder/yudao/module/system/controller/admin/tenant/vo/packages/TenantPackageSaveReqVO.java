package cn.iocoder.yudao.module.system.controller.admin.tenant.vo.packages;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.validation.InEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.Set;

@Schema(description = "Management backend - Tenant package creation/modification Request VO")
@Data
public class TenantPackageSaveReqVO {

    @Schema(description = "Package ID", example = "1024")
    private Long id;

    @Schema(description = "Package name", requiredMode = Schema.RequiredMode.REQUIRED, example = "VIP")
    @NotEmpty(message = "Package name cannot be empty")
    private String name;

    @Schema(description = "Status, see CommonStatusEnum enumeration", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "Status cannot be empty")
    @InEnum(value = CommonStatusEnum.class, message = "status must be {value}")
    private Integer status;

    @Schema(description = "Remark", example = "good")
    private String remark;

    @Schema(description = "associated menu ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "The associated menu ID cannot be empty")
    private Set<Long> menuIds;

}
