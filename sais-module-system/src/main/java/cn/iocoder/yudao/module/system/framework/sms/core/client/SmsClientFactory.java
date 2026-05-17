package cn.iocoder.yudao.module.system.framework.sms.core.client;

import cn.iocoder.yudao.module.system.framework.sms.core.property.SmsChannelProperties;

/**
 * Factory API of SMS client
 *
 * @author zzf
 * @since 2021/1/28 14:01
 */
public interface SmsClientFactory {

    /**
     * Get SMS Client
     *
     * @param channelId Channel ID
     * @return SMS Client
     */
    SmsClient getSmsClient(Long channelId);

    /**
     * Get SMS Client
     *
     * @param channelCode Channel code
     * @return SMS Client
     */
    SmsClient getSmsClient(String channelCode);

    /**
     * Create SMS Client
     *
     * @param properties Configuration object
     * @return SMS Client
     */
    SmsClient createOrUpdateSmsClient(SmsChannelProperties properties);

}
