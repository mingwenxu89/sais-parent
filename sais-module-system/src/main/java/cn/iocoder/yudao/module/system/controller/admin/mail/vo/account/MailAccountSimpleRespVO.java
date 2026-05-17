package cn.iocoder.yudao.module.system.controller.admin.mail.vo.account;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "Management backend - Streamlined email account Response VO")
@Data
public class MailAccountSimpleRespVO {

    @Schema(description = "Mailbox ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long id;

    @Schema(description = "Email", requiredMode = Schema.RequiredMode.REQUIRED, example = "768541388@qq.com")
    private String mail;

}
