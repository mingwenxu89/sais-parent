package cn.iocoder.yudao.module.system.controller.admin.tenant.vo.packages;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Schema(description = "Management backend - Tenant package Response VO")
@Data
public class TenantPackageRespVO {

    @Schema(description = "Package ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long id;

    @Schema(description = "Package name", requiredMode = Schema.RequiredMode.REQUIRED, example = "VIP")
    private String name;

    @Schema(description = "Status, see CommonStatusEnum enumeration", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer status;

    @Schema(description = "Remark", example = "good")
    private String remark;

    @Schema(description = "associated menu ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Set<Long> menuIds;

    @Schema(description = "Create Time", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}
