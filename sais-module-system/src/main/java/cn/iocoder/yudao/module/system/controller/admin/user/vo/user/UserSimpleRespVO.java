package cn.iocoder.yudao.module.system.controller.admin.user.vo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Management backend - user streamlined information Response VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSimpleRespVO {

    @Schema(description = "User ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long id;

    @Schema(description = "User nickname", requiredMode = Schema.RequiredMode.REQUIRED, example = "taro road")
    private String nickname;

    @Schema(description = "Department ID", example = "I am a user")
    private Long deptId;
    @Schema(description = "Department name", example = "IT Department")
    private String deptName;

}
