package cn.iocoder.yudao.module.system.controller.admin.notice.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "Management background - notification and announcement information Response VO")
@Data
public class NoticeRespVO {

    @Schema(description = "Notice announcement serial ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long id;

    @Schema(description = "Announcement title", requiredMode = Schema.RequiredMode.REQUIRED, example = "Little blogger")
    private String title;

    @Schema(description = "Announcement type", requiredMode = Schema.RequiredMode.REQUIRED, example = "Little blogger")
    private Integer type;

    @Schema(description = "Announcement content", requiredMode = Schema.RequiredMode.REQUIRED, example = "half-life coding")
    private String content;

    @Schema(description = "Status, see CommonStatusEnum enumeration class", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer status;

    @Schema(description = "Create Time", requiredMode = Schema.RequiredMode.REQUIRED, example = "timestamp format")
    private LocalDateTime createTime;

}
