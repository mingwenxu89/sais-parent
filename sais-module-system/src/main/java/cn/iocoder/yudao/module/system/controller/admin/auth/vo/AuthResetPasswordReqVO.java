package cn.iocoder.yudao.module.system.controller.admin.auth.vo;

import cn.iocoder.yudao.framework.common.validation.Mobile;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotEmpty;

@Schema(description = "Management backend - Reset account password via SMS Request VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResetPasswordReqVO {

    @Schema(description = "Password", requiredMode = Schema.RequiredMode.REQUIRED, example = "1234")
    @NotEmpty(message = "Password cannot be empty")
    @Length(min = 4, max = 16, message = "Password length is 4-16 characters")
    private String password;

    @Schema(description = "Mobile phone ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "13312341234")
    @NotEmpty(message = "Mobile phone ID cannot be empty")
    @Mobile
    private String mobile;

    @Schema(description = "SMS captcha", requiredMode = Schema.RequiredMode.REQUIRED, example = "123456")
    @NotEmpty(message = "Mobile phone SMS captcha cannot be empty")
    private String code;
}