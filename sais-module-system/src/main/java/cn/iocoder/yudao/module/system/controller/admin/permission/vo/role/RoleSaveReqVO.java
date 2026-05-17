package cn.iocoder.yudao.module.system.controller.admin.permission.vo.role;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.validation.InEnum;
import com.mzt.logapi.starter.annotation.DiffLogField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Management backend - role creation/update Request VO")
@Data
public class RoleSaveReqVO {

    @Schema(description = "role ID", example = "1")
    private Long id;

    @Schema(description = "Character name", requiredMode = Schema.RequiredMode.REQUIRED, example = "Administrator")
    @NotBlank(message = "Role name cannot be empty")
    @Size(max = 30, message = "Role name cannot exceed 30 characters in length")
    @DiffLogField(name = "Character name")
    private String name;

    @NotBlank(message = "Role flag cannot be empty")
    @Size(max = 100, message = "Character logo cannot exceed 100 characters in length")
    @Schema(description = "character mark", requiredMode = Schema.RequiredMode.REQUIRED, example = "ADMIN")
    @DiffLogField(name = "character mark")
    private String code;

    @Schema(description = "Display order", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotNull(message = "Display order cannot be empty")
    @DiffLogField(name = "Display order")
    private Integer sort;

    @Schema(description = "Status", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    @DiffLogField(name = "Status")
    @NotNull(message = "Status cannot be empty")
    @InEnum(value = CommonStatusEnum.class, message = "status must be {value}")
    private Integer status;

    @Schema(description = "Remark", example = "i am a character")
    @Size(max = 500, message = "Comment length cannot exceed 500 characters")
    @DiffLogField(name = "Remark")
    private String remark;

}
