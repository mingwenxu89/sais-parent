package cn.iocoder.yudao.module.system.controller.admin.mail.vo.template;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Management backend - Last email response VO")
@Data
public class MailTemplateRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long id;

    @Schema(description = "Template name", requiredMode = Schema.RequiredMode.REQUIRED, example = "test name")
    private String name;

    @Schema(description = "Template ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "test")
    private String code;

    @Schema(description = "Email account ID sent", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long accountId;

    @Schema(description = "Sender name", example = "taro")
    private String nickname;

    @Schema(description = "Title", requiredMode = Schema.RequiredMode.REQUIRED, example = "Registration successful")
    private String title;

    @Schema(description = "Content", requiredMode = Schema.RequiredMode.REQUIRED, example = "Hello, registration successful")
    private String content;

    @Schema(description = "parameter array", example = "name,code")
    private List<String> params;

    @Schema(description = "Status, see CommonStatusEnum enumeration", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer status;

    @Schema(description = "Remark", example = "ultraman")
    private String remark;

    @Schema(description = "Create Time", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}
