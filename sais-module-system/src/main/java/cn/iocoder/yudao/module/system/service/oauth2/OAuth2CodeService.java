package cn.iocoder.yudao.module.system.service.oauth2;

import cn.iocoder.yudao.module.system.dal.dataobject.oauth2.OAuth2CodeDO;

import java.util.List;

/**
 * OAuth2.0 authorization code Service API
 *
 * Functionally, it is similar to the function of JdbcAuthorizationCodeServices of Spring Security OAuth, providing authorization code operations.
 *
 * @author Yudao Source Code
 */
public interface OAuth2CodeService {

    /**
     * Create authorization code
     *
     * Refer to the createAuthorizationCode method of JdbcAuthorizationCodeServices
     *
     * @param userId User ID
     * @param userType User type
     * @param clientId client ID
     * @param scopes Authorization scope
     * @param redirectUri Redirect URI
     * @param state Status
     * @return Authorization code information
     */
    OAuth2CodeDO createAuthorizationCode(Long userId, Integer userType, String clientId,
                                         List<String> scopes, String redirectUri, String state);

    /**
     * Use authorization code
     *
     * @param code Authorization code
     */
    OAuth2CodeDO consumeAuthorizationCode(String code);

}
