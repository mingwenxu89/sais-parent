package cn.iocoder.yudao.module.system.controller.admin.mail.vo.account;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Management backend - Email account creation/modification Request VO")
@Data
public class MailAccountSaveReqVO {

    @Schema(description = "ID", example = "1024")
    private Long id;

    @Schema(description = "Email", requiredMode = Schema.RequiredMode.REQUIRED, example = "yudaoyuanma@123.com")
    @NotNull(message = "Email cannot be empty")
    @Email(message = "Must be in Email format")
    private String mail;

    @Schema(description = "Username", requiredMode = Schema.RequiredMode.REQUIRED, example = "yudao")
    @NotNull(message = "Username cannot be empty")
    private String username;

    @Schema(description = "Password", requiredMode = Schema.RequiredMode.REQUIRED, example = "123456")
    @NotNull(message = "Password required")
    private String password;

    @Schema(description = "SMTP server domain name", requiredMode = Schema.RequiredMode.REQUIRED, example = "www.iocoder.cn")
    @NotNull(message = "SMTP server domain name cannot be empty")
    private String host;

    @Schema(description = "SMTP server port", requiredMode = Schema.RequiredMode.REQUIRED, example = "80")
    @NotNull(message = "SMTP server port cannot be empty")
    private Integer port;

    @Schema(description = "Whether to enable ssl", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
    @NotNull(message = "Whether to enable ssl required")
    private Boolean sslEnable;

    @Schema(description = "Whether to enable starttls", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
    @NotNull(message = "Whether to enable starttls Required")
    private Boolean starttlsEnable;

}
