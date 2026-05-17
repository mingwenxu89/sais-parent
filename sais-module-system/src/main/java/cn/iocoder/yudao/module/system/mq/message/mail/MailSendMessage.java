package cn.iocoder.yudao.module.system.mq.message.mail;

import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.io.File;
import java.util.Collection;

/**
 * Send message via email
 *
 * @author Yudao Source Code
 */
@Data
public class MailSendMessage {

    /**
     * Mail log ID
     */
    @NotNull(message = "Mail log ID cannot be empty")
    private Long logId;
    /**
     * Receiving email address
     */
    @NotEmpty(message = "The receiving email address cannot be empty")
    private Collection<String> toMails;
    /**
     * Cc email address
     */
    private Collection<String> ccMails;
    /**
     * Bcc email address
     */
    private Collection<String> bccMails;
    /**
     * Email account ID
     */
    @NotNull(message = "Email account ID cannot be empty")
    private Long accountId;

    /**
     * Email sender
     */
    private String nickname;
    /**
     * Email title
     */
    @NotEmpty(message = "Email title cannot be empty")
    private String title;
    /**
     * Email content
     */
    @NotEmpty(message = "Email content cannot be empty")
    private String content;

    /**
     * Accessories
     */
    private File[] attachments;

}
