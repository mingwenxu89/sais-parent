package cn.iocoder.yudao.module.system.controller.admin.socail.vo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "Management backend - social user Response VO")
@Data
public class SocialUserRespVO {

    @Schema(description = "Primary key (auto-increment strategy)", requiredMode = Schema.RequiredMode.REQUIRED, example = "14569")
    private Long id;

    @Schema(description = "Types of social platforms", requiredMode = Schema.RequiredMode.REQUIRED, example = "30")
    private Integer type;

    @Schema(description = "social openid", requiredMode = Schema.RequiredMode.REQUIRED, example = "666")
    private String openid;

    @Schema(description = "social token", requiredMode = Schema.RequiredMode.REQUIRED, example = "666")
    private String token;

    @Schema(description = "Original Token data, usually in JSON format", requiredMode = Schema.RequiredMode.REQUIRED, example = "{}")
    private String rawTokenInfo;

    @Schema(description = "User nickname", requiredMode = Schema.RequiredMode.REQUIRED, example = "Yunai")
    private String nickname;

    @Schema(description = "User avatar", example = "https://www.iocoder.cn/xxx.png")
    private String avatar;

    @Schema(description = "Raw user data, usually in JSON format", requiredMode = Schema.RequiredMode.REQUIRED, example = "{}")
    private String rawUserInfo;

    @Schema(description = "Last authentication code", requiredMode = Schema.RequiredMode.REQUIRED, example = "666666")
    private String code;

    @Schema(description = "The last authentication state", requiredMode = Schema.RequiredMode.REQUIRED, example = "123456")
    private String state;

    @Schema(description = "Create Time", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

    @Schema(description = "Update Time", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime updateTime;

}
