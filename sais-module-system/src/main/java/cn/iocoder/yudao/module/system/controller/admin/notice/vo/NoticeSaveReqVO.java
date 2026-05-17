package cn.iocoder.yudao.module.system.controller.admin.notice.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Management background - notification announcement creation/modification Request VO")
@Data
public class NoticeSaveReqVO {

    @Schema(description = "Job announcement ID", example = "1024")
    private Long id;

    @Schema(description = "Announcement title", requiredMode = Schema.RequiredMode.REQUIRED, example = "Little blogger")
    @NotBlank(message = "Announcement title cannot be empty")
    @Size(max = 50, message = "Announcement title cannot exceed 50 characters")
    private String title;

    @Schema(description = "Announcement type", requiredMode = Schema.RequiredMode.REQUIRED, example = "Little blogger")
    @NotNull(message = "Announcement type cannot be empty")
    private Integer type;

    @Schema(description = "Announcement content", requiredMode = Schema.RequiredMode.REQUIRED, example = "half-life coding")
    private String content;

    @Schema(description = "Status, see CommonStatusEnum enumeration class", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer status;

}
