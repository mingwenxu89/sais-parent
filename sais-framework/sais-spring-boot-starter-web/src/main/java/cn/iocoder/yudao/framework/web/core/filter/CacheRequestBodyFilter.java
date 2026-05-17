package cn.iocoder.yudao.framework.web.core.filter;

import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.util.servlet.ServletUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Request Body cache Filter to achieve its repeatable reading
 *
 * @author Yudao Source Code
 */
public class CacheRequestBodyFilter extends OncePerRequestFilter {

 /**
     * URIs to exclude
 *
     * 1. Exclude Spring Boot Admin related requests to avoID exceptions caused by client connection interruption.
     * For example: <a href="https://github.com/YunaiV/ruoyi-vue-pro/issues/795">795 ISSUE</a>
 */
 private static final String[] IGNORE_URIS = {"/admin/", "/actuator/"};

 @Override
 protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
 throws IOException, ServletException {
 filterChain.doFilter(new CacheRequestBodyWrapper(request), response);
 }

 @Override
 protected boolean shouldNotFilter(HttpServletRequest request) {
        // 1. Verify whether it is an excluded URL
 String requestURI = request.getRequestURI();
 if (StrUtil.startWithAny(requestURI, IGNORE_URIS)) {
 return true;
 }

        // 2. Only process JSON request content
 return !ServletUtils.isJsonRequest(request);
 }

}
