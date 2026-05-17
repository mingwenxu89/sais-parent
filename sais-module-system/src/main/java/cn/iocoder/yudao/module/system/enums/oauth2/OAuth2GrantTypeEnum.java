package cn.iocoder.yudao.module.system.enums.oauth2;

import cn.hutool.core.util.ArrayUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enumeration of OAuth2 authorization types (modes)
 *
 * @author Yudao Source Code
 */
@AllArgsConstructor
@Getter
public enum OAuth2GrantTypeEnum {

    PASSWORD("password"), // password mode
    AUTHORIZATION_CODE("authorization_code"), // Authorization code mode
    IMPLICIT("implicit"), // Simplified mode
    CLIENT_CREDENTIALS("client_credentials"), // client mode
    REFRESH_TOKEN("refresh_token"), // refresh mode
    ;

    private final String grantType;

    public static OAuth2GrantTypeEnum getByGrantType(String grantType) {
        return ArrayUtil.firstMatch(o -> o.getGrantType().equals(grantType), values());
    }

}
