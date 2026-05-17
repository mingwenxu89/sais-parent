package cn.iocoder.yudao.module.system.framework.sms.core.client.impl;

import cn.iocoder.yudao.module.system.framework.sms.core.client.SmsClient;
import cn.iocoder.yudao.module.system.framework.sms.core.property.SmsChannelProperties;
import lombok.extern.slf4j.Slf4j;

/**
 * Abstract class of SMS client, providing template methods to reduce redundant code in subclasses
 *
 * @author zzf
 * @since 2021/2/1 9:28
 */
@Slf4j
public abstract class AbstractSmsClient implements SmsClient {

    /**
     * SMS channel configuration
     */
    protected volatile SmsChannelProperties properties;

    public AbstractSmsClient(SmsChannelProperties properties) {
        this.properties = properties;
    }

    /**
     * initialization
     */
    public final void init() {
        log.debug("[init][Configuration ({}) initialization completed]", properties);
    }

    public final void refresh(SmsChannelProperties properties) {
        // Determine whether to update
        if (properties.equals(this.properties)) {
            return;
        }
        log.info("[refresh][Configuration ({}) changes, reinitialize]", properties);
        this.properties = properties;
        // initialization
        this.init();
    }

    @Override
    public Long getId() {
        return properties.getId();
    }

}
