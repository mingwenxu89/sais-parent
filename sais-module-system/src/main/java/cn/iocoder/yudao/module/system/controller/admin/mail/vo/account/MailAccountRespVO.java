package cn.iocoder.yudao.module.system.controller.admin.mail.vo.account;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "Management backend - Email account Response VO")
@Data
public class MailAccountRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long id;

    @Schema(description = "Email", requiredMode = Schema.RequiredMode.REQUIRED, example = "yudaoyuanma@123.com")
    private String mail;

    @Schema(description = "Username", requiredMode = Schema.RequiredMode.REQUIRED, example = "yudao")
    private String username;

    @Schema(description = "Password", requiredMode = Schema.RequiredMode.REQUIRED, example = "123456")
    private String password;

    @Schema(description = "SMTP server domain name", requiredMode = Schema.RequiredMode.REQUIRED, example = "www.iocoder.cn")
    private String host;

    @Schema(description = "SMTP server port", requiredMode = Schema.RequiredMode.REQUIRED, example = "80")
    private Integer port;

    @Schema(description = "Whether to enable ssl", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
    private Boolean sslEnable;

    @Schema(description = "Whether to enable starttls", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
    private Boolean starttlsEnable;

    @Schema(description = "Create Time", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}
