package cn.iocoder.yudao.module.system.controller.admin.notify.vo.template;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Management backend - Site letter template Response VO")
@Data
public class NotifyTemplateRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long id;

    @Schema(description = "Template name", requiredMode = Schema.RequiredMode.REQUIRED, example = "test template")
    private String name;

    @Schema(description = "Template coding", requiredMode = Schema.RequiredMode.REQUIRED, example = "SEND_TEST")
    private String code;

    @Schema(description = "Template type, corresponding to the system_notify_template_type dict", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer type;

    @Schema(description = "Sender name", requiredMode = Schema.RequiredMode.REQUIRED, example = "potatoes")
    private String nickname;

    @Schema(description = "Template content", requiredMode = Schema.RequiredMode.REQUIRED, example = "I am template content")
    private String content;

    @Schema(description = "parameter array", example = "name,code")
    private List<String> params;

    @Schema(description = "Status, see CommonStatusEnum enumeration", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer status;

    @Schema(description = "Remark", example = "I am a note")
    private String remark;

    @Schema(description = "Create Time", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}
