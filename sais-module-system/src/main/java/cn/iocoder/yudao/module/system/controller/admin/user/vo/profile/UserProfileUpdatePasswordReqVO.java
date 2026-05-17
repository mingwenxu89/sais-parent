package cn.iocoder.yudao.module.system.controller.admin.user.vo.profile;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotEmpty;

@Schema(description = "Admin Backstage - User Personal Center Update Password Request VO")
@Data
public class UserProfileUpdatePasswordReqVO {

    @Schema(description = "old password", requiredMode = Schema.RequiredMode.REQUIRED, example = "123456")
    @NotEmpty(message = "The old password cannot be empty")
    @Length(min = 4, max = 16, message = "Password length is 4-16 characters")
    private String oldPassword;

    @Schema(description = "new password", requiredMode = Schema.RequiredMode.REQUIRED, example = "654321")
    @NotEmpty(message = "New password cannot be empty")
    @Length(min = 4, max = 16, message = "Password length is 4-16 characters")
    private String newPassword;

}
