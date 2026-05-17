package cn.iocoder.yudao.module.system.controller.admin.user.vo.profile;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;


@Schema(description = "Management backend - User personal information update Request VO")
@Data
public class UserProfileUpdateReqVO {

    @Schema(description = "User nickname", example = "Yunai")
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

    @Schema(description = "Character avatar", example = "https://www.iocoder.cn/1.png")
    @URL(message = "The avatar address format is incorrect")
    private String avatar;

}
