package cn.iocoder.yudao.module.system.mq.producer.sms;

import cn.iocoder.yudao.framework.common.core.KeyValue;
import cn.iocoder.yudao.module.system.mq.message.sms.SmsSendMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.util.List;

/**
 * Producer of SMS text message related messages
 *
 * @author zzf
 * @since 2021/3/9 16:35
 */
@Slf4j
@Component
public class SmsProducer {

    @Resource
    private ApplicationContext applicationContext;

    /**
     * Send {@link SmsSendMessage} message
     *
     * @param logId SMS log ID
     * @param mobile Mobile phone ID
     * @param channelId Channel ID
     * @param apiTemplateId SMS template ID
     * @param templateParams SMS template parameters
     */
    public void sendSmsSendMessage(Long logId, String mobile,
                                   Long channelId, String apiTemplateId, List<KeyValue<String, Object>> templateParams) {
        SmsSendMessage message = new SmsSendMessage().setLogId(logId).setMobile(mobile);
        message.setChannelId(channelId).setApiTemplateId(apiTemplateId).setTemplateParams(templateParams);
        applicationContext.publishEvent(message);
    }

}
