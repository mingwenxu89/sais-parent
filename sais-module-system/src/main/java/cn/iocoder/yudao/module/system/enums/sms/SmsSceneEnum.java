package cn.iocoder.yudao.module.system.enums.sms;

import cn.hutool.core.util.ArrayUtil;
import cn.iocoder.yudao.framework.common.core.ArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * Enumeration of scenarios for sending user SMS captchas
 *
 * @author Yudao Source Code
 */
@Getter
@AllArgsConstructor
public enum SmsSceneEnum implements ArrayValuable<Integer> {

    MEMBER_LOGIN(1, "user-sms-login", "Member User - Login with mobile phone ID"),
    MEMBER_UPDATE_MOBILE(2, "user-update-mobile", "Member user - modify mobile phone"),
    MEMBER_UPDATE_PASSWORD(3, "user-update-password", "Member User - Change Password"),
    MEMBER_RESET_PASSWORD(4, "user-reset-password", "Member User - Forgot Password"),

    ADMIN_MEMBER_LOGIN(21, "admin-sms-login", "Backend user - Login with mobile phone ID"),
    ADMIN_MEMBER_REGISTER(22, "admin-sms-register", "Backend user - mobile phone ID registration"),
    ADMIN_MEMBER_RESET_PASSWORD(23, "admin-reset-password", "Backend User - Forgot Password");

    public static final Integer[] ARRAYS = Arrays.stream(values()).map(SmsSceneEnum::getScene).toArray(Integer[]::new);

    /**
     * Verification scenario ID
     */
    private final Integer scene;
    /**
     * Template coding
     */
    private final String templateCode;
    /**
     * Description
     */
    private final String description;

    @Override
    public Integer[] array() {
        return ARRAYS;
    }

    public static SmsSceneEnum getCodeByScene(Integer scene) {
        return ArrayUtil.firstMatch(sceneEnum -> sceneEnum.getScene().equals(scene),
                values());
    }

}
