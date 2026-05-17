package cn.iocoder.yudao.framework.web.core.filter;

import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.web.config.WebProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Filters for API requests such as /admin-API, /app-API, etc.
 *
 * @author Yudao Source Code
 */
@RequiredArgsConstructor
public abstract class ApiRequestFilter extends OncePerRequestFilter {

 protected final WebProperties webProperties;

 @Override
 protected boolean shouldNotFilter(HttpServletRequest request) {
        // Filter only the addresses requested by the API
 String apiUri = request.getRequestURI().substring(request.getContextPath().length());
 return !StrUtil.startWithAny(apiUri, webProperties.getAdminApi().getPrefix(), webProperties.getAppApi().getPrefix());
 }

}
