package cn.iocoder.yudao.module.system.controller.admin.notify.vo.template;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.validation.InEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Management backend - Create/modify on-site letter template Request VO")
@Data
public class NotifyTemplateSaveReqVO {

    @Schema(description = "ID", example = "1024")
    private Long id;

    @Schema(description = "Template name", requiredMode = Schema.RequiredMode.REQUIRED, example = "test template")
    @NotEmpty(message = "Template name cannot be empty")
    private String name;

    @Schema(description = "Template coding", requiredMode = Schema.RequiredMode.REQUIRED, example = "SEND_TEST")
    @NotNull(message = "Template code cannot be empty")
    private String code;

    @Schema(description = "Template type, corresponding to the system_notify_template_type dict", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "Template type cannot be empty")
    private Integer type;

    @Schema(description = "Sender name", requiredMode = Schema.RequiredMode.REQUIRED, example = "potatoes")
    @NotEmpty(message = "Sender name cannot be empty")
    private String nickname;

    @Schema(description = "Template content", requiredMode = Schema.RequiredMode.REQUIRED, example = "I am template content")
    @NotEmpty(message = "Template content cannot be empty")
    private String content;

    @Schema(description = "Status, see CommonStatusEnum enumeration", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "Status cannot be empty")
    @InEnum(value = CommonStatusEnum.class, message = "status must be {value}")
    private Integer status;

    @Schema(description = "Remark", example = "I am a note")
    private String remark;

}
