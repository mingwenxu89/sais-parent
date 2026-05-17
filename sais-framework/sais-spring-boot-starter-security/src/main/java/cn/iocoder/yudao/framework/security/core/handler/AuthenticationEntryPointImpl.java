package cn.iocoder.yudao.framework.security.core.handler;

import cn.iocoder.yudao.framework.common.exception.enums.GlobalErrorCodeConstants;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.servlet.ServletUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.ExceptionTranslationFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static cn.iocoder.yudao.framework.common.exception.enums.GlobalErrorCodeConstants.UNAUTHORIZED;

/**
 * When accessing a URL resource that requires authentication, but you are not authenticated (logged in) at this time, the {@link GlobalErrorCodeConstants#UNAUTHORIZED} error code is returned, causing the front end to redirect to the login page.
 *
 * Supplement: Spring Security calls the current class through the {@link ExceptionTranslationFilter#sendStartAuthentication(HttpServletRequest, HttpServletResponse, FilterChain, AuthenticationException)} method.
 *
 * @author ruoyi
 */
@Slf4j
@SuppressWarnings("JavadocReference") // Ignore document reference error
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

 @Override
 public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) {
        log.debug("[commence][No login when accessing URL({})]", request.getRequestURI(), e);
        // Return 401
 ServletUtils.writeJSON(response, CommonResult.error(UNAUTHORIZED));
 }

}
