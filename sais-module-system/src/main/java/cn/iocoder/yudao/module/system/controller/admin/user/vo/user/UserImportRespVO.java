package cn.iocoder.yudao.module.system.controller.admin.user.vo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Schema(description = "Admin Backend - UserImport Response VO")
@Data
@Builder
public class UserImportRespVO {

    @Schema(description = "Successfully created username array", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<String> createUsernames;

    @Schema(description = "Successfully updated username array", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<String> updateUsernames;

    @Schema(description = "User collection that failed to import, key is user name, value is failure reason", requiredMode = Schema.RequiredMode.REQUIRED)
    private Map<String, String> failureUsernames;

}
