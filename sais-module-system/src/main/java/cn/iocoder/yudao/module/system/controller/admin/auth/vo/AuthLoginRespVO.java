package cn.iocoder.yudao.module.system.controller.admin.auth.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Schema(description = "Management backend - Login Response VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthLoginRespVO {

    @Schema(description = "User ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long userId;

    @Schema(description = "Access Token", requiredMode = Schema.RequiredMode.REQUIRED, example = "happy")
    private String accessToken;

    @Schema(description = "Refresh Token", requiredMode = Schema.RequiredMode.REQUIRED, example = "nice")
    private String refreshToken;

    @Schema(description = "Expiration time", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime expiresTime;

    @Schema(description = "Tenant ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long tenantId;

}
