package cn.iocoder.yudao.module.system.controller.admin.mail.vo.template;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Management backend - Email template creation/modification Request VO")
@Data
public class MailTemplateSaveReqVO {

    @Schema(description = "ID", example = "1024")
    private Long id;

    @Schema(description = "Template name", requiredMode = Schema.RequiredMode.REQUIRED, example = "test name")
    @NotNull(message = "Name cannot be empty")
    private String name;

    @Schema(description = "Template ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "test")
    @NotNull(message = "Template ID cannot be empty")
    private String code;

    @Schema(description = "Email account ID sent", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "The email account ID sent cannot be empty.")
    private Long accountId;

    @Schema(description = "Sender name", example = "taro")
    private String nickname;

    @Schema(description = "Title", requiredMode = Schema.RequiredMode.REQUIRED, example = "Registration successful")
    @NotEmpty(message = "Title cannot be empty")
    private String title;

    @Schema(description = "Content", requiredMode = Schema.RequiredMode.REQUIRED, example = "Hello, registration successful")
    @NotEmpty(message = "Content cannot be empty")
    private String content;

    @Schema(description = "Status, see CommonStatusEnum enumeration", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "Status cannot be empty")
    private Integer status;

    @Schema(description = "Remark", example = "ultraman")
    private String remark;

}
