package cn.iocoder.yudao.module.system.service.mail;

import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.module.system.mq.message.mail.MailSendMessage;

import java.io.File;
import java.util.Collection;
import java.util.Map;

/**
 * Email sending service API
 *
 * @author wangjingyi
 * @since 2022-03-21
 */
public interface MailSendService {

    /**
     * Send a single email to the user in the admin backend
     *
     * @param userId user code
     * @param toMails Receiving email
     * @param ccMails Cc email
     * @param bccMails Bcc email
     * @param templateCode Email template encoding
     * @param templateParams Email template parameters
     * @param attachments Accessories
     * @return Send log ID
     */
    default Long sendSingleMailToAdmin(Long userId,
                                       Collection<String> toMails, Collection<String> ccMails, Collection<String> bccMails,
                                       String templateCode, Map<String, Object> templateParams,
                                       File... attachments) {
        return sendSingleMail(toMails, ccMails, bccMails, userId, UserTypeEnum.ADMIN.getValue(),
                templateCode, templateParams, attachments);
    }

    /**
     * Send a single email to the user of the user APP
     *
     * @param userId user code
     * @param toMails Receiving email
     * @param ccMails Cc email
     * @param bccMails Bcc email
     * @param templateCode Email template encoding
     * @param templateParams Email template parameters
     * @param attachments Accessories
     * @return Send log ID
     */
    default Long sendSingleMailToMember(Long userId,
                                        Collection<String> toMails, Collection<String> ccMails, Collection<String> bccMails,
                                        String templateCode, Map<String, Object> templateParams,
                                        File... attachments) {
        return sendSingleMail(toMails, ccMails, bccMails, userId, UserTypeEnum.MEMBER.getValue(),
                templateCode, templateParams, attachments);
    }

    /**
     * Send a single email
     *
     * @param toMails Receiving email
     * @param ccMails Cc email
     * @param bccMails Bcc email
     * @param userId User ID
     * @param userType User type
     * @param templateCode Email template encoding
     * @param templateParams Email template parameters
     * @param attachments Accessories
     * @return Send log ID
     */
    Long sendSingleMail(Collection<String> toMails, Collection<String> ccMails, Collection<String> bccMails,
                        Long userId, Integer userType,
                        String templateCode, Map<String, Object> templateParams,
                        File... attachments);

    /**
     * Perform real email sending
     * Note that this method is only available to MQ Consumer
     *
     * @param message Mail
     */
    void doSendMail(MailSendMessage message);

}
