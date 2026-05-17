package cn.iocoder.yudao.framework.tenant.core.web;

import cn.iocoder.yudao.framework.tenant.core.context.TenantContextHolder;
import cn.iocoder.yudao.framework.web.core.util.WebFrameworkUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Multi-tenant Context Web Filter
 * Parse the tenant-ID in the request header and add it to {@link TenantContextHolder} so that subsequent DB and other operations can obtain the tenant ID.
 *
 * @author Yudao Source Code
 */
public class TenantContextWebFilter extends OncePerRequestFilter {

 @Override
 protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
 throws ServletException, IOException {
        // set up
 Long tenantId = WebFrameworkUtils.getTenantId(request);
 if (tenantId != null) {
 TenantContextHolder.setTenantId(tenantId);
 }
 try {
 chain.doFilter(request, response);
 } finally {
            // clean up
 TenantContextHolder.clear();
 }
 }

}
