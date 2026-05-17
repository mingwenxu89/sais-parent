package cn.iocoder.yudao.module.system.enums.sms;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * SMS template type enumeration
 *
 * @author Yudao Source Code
 */
@Getter
@AllArgsConstructor
public enum SmsTemplateTypeEnum {

    VERIFICATION_CODE(1), // Captcha
    NOTICE(2), // Notification
    PROMOTION(3), // Marketing
    ;

    /**
     * Type
     */
    private final int type;

}
