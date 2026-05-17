package cn.iocoder.yudao.module.system.mq.consumer.sms;

import cn.iocoder.yudao.module.system.mq.message.sms.SmsSendMessage;
import cn.iocoder.yudao.module.system.service.sms.SmsSendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;

/**
 * For consumers of {@link SmsSendMessage}
 *
 * @author zzf
 */
@Component
@Slf4j
public class SmsSendConsumer {

    @Resource
    private SmsSendService smsSendService;

    @EventListener
    @Async // Spring Event By default, the thread sent by Producer is passed @Async Implement asynchronous
    public void onMessage(SmsSendMessage message) {
        log.info("[onMessage][Message content({})]", message);
        smsSendService.doSendSms(message);
    }

}
