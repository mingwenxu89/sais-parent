package cn.iocoder.yudao.module.system.controller.admin.dept.vo.post;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.validation.InEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Management backend - job creation/modification Request VO")
@Data
public class PostSaveReqVO {

    @Schema(description = "Position ID", example = "1024")
    private Long id;

    @Schema(description = "Job title", requiredMode = Schema.RequiredMode.REQUIRED, example = "small potatoes")
    @NotBlank(message = "Position name cannot be empty")
    @Size(max = 50, message = "Job title cannot exceed 50 characters in length")
    private String name;

    @Schema(description = "Position code", requiredMode = Schema.RequiredMode.REQUIRED, example = "yudao")
    @NotBlank(message = "Position code cannot be empty")
    @Size(max = 64, message = "The length of the position code cannot exceed 64 characters")
    private String code;

    @Schema(description = "Display order", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotNull(message = "Display order cannot be empty")
    private Integer sort;

    @Schema(description = "Status", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @InEnum(CommonStatusEnum.class)
    private Integer status;

    @Schema(description = "Remark", example = "happy note")
    private String remark;

}