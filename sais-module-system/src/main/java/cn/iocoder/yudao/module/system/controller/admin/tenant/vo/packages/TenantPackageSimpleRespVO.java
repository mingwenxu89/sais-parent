package cn.iocoder.yudao.module.system.controller.admin.tenant.vo.packages;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotNull;

@Schema(description = "Management backend - simplified tenant package Response VO")
@Data
public class TenantPackageSimpleRespVO {

    @Schema(description = "Package ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotNull(message = "Package ID cannot be empty")
    private Long id;

    @Schema(description = "Package name", requiredMode = Schema.RequiredMode.REQUIRED, example = "VIP")
    @NotNull(message = "Package name cannot be empty")
    private String name;

}
