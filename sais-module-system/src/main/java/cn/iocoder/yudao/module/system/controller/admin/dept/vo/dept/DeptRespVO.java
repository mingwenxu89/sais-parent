package cn.iocoder.yudao.module.system.controller.admin.dept.vo.dept;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "Management backend - department information Response VO")
@Data
public class DeptRespVO {

    @Schema(description = "Department ID", example = "1024")
    private Long id;

    @Schema(description = "Department name", requiredMode = Schema.RequiredMode.REQUIRED, example = "taro road")
    private String name;

    @Schema(description = "Parent department ID", example = "1024")
    private Long parentId;

    @Schema(description = "Display order", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Integer sort;

    @Schema(description = "User ID of the person in charge", example = "2048")
    private Long leaderUserId;

    @Schema(description = "Contact ID", example = "15601691000")
    private String phone;

    @Schema(description = "Email", example = "yudao@iocoder.cn")
    private String email;

    @Schema(description = "Status, see CommonStatusEnum enumeration", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer status;

    @Schema(description = "Create Time", requiredMode = Schema.RequiredMode.REQUIRED, example = "timestamp format")
    private LocalDateTime createTime;

}
