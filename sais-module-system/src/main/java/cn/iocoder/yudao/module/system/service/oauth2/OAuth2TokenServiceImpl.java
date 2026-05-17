package cn.iocoder.yudao.module.system.service.oauth2;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.exception.enums.GlobalErrorCodeConstants;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.date.DateUtils;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.framework.tenant.core.context.TenantContextHolder;
import cn.iocoder.yudao.framework.tenant.core.util.TenantUtils;
import cn.iocoder.yudao.module.system.controller.admin.oauth2.vo.token.OAuth2AccessTokenPageReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.oauth2.OAuth2AccessTokenDO;
import cn.iocoder.yudao.module.system.dal.dataobject.oauth2.OAuth2ClientDO;
import cn.iocoder.yudao.module.system.dal.dataobject.oauth2.OAuth2RefreshTokenDO;
import cn.iocoder.yudao.module.system.dal.dataobject.user.AdminUserDO;
import cn.iocoder.yudao.module.system.dal.mysql.oauth2.OAuth2AccessTokenMapper;
import cn.iocoder.yudao.module.system.dal.mysql.oauth2.OAuth2RefreshTokenMapper;
import cn.iocoder.yudao.module.system.dal.redis.oauth2.OAuth2AccessTokenRedisDAO;
import cn.iocoder.yudao.module.system.service.user.AdminUserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception0;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertSet;

/**
 * OAuth2.0 Token Service implementation class
 *
 * @author Yudao Source Code
 */
@Service
public class OAuth2TokenServiceImpl implements OAuth2TokenService {

    @Resource
    private OAuth2AccessTokenMapper oauth2AccessTokenMapper;
    @Resource
    private OAuth2RefreshTokenMapper oauth2RefreshTokenMapper;

    @Resource
    private OAuth2AccessTokenRedisDAO oauth2AccessTokenRedisDAO;

    @Resource
    private OAuth2ClientService oauth2ClientService;
    @Resource
    @Lazy // Lazy loading to avoid circular dependencies
    private AdminUserService adminUserService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OAuth2AccessTokenDO createAccessToken(Long userId, Integer userType, String clientId, List<String> scopes) {
        OAuth2ClientDO clientDO = oauth2ClientService.validOAuthClientFromCache(clientId);
        // Create refresh token
        OAuth2RefreshTokenDO refreshTokenDO = createOAuth2RefreshToken(userId, userType, clientDO, scopes);
        // Create access token
        return createOAuth2AccessToken(refreshTokenDO, clientDO);
    }

    @Override
    @Transactional(noRollbackFor = ServiceException.class)
    public OAuth2AccessTokenDO refreshAccessToken(String refreshToken, String clientId) {
        // Query access token
        OAuth2RefreshTokenDO refreshTokenDO = oauth2RefreshTokenMapper.selectByRefreshToken(refreshToken);
        if (refreshTokenDO == null) {
            throw exception0(GlobalErrorCodeConstants.BAD_REQUEST.getCode(), "Invalid refresh token");
        }

        // Verify Client matches
        OAuth2ClientDO clientDO = oauth2ClientService.validOAuthClientFromCache(clientId);
        if (ObjectUtil.notEqual(clientId, refreshTokenDO.getClientId())) {
            throw exception0(GlobalErrorCodeConstants.BAD_REQUEST.getCode(), "Incorrect client ID for refresh token");
        }

        // Remove related access tokens
        List<OAuth2AccessTokenDO> accessTokenDOs = oauth2AccessTokenMapper.selectListByRefreshToken(refreshToken);
        if (CollUtil.isNotEmpty(accessTokenDOs)) {
            oauth2AccessTokenMapper.deleteByIds(convertSet(accessTokenDOs, OAuth2AccessTokenDO::getId));
            oauth2AccessTokenRedisDAO.deleteList(convertSet(accessTokenDOs, OAuth2AccessTokenDO::getAccessToken));
        }

        // Delete the refresh token if it has expired
        if (DateUtils.isExpired(refreshTokenDO.getExpiresTime())) {
            oauth2RefreshTokenMapper.deleteById(refreshTokenDO.getId());
            throw exception0(GlobalErrorCodeConstants.UNAUTHORIZED.getCode(), "Refresh token has expired");
        }

        // Create access token
        return createOAuth2AccessToken(refreshTokenDO, clientDO);
    }

    @Override
    public OAuth2AccessTokenDO getAccessToken(String accessToken) {
        // Get it from Redis first
        OAuth2AccessTokenDO accessTokenDO = oauth2AccessTokenRedisDAO.get(accessToken);
        if (accessTokenDO != null) {
            return accessTokenDO;
        }

        // Unable to obtain, obtain access token from MySQL
        accessTokenDO = oauth2AccessTokenMapper.selectByAccessToken(accessToken);
        if (accessTokenDO == null) {
            // Special: Get refresh token from MySQL. Reason: Solve the problem of inconvenient refreshing of access token in some scenarios
            // For example, the building block report only allows the passing of token, but does not allow the passing of refresh_token, resulting in the inability to refresh the access token.
            // For another example, the token of the frontend WebSocket directly follows the URL, and refresh_token cannot be passed.
            OAuth2RefreshTokenDO refreshTokenDO = oauth2RefreshTokenMapper.selectByRefreshToken(accessToken);
            if (refreshTokenDO != null && !DateUtils.isExpired(refreshTokenDO.getExpiresTime())) {
                accessTokenDO = convertToAccessToken(refreshTokenDO);
            }
        }

        // If it exists in MySQL, write it to Redis
        if (accessTokenDO != null && !DateUtils.isExpired(accessTokenDO.getExpiresTime())) {
            oauth2AccessTokenRedisDAO.set(accessTokenDO);
        }
        return accessTokenDO;
    }

    @Override
    public OAuth2AccessTokenDO checkAccessToken(String accessToken) {
        OAuth2AccessTokenDO accessTokenDO = getAccessToken(accessToken);
        if (accessTokenDO == null) {
            throw exception0(GlobalErrorCodeConstants.UNAUTHORIZED.getCode(), "Access token does not exist");
        }
        if (DateUtils.isExpired(accessTokenDO.getExpiresTime())) {
            throw exception0(GlobalErrorCodeConstants.UNAUTHORIZED.getCode(), "Access token has expired");
        }
        return accessTokenDO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OAuth2AccessTokenDO removeAccessToken(String accessToken) {
        // Delete access token
        OAuth2AccessTokenDO accessTokenDO = oauth2AccessTokenMapper.selectByAccessToken(accessToken);
        if (accessTokenDO == null) {
            return null;
        }
        oauth2AccessTokenMapper.deleteById(accessTokenDO.getId());
        oauth2AccessTokenRedisDAO.delete(accessToken);
        // Delete refresh token
        oauth2RefreshTokenMapper.deleteByRefreshToken(accessTokenDO.getRefreshToken());
        return accessTokenDO;
    }

    @Override
    public void removeAccessToken(Long userId, Integer userType) {
        List<OAuth2AccessTokenDO> accessTokens = oauth2AccessTokenMapper.selectListByUserIdAndUserType(userId, userType);
        if (CollUtil.isEmpty(accessTokens)) {
            return;
        }
        accessTokens.forEach(accessToken -> {
            // Delete access token
            oauth2AccessTokenMapper.deleteById(accessToken.getId());
            oauth2AccessTokenRedisDAO.delete(accessToken.getAccessToken());
            // Delete refresh token
            oauth2RefreshTokenMapper.deleteByRefreshToken(accessToken.getRefreshToken());
        });
    }

    @Override
    public PageResult<OAuth2AccessTokenDO> getAccessTokenPage(OAuth2AccessTokenPageReqVO reqVO) {
        return oauth2AccessTokenMapper.selectPage(reqVO);
    }

    private OAuth2AccessTokenDO createOAuth2AccessToken(OAuth2RefreshTokenDO refreshTokenDO, OAuth2ClientDO clientDO) {
        OAuth2AccessTokenDO accessTokenDO = new OAuth2AccessTokenDO().setAccessToken(generateAccessToken())
                .setUserId(refreshTokenDO.getUserId()).setUserType(refreshTokenDO.getUserType())
                .setUserInfo(buildUserInfo(refreshTokenDO.getUserId(), refreshTokenDO.getUserType()))
                .setClientId(clientDO.getClientId()).setScopes(refreshTokenDO.getScopes())
                .setRefreshToken(refreshTokenDO.getRefreshToken())
                .setExpiresTime(LocalDateTime.now().plusSeconds(clientDO.getAccessTokenValiditySeconds()));
        // Prioritize getting the tenant ID from refreshToken to avoid tenantId being null when ThreadLocal is contaminated.
        // Possibly related issues: https://t.zsxq.com/JIi5G
        Long tenantId = refreshTokenDO.getTenantId();
        if (tenantId == null) {
            tenantId = TenantContextHolder.getTenantId();
        }
        accessTokenDO.setTenantId(tenantId);
        oauth2AccessTokenMapper.insert(accessTokenDO);
        // Record to Redis
        oauth2AccessTokenRedisDAO.set(accessTokenDO);
        return accessTokenDO;
    }

    private OAuth2RefreshTokenDO createOAuth2RefreshToken(Long userId, Integer userType, OAuth2ClientDO clientDO, List<String> scopes) {
        OAuth2RefreshTokenDO refreshToken = new OAuth2RefreshTokenDO().setRefreshToken(generateRefreshToken())
                .setUserId(userId).setUserType(userType)
                .setClientId(clientDO.getClientId()).setScopes(scopes)
                .setExpiresTime(LocalDateTime.now().plusSeconds(clientDO.getRefreshTokenValiditySeconds()));
        // Explicitly set tenantId to avoid MyBatis Plus automatic injection being suppressed and causing tenant_id to be null in the @TenantIgnore scenario
        // Cannot be called in a chain: setTenantId is defined in the parent class TenantBaseDO and returns TenantBaseDO instead of the subclass type.
        refreshToken.setTenantId(TenantContextHolder.getTenantId());
        oauth2RefreshTokenMapper.insert(refreshToken);
        return refreshToken;
    }

    private OAuth2AccessTokenDO convertToAccessToken(OAuth2RefreshTokenDO refreshTokenDO) {
        OAuth2AccessTokenDO accessTokenDO = BeanUtils.toBean(refreshTokenDO, OAuth2AccessTokenDO.class)
                .setAccessToken(refreshTokenDO.getRefreshToken());
        TenantUtils.execute(refreshTokenDO.getTenantId(),
                        () -> accessTokenDO.setUserInfo(buildUserInfo(refreshTokenDO.getUserId(), refreshTokenDO.getUserType())));
        return accessTokenDO;
    }

    /**
     * Load user information to facilitate {@link cn.iocoder.yudao.framework.security.core.LoginUser} to obtain nickname, department and other information
     *
     * @param userId User ID
     * @param userType User type
     * @return User information
     */
    private Map<String, String> buildUserInfo(Long userId, Integer userType) {
        if (userId == null || userId <= 0) {
            return Collections.emptyMap();
        }
        if (userType.equals(UserTypeEnum.ADMIN.getValue())) {
            AdminUserDO user = adminUserService.getUser(userId);
            return MapUtil.builder(LoginUser.INFO_KEY_NICKNAME, user.getNickname())
                    .put(LoginUser.INFO_KEY_DEPT_ID, StrUtil.toStringOrNull(user.getDeptId())).build();
        } else if (userType.equals(UserTypeEnum.MEMBER.getValue())) {
            // Note: Currently, Member is not read, but it can be implemented as needed.
            return Collections.emptyMap();
        }
        throw new IllegalArgumentException("Unknown user type:" + userType);
    }

    private static String generateAccessToken() {
        return IdUtil.fastSimpleUUID();
    }

    private static String generateRefreshToken() {
        return IdUtil.fastSimpleUUID();
    }

}
