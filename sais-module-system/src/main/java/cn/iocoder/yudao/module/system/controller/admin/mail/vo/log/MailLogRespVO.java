package cn.iocoder.yudao.module.system.controller.admin.mail.vo.log;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Schema(description = "Management backend - Email log Response VO")
@Data
public class MailLogRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "31020")
    private Long id;

    @Schema(description = "User ID", example = "30883")
    private Long userId;

    @Schema(description = "User type, see UserTypeEnum enumeration", example = "2")
    private Byte userType;

    @Schema(description = "Receive email address", requiredMode = Schema.RequiredMode.REQUIRED, example = "user1@example.com, user2@example.com")
    private List<String> toMails;

    @Schema(description = "CC email address", requiredMode = Schema.RequiredMode.REQUIRED, example = "user3@example.com, user4@example.com")
    private List<String> ccMails;

    @Schema(description = "Bcc email address", requiredMode = Schema.RequiredMode.REQUIRED, example = "user5@example.com, user6@example.com")
    private List<String> bccMails;

    @Schema(description = "Email account ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "18107")
    private Long accountId;

    @Schema(description = "Send email address", requiredMode = Schema.RequiredMode.REQUIRED, example = "85757@qq.com")
    private String fromMail;

    @Schema(description = "Template ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "5678")
    private Long templateId;

    @Schema(description = "template encoding", requiredMode = Schema.RequiredMode.REQUIRED, example = "test_01")
    private String templateCode;

    @Schema(description = "Template sender name", example = "John Doe")
    private String templateNickname;

    @Schema(description = "Email title", requiredMode = Schema.RequiredMode.REQUIRED, example = "test title")
    private String templateTitle;

    @Schema(description = "Email content", requiredMode = Schema.RequiredMode.REQUIRED, example = "Test content")
    private String templateContent;

    @Schema(description = "Email parameters", requiredMode = Schema.RequiredMode.REQUIRED)
    private Map<String, Object> templateParams;

    @Schema(description = "Send status, see MailSendStatusEnum enumeration", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Byte sendStatus;

    @Schema(description = "Send time")
    private LocalDateTime sendTime;

    @Schema(description = "Send returned message ID", example = "28568")
    private String sendMessageId;

    @Schema(description = "Send exception")
    private String sendException;

    @Schema(description = "Create Time", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}
