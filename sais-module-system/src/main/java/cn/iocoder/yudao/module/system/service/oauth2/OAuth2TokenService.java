package cn.iocoder.yudao.module.system.service.oauth2;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.system.controller.admin.oauth2.vo.token.OAuth2AccessTokenPageReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.oauth2.OAuth2AccessTokenDO;

import java.util.List;

/**
 * OAuth2.0 Token Service API
 *
 * Functionally, it is similar to the functions of Spring Security OAuth's DefaultTokenServices + JdbcTokenStore, providing access token and refresh token operations.
 *
 * @author Yudao Source Code
 */
public interface OAuth2TokenService {

    /**
     * Create access token
     * Note: This process will include the creation of refresh tokens
     *
     * Refer to the createAccessToken method of DefaultTokenServices
     *
     * @param userId User ID
     * @param userType User type
     * @param clientId client ID
     * @param scopes Authorization scope
     * @return Access token information
     */
    OAuth2AccessTokenDO createAccessToken(Long userId, Integer userType, String clientId, List<String> scopes);

    /**
     * Refresh access token
     *
     * Refer to the refreshAccessToken method of DefaultTokenServices
     *
     * @param refreshToken Refresh Token
     * @param clientId client ID
     * @return Access token information
     */
    OAuth2AccessTokenDO refreshAccessToken(String refreshToken, String clientId);

    /**
     * Get access token
     *
     * Refer to the getAccessToken method of DefaultTokenServices
     *
     * @param accessToken Access Token
     * @return Access token information
     */
    OAuth2AccessTokenDO getAccessToken(String accessToken);

    /**
     * Verify access token
     *
     * @param accessToken Access Token
     * @return Access token information
     */
    OAuth2AccessTokenDO checkAccessToken(String accessToken);

    /**
     * Remove access token
     * Note: During this process, related refresh tokens will be removed
     *
     * Refer to the revokeToken method of DefaultTokenServices
     *
     * @param accessToken Refresh Token
     * @return Access token information
     */
    OAuth2AccessTokenDO removeAccessToken(String accessToken);

    /**
     * Remove access token
     * Note: During this process, related refresh tokens will be removed
     *
     * Refer to the revokeToken method of DefaultTokenServices
     *
     * @param userId User ID
     * @param userType User type
     */
    void removeAccessToken(Long userId, Integer userType);

    /**
     * Get access token pagination
     *
     * @param reqVO Request
     * @return Access token pagination
     */
    PageResult<OAuth2AccessTokenDO> getAccessTokenPage(OAuth2AccessTokenPageReqVO reqVO);

}
