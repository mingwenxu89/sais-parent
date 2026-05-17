package cn.iocoder.yudao.module.system.controller.admin.socail.vo.user;

import cn.iocoder.yudao.framework.common.validation.InEnum;
import cn.iocoder.yudao.module.system.enums.social.SocialTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Management backend - Cancel social binding Request VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SocialUserUnbindReqVO {

    @Schema(description = "Type of social platform, see UserSocialTypeEnum enumeration value", requiredMode = Schema.RequiredMode.REQUIRED, example = "10")
    @InEnum(SocialTypeEnum.class)
    @NotNull(message = "The type of social platform cannot be empty")
    private Integer type;

    @Schema(description = "openid of social user", requiredMode = Schema.RequiredMode.REQUIRED, example = "IPRmJ0wvBptiPIlGEZiPewGwiEiE")
    @NotEmpty(message = "The openid of social users cannot be empty")
    private String openid;

}
