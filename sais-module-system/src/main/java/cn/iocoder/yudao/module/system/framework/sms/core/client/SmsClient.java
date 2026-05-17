package cn.iocoder.yudao.module.system.framework.sms.core.client;

import cn.iocoder.yudao.framework.common.core.KeyValue;
import cn.iocoder.yudao.module.system.framework.sms.core.client.dto.SmsReceiveRespDTO;
import cn.iocoder.yudao.module.system.framework.sms.core.client.dto.SmsSendRespDTO;
import cn.iocoder.yudao.module.system.framework.sms.core.client.dto.SmsTemplateRespDTO;

import java.util.List;

/**
 * SMS client, used to connect to the SDK of various SMS platforms to implement functions such as sending SMS messages.
 *
 * @author zzf
 * @since 2021/1/25 14:14
 */
public interface SmsClient {

    /**
     * Get channel ID
     *
     * @return Channel ID
     */
    Long getId();

    /**
     * Send message
     *
     * @param logId Log ID
     * @param mobile Mobile phone ID
     * @param apiTemplateId SMS API template ID
     * @param templateParams SMS template parameters. Ensure the order of parameters through List array
     * @return SMS sending result
     */
    SmsSendRespDTO sendSms(Long logId, String mobile, String apiTemplateId,
                           List<KeyValue<String, Object>> templateParams) throws Throwable;

    /**
     * Analyze the reception result of receiving SMS
     *
     * @param text result
     * @return Result content
     * @throws Throwable When an exception occurs while parsing text, an exception will be thrown
     */
    List<SmsReceiveRespDTO> parseSmsReceiveStatus(String text) throws Throwable;

    /**
     * Query the specified SMS template
     *
     * If the query fails, null is returned.
     *
     * @param apiTemplateId SMS API template ID
     * @return SMS template
     */
    SmsTemplateRespDTO getSmsTemplate(String apiTemplateId) throws Throwable;

}
