package cn.iocoder.yudao.module.system.controller.admin.sms.vo.template;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotNull;

@Schema(description = "Management backend - SMS template creation/modification Request VO")
@Data
public class SmsTemplateSaveReqVO {

    @Schema(description = "ID", example = "1024")
    private Long id;

    @Schema(description = "SMS type, see SmsTemplateTypeEnum enumeration class", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "SMS type cannot be empty")
    private Integer type;

    @Schema(description = "Open status, see CommonStatusEnum enumeration class", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "Open status cannot be empty")
    private Integer status;

    @Schema(description = "template encoding", requiredMode = Schema.RequiredMode.REQUIRED, example = "test_01")
    @NotNull(message = "Template encoding cannot be empty")
    private String code;

    @Schema(description = "Template name", requiredMode = Schema.RequiredMode.REQUIRED, example = "yudao")
    @NotNull(message = "Template name cannot be empty")
    private String name;

    @Schema(description = "Template content", requiredMode = Schema.RequiredMode.REQUIRED, example = "Hello, {name}. You look so {like}!")
    @NotNull(message = "Template content cannot be empty")
    private String content;

    @Schema(description = "Remark", example = "Hahaha")
    private String remark;

    @Schema(description = "SMS API template ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "4383920")
    @NotNull(message = "The template ID of SMS API cannot be empty")
    private String apiTemplateId;

    @Schema(description = "SMS channel ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "10")
    @NotNull(message = "SMS channel ID cannot be empty")
    private Long channelId;

}
