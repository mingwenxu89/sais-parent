package cn.iocoder.yudao.module.system.controller.admin.mail.vo.template;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "Management backend - streamlined email template Response VO")
@Data
public class MailTemplateSimpleRespVO {

    @Schema(description = "Template ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long id;

    @Schema(description = "Template name", requiredMode = Schema.RequiredMode.REQUIRED, example = "Da da da")
    private String name;

}
