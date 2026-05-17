package cn.iocoder.yudao.module.system.framework.sms.core.client.impl;

import cn.iocoder.yudao.module.system.framework.sms.core.client.SmsClient;
import cn.iocoder.yudao.module.system.framework.sms.core.client.SmsClientFactory;
import cn.iocoder.yudao.module.system.framework.sms.core.enums.SmsChannelEnum;
import cn.iocoder.yudao.module.system.framework.sms.core.property.SmsChannelProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * SMS client factory API
 *
 * @author zzf
 */
@Validated
@Slf4j
public class SmsClientFactoryImpl implements SmsClientFactory {

    /**
     * SMS client Map
     * key: channel ID, use {@link SmsChannelProperties#getId()}
     */
    private final ConcurrentMap<Long, AbstractSmsClient> channelIdClients = new ConcurrentHashMap<>();

    /**
     * SMS client Map
     * key: channel code, use {@link SmsChannelProperties#getCode()} ()}
     *
     * Note that in some scenarios, you need to obtain a client of a certain channel type, so you need to use it.
     * For example, parsing SMS reception results is relatively universal and does not require the use of a certain channel ID {@link #channelIdClients}
     */
    private final ConcurrentMap<String, AbstractSmsClient> channelCodeClients = new ConcurrentHashMap<>();

    public SmsClientFactoryImpl() {
        // Initialize channelCodeClients collection
        Arrays.stream(SmsChannelEnum.values()).forEach(channel -> {
            // Create an empty SmsChannelProperties object
            SmsChannelProperties properties = new SmsChannelProperties().setCode(channel.getCode())
                    .setApiKey("default default").setApiSecret("default");
            // Create SMS client
            AbstractSmsClient smsClient = createSmsClient(properties);
            channelCodeClients.put(channel.getCode(), smsClient);
        });
    }

    @Override
    public SmsClient getSmsClient(Long channelId) {
        return channelIdClients.get(channelId);
    }

    @Override
    public SmsClient getSmsClient(String channelCode) {
        return channelCodeClients.get(channelCode);
    }

    @Override
    public SmsClient createOrUpdateSmsClient(SmsChannelProperties properties) {
        AbstractSmsClient client = channelIdClients.get(properties.getId());
        if (client == null) {
            client = this.createSmsClient(properties);
            client.init();
            channelIdClients.put(client.getId(), client);
        } else {
            client.refresh(properties);
        }
        return client;
    }

    private AbstractSmsClient createSmsClient(SmsChannelProperties properties) {
        SmsChannelEnum channelEnum = SmsChannelEnum.getByCode(properties.getCode());
        Assert.notNull(channelEnum, String.format("Channel type (%s) is empty", channelEnum));
        // Create client
        switch (channelEnum) {
            case ALIYUN: return new AliyunSmsClient(properties);
            case DEBUG_DING_TALK: return new DebugDingTalkSmsClient(properties);
            case TENCENT: return new TencentSmsClient(properties);
            case HUAWEI: return  new HuaweiSmsClient(properties);
            case QINIU: return new QiniuSmsClient(properties);
        }
        // Creation failed, error log + exception thrown
        log.error("[createSmsClient][Configuration({}) cannot find a suitable client implementation]", properties);
        throw new IllegalArgumentException(String.format("Configuration(%s) No suitable client implementation found", properties));
    }

}
