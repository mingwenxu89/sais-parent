package cn.iocoder.yudao.module.system.controller.admin.user.vo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Management background - User update password Request VO")
@Data
public class UserUpdatePasswordReqVO {

    @Schema(description = "User ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotNull(message = "User ID cannot be empty")
    private Long id;

    @Schema(description = "Password", requiredMode = Schema.RequiredMode.REQUIRED, example = "123456")
    @NotEmpty(message = "Password cannot be empty")
    @Length(min = 4, max = 16, message = "Password length is 4-16 characters")
    private String password;

}
