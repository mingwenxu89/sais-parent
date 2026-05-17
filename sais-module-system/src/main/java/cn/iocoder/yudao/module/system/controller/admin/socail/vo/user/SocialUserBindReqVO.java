package cn.iocoder.yudao.module.system.controller.admin.socail.vo.user;

import cn.iocoder.yudao.module.system.enums.social.SocialTypeEnum;
import cn.iocoder.yudao.framework.common.validation.InEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Management background - social binding Request VO, use code authorization code")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SocialUserBindReqVO {

    @Schema(description = "Type of social platform, see UserSocialTypeEnum enumeration value", requiredMode = Schema.RequiredMode.REQUIRED, example = "10")
    @InEnum(SocialTypeEnum.class)
    @NotNull(message = "The type of social platform cannot be empty")
    private Integer type;

    @Schema(description = "Authorization code", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotEmpty(message = "Authorization code cannot be empty")
    private String code;

    @Schema(description = "state", requiredMode = Schema.RequiredMode.REQUIRED, example = "9b2ffbc1-7425-4155-9894-9d5c08541d62")
    @NotEmpty(message = "state cannot be empty")
    private String state;

}
