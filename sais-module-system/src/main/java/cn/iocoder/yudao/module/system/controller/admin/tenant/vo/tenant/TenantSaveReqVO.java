package cn.iocoder.yudao.module.system.controller.admin.tenant.vo.tenant;

import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Management backend - Tenant creation/modification Request VO")
@Data
public class TenantSaveReqVO {

    @Schema(description = "Tenant ID", example = "1024")
    private Long id;

    @Schema(description = "Tenant name", requiredMode = Schema.RequiredMode.REQUIRED, example = "taro road")
    @NotNull(message = "Tenant name cannot be empty")
    private String name;

    @Schema(description = "Contact person", requiredMode = Schema.RequiredMode.REQUIRED, example = "Yunai")
    @NotNull(message = "Contact cannot be empty")
    private String contactName;

    @Schema(description = "Contact mobile phone", example = "15601691300")
    private String contactMobile;

    @Schema(description = "Tenant status", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "Tenant status")
    private Integer status;

    @Schema(description = "Bind domain name array", example = "https://www.iocoder.cn")
    private List<String> websites;

    @Schema(description = "Tenant Package ID", example = "1024")
    private Long packageId;

    @Schema(description = "Expiration time", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Expiration time cannot be empty")
    private LocalDateTime expireTime;

    // ========== Fields that need to be passed only when [Creating] ==========

    @Schema(description = "User account", requiredMode = Schema.RequiredMode.REQUIRED, example = "yudao")
    @Pattern(regexp = "^[a-zA-Z0-9]{4,30}$", message = "User account consists of IDs and letters")
    @Size(min = 4, max = 30, message = "User account length is 4-30 characters")
    private String username;

    @Schema(description = "Password", requiredMode = Schema.RequiredMode.REQUIRED, example = "123456")
    @Length(min = 4, max = 16, message = "Password length is 4-16 characters")
    private String password;

    @AssertTrue(message = "User account and password cannot be empty")
    @JsonIgnore
    public boolean isUsernameValid() {
        return id != null // When modifying, no need to pass
                || (ObjectUtil.isAllNotEmpty(username, password)); // When adding, username and password must be passed.
    }

}
