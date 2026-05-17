package cn.iocoder.yudao.framework.xss.core.filter;

import cn.iocoder.yudao.framework.xss.config.XssProperties;
import cn.iocoder.yudao.framework.xss.core.clean.XssCleaner;
import lombok.AllArgsConstructor;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Xss filter
 *
 * @author Yudao Source Code
 */
@AllArgsConstructor
public class XssFilter extends OncePerRequestFilter {

 /**
     * property
 */
 private final XssProperties properties;
 /**
     * path matcher
 */
 private final PathMatcher pathMatcher;

 private final XssCleaner xssCleaner;

 @Override
 protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
 throws IOException, ServletException {
 filterChain.doFilter(new XssRequestWrapper(request, xssCleaner), response);
 }

 @Override
 protected boolean shouldNotFilter(HttpServletRequest request) {
        // If off, no filtering
 if (!properties.isEnable()) {
 return true;
 }

        // If no filtering is required, no filtering will be performed.
 String uri = request.getRequestURI();
 return properties.getExcludeUrls().stream().anyMatch(excludeUrl -> pathMatcher.match(excludeUrl, uri));
 }

}
