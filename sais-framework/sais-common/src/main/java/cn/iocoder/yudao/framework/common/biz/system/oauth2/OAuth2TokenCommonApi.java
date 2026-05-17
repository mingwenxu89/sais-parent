package cn.iocoder.yudao.framework.common.biz.system.oauth2;

import cn.iocoder.yudao.framework.common.biz.system.oauth2.dto.OAuth2AccessTokenCheckRespDTO;
import cn.iocoder.yudao.framework.common.biz.system.oauth2.dto.OAuth2AccessTokenCreateReqDTO;
import cn.iocoder.yudao.framework.common.biz.system.oauth2.dto.OAuth2AccessTokenRespDTO;

import jakarta.validation.Valid;

/**
 * OAuth2.0 Token API interface
 *
 * @author Yudao Source Code
 */
public interface OAuth2TokenCommonApi {

 /**
     * Create access token
 *
     * @param reqDTO Access token creation information
     * @return Access token information
 */
 OAuth2AccessTokenRespDTO createAccessToken(@Valid OAuth2AccessTokenCreateReqDTO reqDTO);

 /**
     * Verify access token
 *
     * @param accessToken access token
     * @return Access token information
 */
 OAuth2AccessTokenCheckRespDTO checkAccessToken(String accessToken);

 /**
     * Remove access token
 *
     * @param accessToken access token
     * @return Access token information
 */
 OAuth2AccessTokenRespDTO removeAccessToken(String accessToken);

 /**
     * Refresh access token
 *
     * @param refreshToken refresh token
     * @param clientId client number
     * @return Access token information
 */
 OAuth2AccessTokenRespDTO refreshAccessToken(String refreshToken, String clientId);

}
