package cn.iocoder.yudao.module.agri.controller.admin.alert.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotNull;

@Schema(description = "Admin - Handle Alert Request VO")
@Data
public class AlertHandleReqVO {

    @Schema(description = "Alert ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "Alert ID is required")
    private Long id;

    @Schema(description = "Handle status (1=HANDLING 2=RESOLVED 3=IGNORED)",
            requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "Handle status is required")
    private Integer status;

}
