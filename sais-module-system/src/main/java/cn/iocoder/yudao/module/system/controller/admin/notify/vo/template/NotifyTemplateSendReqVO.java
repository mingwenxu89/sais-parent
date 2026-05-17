package cn.iocoder.yudao.module.system.controller.admin.notify.vo.template;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.Map;

@Schema(description = "Management backend - sending internal letter templates Request VO")
@Data
public class NotifyTemplateSendReqVO {

    @Schema(description = "user id", requiredMode = Schema.RequiredMode.REQUIRED, example = "01")
    @NotNull(message = "User id cannot be empty")
    private Long userId;

    @Schema(description = "User type", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "User type cannot be empty")
    private Integer userType;

    @Schema(description = "template encoding", requiredMode = Schema.RequiredMode.REQUIRED, example = "01")
    @NotEmpty(message = "Template encoding cannot be empty")
    private String templateCode;

    @Schema(description = "template parameters")
    private Map<String, Object> templateParams;

}
