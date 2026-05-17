package cn.iocoder.yudao.module.system.controller.admin.mail.vo.template;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@Schema(description = "Management background - email sending Req VO")
@Data
public class MailTemplateSendReqVO {

    @Schema(description = "Receive email", requiredMode = Schema.RequiredMode.REQUIRED, example = "[user1@example.com, user2@example.com]")
    @NotEmpty(message = "The receiving email cannot be empty")
    private List<String> toMails;

    @Schema(description = "Cc email", requiredMode = Schema.RequiredMode.REQUIRED, example = "[user3@example.com, user4@example.com]")
    private List<String> ccMails;

    @Schema(description = "Bcc email", requiredMode = Schema.RequiredMode.REQUIRED, example = "[user5@example.com, user6@example.com]")
    private List<String> bccMails;

    @Schema(description = "template encoding", requiredMode = Schema.RequiredMode.REQUIRED, example = "test_01")
    @NotNull(message = "Template encoding cannot be empty")
    private String templateCode;

    @Schema(description = "template parameters")
    private Map<String, Object> templateParams;

}
