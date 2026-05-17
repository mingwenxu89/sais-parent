package cn.iocoder.yudao.module.system.controller.admin.socail.vo.client;

import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.framework.common.validation.InEnum;
import cn.iocoder.yudao.module.system.enums.social.SocialTypeEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;

@Schema(description = "Management backend - social client creation/modification Request VO")
@Data
public class SocialClientSaveReqVO {

    @Schema(description = "ID", example = "27162")
    private Long id;

    @Schema(description = "Application name", requiredMode = Schema.RequiredMode.REQUIRED, example = "yudao mall")
    @NotNull(message = "Application name cannot be empty")
    private String name;

    @Schema(description = "Types of social platforms", requiredMode = Schema.RequiredMode.REQUIRED, example = "31")
    @NotNull(message = "The type of social platform cannot be empty")
    @InEnum(SocialTypeEnum.class)
    private Integer socialType;

    @Schema(description = "User type", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "User type cannot be empty")
    @InEnum(UserTypeEnum.class)
    private Integer userType;

    @Schema(description = "client ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "wwd411c69a39ad2e54")
    @NotNull(message = "Client ID cannot be empty")
    private String clientId;

    @Schema(description = "client key", requiredMode = Schema.RequiredMode.REQUIRED, example = "peter")
    @NotNull(message = "Client key cannot be empty")
    private String clientSecret;

    @Schema(description = "Authorizer’s web application ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "2000045")
    private String agentId;

    @Schema(description = "publicKey public key", example = "2000045")
    private String publicKey;

    @Schema(description = "Status", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "Status cannot be empty")
    @InEnum(CommonStatusEnum.class)
    private Integer status;

    @AssertTrue(message = "agentId cannot be empty")
    @JsonIgnore
    public boolean isAgentIdValid() {
        // If it is Enterprise WeChat, the agentId attribute must be filled in
        return !Objects.equals(socialType, SocialTypeEnum.WECHAT_ENTERPRISE.getType())
                || !StrUtil.isEmpty(agentId);
    }

    @AssertTrue(message = "publicKey cannot be empty")
    @JsonIgnore
    public boolean isPublicKeyValid() {
        // If it is Alipay, the publicKey attribute must be filled in
        return !Objects.equals(socialType, SocialTypeEnum.ALIPAY_MINI_PROGRAM.getType())
                || !StrUtil.isEmpty(publicKey);
    }

}
