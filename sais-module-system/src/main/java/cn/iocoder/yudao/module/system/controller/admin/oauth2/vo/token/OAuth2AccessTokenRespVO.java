package cn.iocoder.yudao.module.system.controller.admin.oauth2.vo.token;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Schema(description = "Management backend - access token Response VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OAuth2AccessTokenRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long id;

    @Schema(description = "Access Token", requiredMode = Schema.RequiredMode.REQUIRED, example = "tudou")
    private String accessToken;

    @Schema(description = "Refresh Token", requiredMode = Schema.RequiredMode.REQUIRED, example = "nice")
    private String refreshToken;

    @Schema(description = "User ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "666")
    private Long userId;

    @Schema(description = "User type, see UserTypeEnum enumeration", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private Integer userType;

    @Schema(description = "client ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private String clientId;

    @Schema(description = "Create Time", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

    @Schema(description = "Expiration time", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime expiresTime;

}
