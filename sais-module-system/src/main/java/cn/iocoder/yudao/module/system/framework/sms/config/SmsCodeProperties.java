package cn.iocoder.yudao.module.system.framework.sms.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotNull;
import java.time.Duration;

@ConfigurationProperties(prefix = "yudao.sms-code")
@Validated
@Data
public class SmsCodeProperties {

    /**
     * Expiration time
     */
    @NotNull(message = "Expiration time cannot be empty")
    private Duration expireTimes;
    /**
     * SMS sending frequency
     */
    @NotNull(message = "SMS sending frequency cannot be empty")
    private Duration sendFrequency;
    /**
     * Maximum quantity sent per day
     */
    @NotNull(message = "The maximum quantity sent per day cannot be empty")
    private Integer sendMaximumQuantityPerDay;
    /**
     * Captcha minimum value
     */
    @NotNull(message = "The minimum captcha value cannot be empty")
    private Integer beginCode;
    /**
     * Captcha maximum value
     */
    @NotNull(message = "The maximum value of the captcha cannot be empty")
    private Integer endCode;

}
