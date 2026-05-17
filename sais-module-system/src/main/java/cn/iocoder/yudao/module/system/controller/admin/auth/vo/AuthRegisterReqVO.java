package cn.iocoder.yudao.module.system.controller.admin.auth.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.*;

@Schema(description = "Management backend - Register Request VO")
@Data
public class AuthRegisterReqVO extends CaptchaVerificationReqVO {

    @Schema(description = "User account", requiredMode = Schema.RequiredMode.REQUIRED, example = "yudao")
    @NotBlank(message = "User account cannot be empty")
    @Pattern(regexp = "^[a-zA-Z0-9]{4,30}$", message = "User account consists of IDs and letters")
    @Size(min = 4, max = 30, message = "User account length is 4-30 characters")
    private String username;

    @Schema(description = "User nickname", requiredMode = Schema.RequiredMode.REQUIRED, example = "Yunai")
    @NotBlank(message = "User nickname cannot be empty")
    @Size(max = 30, message = "User nickname cannot exceed 30 characters in length")
    private String nickname;

    @Schema(description = "Password", requiredMode = Schema.RequiredMode.REQUIRED, example = "123456")
    @NotEmpty(message = "Password cannot be empty")
    @Length(min = 4, max = 16, message = "Password length is 4-16 characters")
    private String password;
}