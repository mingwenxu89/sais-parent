package cn.iocoder.yudao.framework.security.core.util;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.framework.web.core.util.WebFrameworkUtils;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Collections;

/**
 * Security service tools
 *
 * @author Yudao Source Code
 */
public class SecurityFrameworkUtils {

 /**
     * HEADER The prefix of the authentication header value
 */
 public static final String AUTHORIZATION_BEARER = "Bearer";

 private SecurityFrameworkUtils() {}

 /**
     * Obtain the authentication token from the request
 *
     * @param request ask
     * @param headerName Header name corresponding to the authentication Token
     * @param parameterName Parameter name corresponding to the authentication Token
     * @return Authentication Token
 */
 public static String obtainAuthorization(HttpServletRequest request,
 String headerName, String parameterName) {
        // 1. Obtain Token. Priority: Header > Parameter
 String token = request.getHeader(headerName);
 if (StrUtil.isEmpty(token)) {
 token = request.getParameter(parameterName);
 }
 if (!StringUtils.hasText(token)) {
 return null;
 }
        // 2. Remove the Bearer in the Token
 int index = token.indexOf(AUTHORIZATION_BEARER + " ");
 return index >= 0 ? token.substring(index + 7).trim(): token;
 }

 /**
     * Get current certification information
 *
     * @return Certification information
 */
 public static Authentication getAuthentication() {
 SecurityContext context = SecurityContextHolder.getContext();
 if (context == null) {
 return null;
 }
 return context.getAuthentication();
 }

 /**
     * Get current user
 *
     * @return current user
 */
 @Nullable
 public static LoginUser getLoginUser() {
 Authentication authentication = getAuthentication();
 if (authentication == null) {
 return null;
 }
 return authentication.getPrincipal() instanceof LoginUser ? (LoginUser) authentication.getPrincipal(): null;
 }

 /**
     * Get the current user's number, from the context
 *
     * @return User ID
 */
 @Nullable
 public static Long getLoginUserId() {
 LoginUser loginUser = getLoginUser();
 return loginUser != null ? loginUser.getId(): null;
 }

 /**
     * Get the current user's nickname, from the context
 *
     * @return Nick name
 */
 @Nullable
 public static String getLoginUserNickname() {
 LoginUser loginUser = getLoginUser();
 return loginUser != null ? MapUtil.getStr(loginUser.getInfo(), LoginUser.INFO_KEY_NICKNAME): null;
 }

 /**
     * Get the current user's department ID, from the context
 *
     * @return Department number
 */
 @Nullable
 public static Long getLoginUserDeptId() {
 LoginUser loginUser = getLoginUser();
 return loginUser != null ? MapUtil.getLong(loginUser.getInfo(), LoginUser.INFO_KEY_DEPT_ID): null;
 }

 /**
     * Set current user
 *
     * @param loginUser Login user
     * @param request ask
 */
 public static void setLoginUser(LoginUser loginUser, HttpServletRequest request) {
        // Create Authentication and set it to the context
 Authentication authentication = buildAuthentication(loginUser, request);
 SecurityContextHolder.getContext().setAuthentication(authentication);

        // Additional settings are added to the request, and the user ID can be obtained by using APIAccessLogFilter;
        // The reason is that Spring Security's Filter is behind APIAccessLogFilter. When it records the access log, the online context no longer has user ID and other information.
 if (request != null) {
 WebFrameworkUtils.setLoginUserId(request, loginUser.getId());
 WebFrameworkUtils.setLoginUserType(request, loginUser.getUserType());
 }
 }

 private static Authentication buildAuthentication(LoginUser loginUser, HttpServletRequest request) {
        // Create UsernamePasswordAuthenticationToken object
 UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
 loginUser, null, Collections.emptyList());
 authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
 return authenticationToken;
 }

 /**
     * Whether to conditionally skip permission verification, including data permissions and function permissions
 *
     * @return Whether to skip
 */
 public static boolean skipPermissionCheck() {
 LoginUser loginUser = getLoginUser();
 if (loginUser == null) {
 return false;
 }
 if (loginUser.getVisitTenantId() == null) {
 return false;
 }
        // Important: When accessing across tenants, permission verification cannot be performed.
 return ObjUtil.notEqual(loginUser.getVisitTenantId(), loginUser.getTenantId());
 }

}
