package cn.iocoder.yudao.module.system.framework.sms.core.property;

import cn.iocoder.yudao.module.system.framework.sms.core.enums.SmsChannelEnum;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * SMS channel configuration class
 *
 * @author zzf
 * @since 2021/1/25 17:01
 */
@Data
@Validated
public class SmsChannelProperties {

    /**
     * Channel ID
     */
    @NotNull(message = "SMS channel ID cannot be empty")
    private Long id;
    /**
     * SMS signature
     */
    @NotEmpty(message = "SMS signature cannot be empty")
    private String signature;
    /**
     * Channel code
     *
     * Enum {@link SmsChannelEnum}
     */
    @NotEmpty(message = "Channel code cannot be empty")
    private String code;
    /**
     * SMS API account
     */
    @NotEmpty(message = "The SMS API account cannot be empty")
    private String apiKey;
    /**
     * SMS API key
     */
    @NotEmpty(message = "SMS API key cannot be empty")
    private String apiSecret;
    /**
     * SMS send callback URL
     */
    private String callbackUrl;

}
