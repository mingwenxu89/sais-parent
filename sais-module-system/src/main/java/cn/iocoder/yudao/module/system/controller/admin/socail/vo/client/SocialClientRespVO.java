package cn.iocoder.yudao.module.system.controller.admin.socail.vo.client;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "Management backend - social client Response VO")
@Data
public class SocialClientRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "27162")
    private Long id;

    @Schema(description = "Application name", requiredMode = Schema.RequiredMode.REQUIRED, example = "yudao mall")
    private String name;

    @Schema(description = "Types of social platforms", requiredMode = Schema.RequiredMode.REQUIRED, example = "31")
    private Integer socialType;

    @Schema(description = "User type", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private Integer userType;

    @Schema(description = "client ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "wwd411c69a39ad2e54")
    private String clientId;

    @Schema(description = "client key", requiredMode = Schema.RequiredMode.REQUIRED, example = "peter")
    private String clientSecret;

    @Schema(description = "Authorizer’s web application ID", example = "2000045")
    private String agentId;

    @Schema(description = "publicKey public key", example = "2000045")
    private String publicKey;

    @Schema(description = "Status", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer status;

    @Schema(description = "Create Time", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}
