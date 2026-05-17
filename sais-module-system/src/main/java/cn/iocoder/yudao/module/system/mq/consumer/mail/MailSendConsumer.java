package cn.iocoder.yudao.module.system.mq.consumer.mail;

import cn.iocoder.yudao.module.system.mq.message.mail.MailSendMessage;
import cn.iocoder.yudao.module.system.service.mail.MailSendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;

/**
 * For consumers of {@link MailSendMessage}
 *
 * @author Yudao Source Code
 */
@Component
@Slf4j
public class MailSendConsumer {

    @Resource
    private MailSendService mailSendService;

    @EventListener
    @Async // Spring Event By default, the thread sent by Producer is passed @Async Implement asynchronous
    public void onMessage(MailSendMessage message) {
        log.info("[onMessage][Message content({})]", message);
        mailSendService.doSendMail(message);
    }

}
