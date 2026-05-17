package cn.iocoder.yudao.module.system.controller.admin.oauth2.vo.client;

import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.util.json.JsonUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Schema(description = "Management backend - OAuth2 client creation/modification Request VO")
@Data
public class OAuth2ClientSaveReqVO {

    @Schema(description = "ID", example = "1024")
    private Long id;

    @Schema(description = "client ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "tudou")
    @NotNull(message = "Client ID cannot be empty")
    private String clientId;

    @Schema(description = "client key", requiredMode = Schema.RequiredMode.REQUIRED, example = "fan")
    @NotNull(message = "Client key cannot be empty")
    private String secret;

    @Schema(description = "Application name", requiredMode = Schema.RequiredMode.REQUIRED, example = "potatoes")
    @NotNull(message = "Application name cannot be empty")
    private String name;

    @Schema(description = "application icon", requiredMode = Schema.RequiredMode.REQUIRED, example = "https://www.iocoder.cn/xx.png")
    @NotNull(message = "The application icon cannot be empty")
    @URL(message = "The address of the app icon is incorrect")
    private String logo;

    @Schema(description = "Application description", example = "I am an application")
    private String description;

    @Schema(description = "Status, see CommonStatusEnum enumeration", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "Status cannot be empty")
    private Integer status;

    @Schema(description = "Access token validity period", requiredMode = Schema.RequiredMode.REQUIRED, example = "8640")
    @NotNull(message = "The validity period of the access token cannot be empty")
    private Integer accessTokenValiditySeconds;

    @Schema(description = "Refresh token validity period", requiredMode = Schema.RequiredMode.REQUIRED, example = "8640000")
    @NotNull(message = "Refresh token validity period cannot be empty")
    private Integer refreshTokenValiditySeconds;

    @Schema(description = "Redirectable URI address", requiredMode = Schema.RequiredMode.REQUIRED, example = "https://www.iocoder.cn")
    @NotNull(message = "Redirectable URI address cannot be empty")
    private List<@NotEmpty(message = "Redirect URI cannot be empty") @URL(message = "Redirect URI is malformed") String> redirectUris;

    @Schema(description = "Grant type, see OAuth2GrantTypeEnum enumeration", requiredMode = Schema.RequiredMode.REQUIRED, example = "password")
    @NotNull(message = "Authorization type cannot be empty")
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

    @AssertTrue(message = "Additional information must be in JSON format")
    public boolean isAdditionalInformationJson() {
        return StrUtil.isEmpty(additionalInformation) || JsonUtils.isJson(additionalInformation);
    }

}
