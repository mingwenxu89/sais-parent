package cn.iocoder.yudao.module.system.controller.admin.oauth2.vo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

@Schema(description = "Management background - OAuth2 update basic user information Request VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OAuth2UserUpdateReqVO {

    @Schema(description = "User nickname", requiredMode = Schema.RequiredMode.REQUIRED, example = "Yunai")
    @Size(max = 30, message = "User nickname cannot exceed 30 characters in length")
    private String nickname;

    @Schema(description = "User email", example = "yudao@iocoder.cn")
    @Email(message = "Email format is incorrect")
    @Size(max = 50, message = "Email length cannot exceed 50 characters")
    private String email;

    @Schema(description = "Mobile phone ID", example = "15601691300")
    @Length(min = 11, max = 11, message = "Mobile phone ID must be 11 digits long")
    private String mobile;

    @Schema(description = "User gender, see SexEnum enumeration class", example = "1")
    private Integer sex;

}
