package cn.iocoder.yudao.module.system.enums.logger;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enumeration class of login results
 */
@Getter
@AllArgsConstructor
public enum LoginResultEnum {

    SUCCESS(0), // Success
    BAD_CREDENTIALS(10), // The account or password is incorrect
    USER_DISABLED(20), // User is disabled
    CAPTCHA_NOT_FOUND(30), // Image captcha does not exist
    CAPTCHA_CODE_ERROR(31), // Image captcha is incorrect

    ;

    /**
     * result
     */
    private final Integer result;

}
