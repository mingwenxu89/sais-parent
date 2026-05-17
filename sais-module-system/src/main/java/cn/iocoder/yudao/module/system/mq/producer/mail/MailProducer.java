package cn.iocoder.yudao.module.system.mq.producer.mail;

import cn.iocoder.yudao.module.system.mq.message.mail.MailSendMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.io.File;
import java.util.Collection;

/**
 * Mail Producer for mail-related messages
 *
 * @author wangjingyi
 * @since 2021/4/19 13:33
 */
@Slf4j
@Component
public class MailProducer {

    @Resource
    private ApplicationContext applicationContext;

    /**
     * Send {@link MailSendMessage} message
     *
     * @param sendLogId   Send log encoding
     * @param toMails     Receiving email address
     * @param ccMails     Cc email address
     * @param bccMails    Bcc email address
     * @param accountId   Email account ID
     * @param nickname    Email sender
     * @param title       Email title
     * @param content     Email content
     * @param attachments Accessories
     */
    public void sendMailSendMessage(Long sendLogId,
                                    Collection<String> toMails, Collection<String> ccMails, Collection<String> bccMails,
                                    Long accountId, String nickname, String title, String content,
                                    File[] attachments) {
        MailSendMessage message = new MailSendMessage()
                .setLogId(sendLogId)
                .setToMails(toMails).setCcMails(ccMails).setBccMails(bccMails)
                .setAccountId(accountId).setNickname(nickname)
                .setTitle(title).setContent(content).setAttachments(attachments);
        applicationContext.publishEvent(message);
    }

}
