package cn.iocoder.yudao.module.system.controller.admin.sms.vo.template;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.util.Map;

@Schema(description = "Management background - sending SMS template Request VO")
@Data
public class SmsTemplateSendReqVO {

    @Schema(description = "Mobile phone ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "15601691300")
    @NotNull(message = "Mobile phone ID cannot be empty")
    private String mobile;

    @Schema(description = "template encoding", requiredMode = Schema.RequiredMode.REQUIRED, example = "test_01")
    @NotNull(message = "Template encoding cannot be empty")
    private String templateCode;

    @Schema(description = "template parameters")
    private Map<String, Object> templateParams;

}
