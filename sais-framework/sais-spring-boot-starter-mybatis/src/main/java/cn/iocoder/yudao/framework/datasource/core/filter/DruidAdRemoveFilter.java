package cn.iocoder.yudao.framework.datasource.core.filter;

import com.alibaba.druid.util.Utils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * DruID bottom ad filter
 *
 * @author Yudao Source Code
 */
public class DruidAdRemoveFilter extends OncePerRequestFilter {

 /**
     * path to common.js
 */
 private static final String COMMON_JS_ILE_PATH = "support/http/resources/js/common.js";

 @Override
 protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
 throws ServletException, IOException {
 chain.doFilter(request, response);
        // Reset the buffer, the response header will not be reset
 response.resetBuffer();
        // Get common.js
 String text = Utils.readFromResource(COMMON_JS_ILE_PATH);
        // Regularly replace the banner and remove the advertising information at the bottom
 text = text.replaceAll("<a.*?banner\"></a><br/>", "");
 text = text.replaceAll("powered.*?shrek.wang</a>", "");
 response.getWriter().write(text);
 }

}
