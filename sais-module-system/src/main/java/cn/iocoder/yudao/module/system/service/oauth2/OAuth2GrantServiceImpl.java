package cn.iocoder.yudao.module.system.service.oauth2;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.module.system.dal.dataobject.oauth2.OAuth2AccessTokenDO;
import cn.iocoder.yudao.module.system.dal.dataobject.oauth2.OAuth2CodeDO;
import cn.iocoder.yudao.module.system.dal.dataobject.user.AdminUserDO;
import cn.iocoder.yudao.module.system.enums.ErrorCodeConstants;
import cn.iocoder.yudao.module.system.service.auth.AdminAuthService;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;

/**
 * OAuth2 grant Service implementation class
 *
 * @author Yudao Source Code
 */
@Service
public class OAuth2GrantServiceImpl implements OAuth2GrantService {

    @Resource
    private OAuth2TokenService oauth2TokenService;
    @Resource
    private OAuth2CodeService oauth2CodeService;
    @Resource
    private AdminAuthService adminAuthService;

    @Override
    public OAuth2AccessTokenDO grantImplicit(Long userId, Integer userType,
                                             String clientId, List<String> scopes) {
        return oauth2TokenService.createAccessToken(userId, userType, clientId, scopes);
    }

    @Override
    public String grantAuthorizationCodeForCode(Long userId, Integer userType,
                                                String clientId, List<String> scopes,
                                                String redirectUri, String state) {
        return oauth2CodeService.createAuthorizationCode(userId, userType, clientId, scopes,
                redirectUri, state).getCode();
    }

    @Override
    public OAuth2AccessTokenDO grantAuthorizationCodeForAccessToken(String clientId, String code,
                                                                    String redirectUri, String state) {
        OAuth2CodeDO codeDO = oauth2CodeService.consumeAuthorizationCode(code);
        Assert.notNull(codeDO, "Authorization code cannot be empty"); // defensive programming
        // Verify clientId matches
        if (!StrUtil.equals(clientId, codeDO.getClientId())) {
            throw exception(ErrorCodeConstants.OAUTH2_GRANT_CLIENT_ID_MISMATCH);
        }
        // Verify whether redirectUri matches
        if (!StrUtil.equals(redirectUri, codeDO.getRedirectUri())) {
            throw exception(ErrorCodeConstants.OAUTH2_GRANT_REDIRECT_URI_MISMATCH);
        }
        // Verify whether state matches
        state = StrUtil.nullToDefault(state, ""); // When the database state is null, it will be set to "" empty string
        if (!StrUtil.equals(state, codeDO.getState())) {
            throw exception(ErrorCodeConstants.OAUTH2_GRANT_STATE_MISMATCH);
        }

        // Create access token
        return oauth2TokenService.createAccessToken(codeDO.getUserId(), codeDO.getUserType(),
                codeDO.getClientId(), codeDO.getScopes());
    }

    @Override
    public OAuth2AccessTokenDO grantPassword(String username, String password, String clientId, List<String> scopes) {
        // Log in using account + password
        AdminUserDO user = adminAuthService.authenticate(username, password);
        Assert.notNull(user, "User cannot be empty!"); // defensive programming

        // Create access token
        return oauth2TokenService.createAccessToken(user.getId(), UserTypeEnum.ADMIN.getValue(), clientId, scopes);
    }

    @Override
    public OAuth2AccessTokenDO grantRefreshToken(String refreshToken, String clientId) {
        return oauth2TokenService.refreshAccessToken(refreshToken, clientId);
    }

    @Override
    public OAuth2AccessTokenDO grantClientCredentials(String clientId, List<String> scopes) {
        // Special: https://yuanbao.tencent.com/bot/app/share/chat/wFj642xSZHHx
        return oauth2TokenService.createAccessToken(0L, UserTypeEnum.ADMIN.getValue(), clientId, scopes);
    }

    @Override
    public boolean revokeToken(String clientId, String accessToken) {
        // Query first to ensure that the clientId matches
        OAuth2AccessTokenDO accessTokenDO = oauth2TokenService.getAccessToken(accessToken);
        if (accessTokenDO == null || ObjectUtil.notEqual(clientId, accessTokenDO.getClientId())) {
            return false;
        }
        // Delete again
        return oauth2TokenService.removeAccessToken(accessToken) != null;
    }

}
