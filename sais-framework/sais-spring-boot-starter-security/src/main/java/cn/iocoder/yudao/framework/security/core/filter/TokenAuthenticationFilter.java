package cn.iocoder.yudao.framework.security.core.filter;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.biz.system.oauth2.OAuth2TokenCommonApi;
import cn.iocoder.yudao.framework.common.biz.system.oauth2.dto.OAuth2AccessTokenCheckRespDTO;
import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.servlet.ServletUtils;
import cn.iocoder.yudao.framework.security.config.SecurityProperties;
import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.framework.web.core.handler.GlobalExceptionHandler;
import cn.iocoder.yudao.framework.web.core.util.WebFrameworkUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Token filter, verify the validity of token
 * After the verification is passed, the {@link LoginUser} information is obtained and added to the Spring Security context.
 *
 * @author Yudao Source Code
 */
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

 private final SecurityProperties securityProperties;

 private final GlobalExceptionHandler globalExceptionHandler;

 private final OAuth2TokenCommonApi oauth2TokenApi;

 @Override
 @SuppressWarnings("NullableProblems")
 protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
 throws ServletException, IOException {
 String token = SecurityFrameworkUtils.obtainAuthorization(request,
 securityProperties.getTokenHeader(), securityProperties.getTokenParameter());
 if (StrUtil.isNotEmpty(token)) {
 Integer userType = WebFrameworkUtils.getLoginUserType(request);
 try {
                // 1.1 Build login user based on token
 LoginUser loginUser = buildLoginUserByToken(token, userType);
                // 1.2 Simulate Login function to facilitate daily development and debugging
 if (loginUser == null) {
 loginUser = mockLoginUser(request, token, userType);
 }

                // 2. Set current user
 if (loginUser != null) {
 SecurityFrameworkUtils.setLoginUser(loginUser, request);
 }
 } catch (Throwable ex) {
 CommonResult<?> result = globalExceptionHandler.allExceptionHandler(request, ex);
 ServletUtils.writeJSON(response, result);
 return;
 }
 }

        // continue filter chain
 chain.doFilter(request, response);
 }

 private LoginUser buildLoginUserByToken(String token, Integer userType) {
 try {
 OAuth2AccessTokenCheckRespDTO accessToken = oauth2TokenApi.checkAccessToken(token);
 if (accessToken == null) {
 return null;
 }
            // User type does not match, no permissions
            // Note: Only /admin-API/* and /app-API/* have userType, and the user type needs to be compared.
            // Similar to WebSocket's /ws/* connection address, there is no need to compare the user type.
 if (userType != null
 && ObjectUtil.notEqual(accessToken.getUserType(), userType)) {
                throw new AccessDeniedException("Wrong user type");
 }
            // Build login user
 return new LoginUser().setId(accessToken.getUserId()).setUserType(accessToken.getUserType())
                    .setInfo(accessToken.getUserInfo()) // additional user information
.setTenantId(accessToken.getTenantId()).setScopes(accessToken.getScopes())
.setExpiresTime(accessToken.getExpiresTime());
 } catch (ServiceException serviceException) {
            // When the token verification fails, considering that some interfaces do not require login, null can be returned directly.
 return null;
 }
 }

 /**
     * Simulate logged-in users to facilitate daily development and debugging
 *
     * Note that in an online environment, this function must be turned off! ! !
 *
     * @param request ask
     * @param token Simulated token, in the format {@link SecurityProperties#getMockSecret()} + user ID
     * @param userType User type
     * @return Impersonated LoginUser
 */
 private LoginUser mockLoginUser(HttpServletRequest request, String token, Integer userType) {
 if (!securityProperties.getMockEnable()) {
 return null;
 }
        // Must start with mockSecret
 if (!token.startsWith(securityProperties.getMockSecret())) {
 return null;
 }
        // Build a simulated user
 Long userId = Long.valueOf(token.substring(securityProperties.getMockSecret().length()));
 return new LoginUser().setId(userId).setUserType(userType)
.setTenantId(WebFrameworkUtils.getTenantId(request));
 }

}
