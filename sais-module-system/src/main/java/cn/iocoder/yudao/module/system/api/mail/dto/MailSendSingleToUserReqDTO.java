package cn.iocoder.yudao.module.system.api.mail.dto;

import lombok.Data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Send email Request DTO
 *
 * @author wangjingqi
 */
@Data
public class MailSendSingleToUserReqDTO {

    /**
     * User ID
     *
     * If it is not empty, load the corresponding user's mailbox and add it to {@link #toMails}
     */
    private Long userId;

    /**
     * Receiving email
     */
    private List<@Email String> toMails;
    /**
     * Cc email
     */
    private List<@Email String> ccMails;
    /**
     * Bcc email
     */
    private List<@Email String> bccMails;


    /**
     * Email template ID
     */
    @NotNull(message = "Email template ID cannot be empty")
    private String templateCode;
    /**
     * Email template parameters
     */
    private Map<String, Object> templateParams;

    /**
     * Accessories
     */
    private File[] attachments;

}
