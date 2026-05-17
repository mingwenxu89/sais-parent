package cn.iocoder.yudao.framework.security.core.handler;

import cn.iocoder.yudao.framework.common.exception.enums.GlobalErrorCodeConstants;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.framework.common.util.servlet.ServletUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.ExceptionTranslationFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import static cn.iocoder.yudao.framework.common.exception.enums.GlobalErrorCodeConstants.FORBIDDEN;

/**
 * When accessing a URL resource that requires authentication and has been authenticated (logged in) but does not have permission, the {@link GlobalErrorCodeConstants#FORBIDDEN} error code is returned.
 *
 * Supplement: Spring Security calls the current class through the {@link ExceptionTranslationFilter#handleAccessDeniedException(HttpServletRequest, HttpServletResponse, FilterChain, AccessDeniedException)} method.
 *
 * @author Yudao Source Code
 */
@Slf4j
@SuppressWarnings("JavadocReference")
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e)
            throws IOException, ServletException {
        // The reason for printing warn is to merge warn from time to time to see if there is any malicious damage.
        log.warn("[commence][When accessing URL({}), user({}) has insufficient permissions]", request.getRequestURI(),
                SecurityFrameworkUtils.getLoginUserId(), e);
        // Return 403
        ServletUtils.writeJSON(response, CommonResult.error(FORBIDDEN));
    }

}
