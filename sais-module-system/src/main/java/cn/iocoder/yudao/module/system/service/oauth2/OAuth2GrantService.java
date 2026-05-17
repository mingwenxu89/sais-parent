package cn.iocoder.yudao.module.system.service.oauth2;

import cn.iocoder.yudao.module.system.dal.dataobject.oauth2.OAuth2AccessTokenDO;

import java.util.List;

/**
 * OAuth2 grant Service API
 *
 * Functionally, it is similar to the function of Spring Security OAuth's TokenGranter, providing access token and refresh token operations.
 *
 * Authorize your own AdminUser user to third-party applications, using the OAuth2.0 protocol.
 *
 * Question: Why does it also go through this process as a third-party application?
 * Reply: Of course you can DO this, using password mode. Considering that most developers cannot use this feature and OAuth2.0 has a certain learning cost, this approach has not been adopted for the time being.
 *
 * @author Yudao Source Code
 */
public interface OAuth2GrantService {

    /**
     * Simplified mode
     *
     * Corresponds to the ImplicitTokenGranter function of Spring Security OAuth2
     *
     * @param userId User ID
     * @param userType User type
     * @param clientId client ID
     * @param scopes Authorization scope
     * @return Access Token
     */
    OAuth2AccessTokenDO grantImplicit(Long userId, Integer userType,
                                      String clientId, List<String> scopes);

    /**
     * Authorization code mode, the first stage, obtain the code authorization code
     *
     * Corresponds to the generateCode method of AuthorizationEndpoint of Spring Security OAuth2
     *
     * @param userId User ID
     * @param userType User type
     * @param clientId client ID
     * @param scopes Authorization scope
     * @param redirectUri Redirect URI
     * @param state Status
     * @return Authorization code
     */
    String grantAuthorizationCodeForCode(Long userId, Integer userType,
                                         String clientId, List<String> scopes,
                                         String redirectUri, String state);

    /**
     * Authorization code mode, second stage, obtain accessToken access token
     *
     * Corresponds to the AuthorizationCodeTokenGranter function of Spring Security OAuth2
     *
     * @param clientId client ID
     * @param code Authorization code
     * @param redirectUri Redirect URI
     * @param state Status
     * @return Access Token
     */
    OAuth2AccessTokenDO grantAuthorizationCodeForAccessToken(String clientId, String code,
                                                             String redirectUri, String state);

    /**
     * password mode
     *
     * Corresponds to the ResourceOwnerPasswordTokenGranter function of Spring Security OAuth2
     *
     * @param username Account
     * @param password Password
     * @param clientId client ID
     * @param scopes Authorization scope
     * @return Access Token
     */
    OAuth2AccessTokenDO grantPassword(String username, String password,
                                      String clientId, List<String> scopes);

    /**
     * refresh mode
     *
     * Corresponds to the ResourceOwnerPasswordTokenGranter function of Spring Security OAuth2
     *
     * @param refreshToken Refresh Token
     * @param clientId client ID
     * @return Access Token
     */
    OAuth2AccessTokenDO grantRefreshToken(String refreshToken, String clientId);

    /**
     * client mode
     *
     * Corresponds to the ClientCredentialsTokenGranter function of Spring Security OAuth2
     *
     * @param clientId client ID
     * @param scopes Authorization scope
     * @return Access Token
     */
    OAuth2AccessTokenDO grantClientCredentials(String clientId, List<String> scopes);

    /**
     * Remove access token
     *
     * Corresponds to the revokeToken method of ConsumerTokenServices of Spring Security OAuth2
     *
     * @param accessToken Access Token
     * @param clientId client ID
     * @return Whether to remove to
     */
    boolean revokeToken(String clientId, String accessToken);

}
