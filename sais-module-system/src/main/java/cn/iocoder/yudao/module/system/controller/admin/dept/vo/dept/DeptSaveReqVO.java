package cn.iocoder.yudao.module.system.controller.admin.dept.vo.dept;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.validation.InEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Management backend - department creation/modification Request VO")
@Data
public class DeptSaveReqVO {

    @Schema(description = "Department ID", example = "1024")
    private Long id;

    @Schema(description = "Department name", requiredMode = Schema.RequiredMode.REQUIRED, example = "taro road")
    @NotBlank(message = "Department name cannot be empty")
    @Size(max = 30, message = "Department name cannot exceed 30 characters in length")
    private String name;

    @Schema(description = "Parent department ID", example = "1024")
    private Long parentId;

    @Schema(description = "Display order", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotNull(message = "Display order cannot be empty")
    private Integer sort;

    @Schema(description = "User ID of the person in charge", example = "2048")
    private Long leaderUserId;

    @Schema(description = "Contact ID", example = "15601691000")
    @Size(max = 11, message = "Contact ID cannot exceed 11 characters in length")
    private String phone;

    @Schema(description = "Email", example = "yudao@iocoder.cn")
    @Email(message = "Email format is incorrect")
    @Size(max = 50, message = "Email length cannot exceed 50 characters")
    private String email;

    @Schema(description = "Status, see CommonStatusEnum enumeration", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "Status cannot be empty")
    @InEnum(value = CommonStatusEnum.class, message = "Modification status must be {value}")
    private Integer status;

}
