package cn.iocoder.yudao.module.system.controller.admin.oauth2.vo.client;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Management backend - OAuth2 client Response VO")
@Data
public class OAuth2ClientRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long id;

    @Schema(description = "client ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "tudou")
    private String clientId;

    @Schema(description = "client key", requiredMode = Schema.RequiredMode.REQUIRED, example = "fan")
    private String secret;

    @Schema(description = "Application name", requiredMode = Schema.RequiredMode.REQUIRED, example = "potatoes")
    private String name;

    @Schema(description = "application icon", requiredMode = Schema.RequiredMode.REQUIRED, example = "https://www.iocoder.cn/xx.png")
    private String logo;

    @Schema(description = "Application description", example = "I am an application")
    private String description;

    @Schema(description = "Status, see CommonStatusEnum enumeration", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer status;

    @Schema(description = "Access token validity period", requiredMode = Schema.RequiredMode.REQUIRED, example = "8640")
    private Integer accessTokenValiditySeconds;

    @Schema(description = "Refresh token validity period", requiredMode = Schema.RequiredMode.REQUIRED, example = "8640000")
    private Integer refreshTokenValiditySeconds;

    @Schema(description = "Redirectable URI address", requiredMode = Schema.RequiredMode.REQUIRED, example = "https://www.iocoder.cn")
    private List<String> redirectUris;

    @Schema(description = "Grant type, see OAuth2GrantTypeEnum enumeration", requiredMode = Schema.RequiredMode.REQUIRED, example = "password")
    private List<String> authorizedGrantTypes;

    @Schema(description = "Authorization scope", example = "user_info")
    private List<String> scopes;

    @Schema(description = "Automatically passed authorization scope", example = "user_info")
    private List<String> autoApproveScopes;

    @Schema(description = "Permission", example = "system:user:query")
    private List<String> authorities;

    @Schema(description = "Resources", example = "1024")
    private List<String> resourceIds;

    @Schema(description = "Additional information", example = "{yunai: true}")
    private String additionalInformation;

    @Schema(description = "Create Time", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}
