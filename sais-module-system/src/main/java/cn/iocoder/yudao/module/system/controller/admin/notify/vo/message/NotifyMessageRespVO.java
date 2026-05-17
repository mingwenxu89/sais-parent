package cn.iocoder.yudao.module.system.controller.admin.notify.vo.message;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Schema(description = "Management backend - Site message Response VO")
@Data
public class NotifyMessageRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long id;

    @Schema(description = "User ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "25025")
    private Long userId;

    @Schema(description = "User type, see UserTypeEnum enumeration", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Byte userType;

    @Schema(description = "Template ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "13013")
    private Long templateId;

    @Schema(description = "template encoding", requiredMode = Schema.RequiredMode.REQUIRED, example = "test_01")
    private String templateCode;

    @Schema(description = "Template sender name", requiredMode = Schema.RequiredMode.REQUIRED, example = "Yunai")
    private String templateNickname;

    @Schema(description = "Template content", requiredMode = Schema.RequiredMode.REQUIRED, example = "Test content")
    private String templateContent;

    @Schema(description = "template type", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private Integer templateType;

    @Schema(description = "Template parameters", requiredMode = Schema.RequiredMode.REQUIRED)
    private Map<String, Object> templateParams;

    @Schema(description = "Has it been read?", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
    private Boolean readStatus;

    @Schema(description = "reading time")
    private LocalDateTime readTime;

    @Schema(description = "Create Time", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}
