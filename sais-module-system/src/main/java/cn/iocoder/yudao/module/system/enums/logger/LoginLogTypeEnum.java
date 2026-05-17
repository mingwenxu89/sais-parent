package cn.iocoder.yudao.module.system.enums.logger;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Login log type enumeration
 */
@Getter
@AllArgsConstructor
public enum LoginLogTypeEnum {

    LOGIN_USERNAME(100), // Log in with account
    LOGIN_SOCIAL(101), // Use social login
    LOGIN_MOBILE(103), // Log in using your mobile phone
    LOGIN_SMS(104), // Login using SMS

    LOGOUT_SELF(200),  // Log out on your own initiative
    LOGOUT_DELETE(202), // force quit
    ;

    /**
     * Log type
     */
    private final Integer type;

}
