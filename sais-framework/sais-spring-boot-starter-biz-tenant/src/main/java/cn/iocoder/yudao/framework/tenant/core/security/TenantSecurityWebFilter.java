package cn.iocoder.yudao.framework.tenant.core.security;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.yudao.framework.common.exception.enums.GlobalErrorCodeConstants;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.servlet.ServletUtils;
import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.framework.tenant.config.TenantProperties;
import cn.iocoder.yudao.framework.tenant.core.context.TenantContextHolder;
import cn.iocoder.yudao.framework.tenant.core.service.TenantFrameworkService;
import cn.iocoder.yudao.framework.web.config.WebProperties;
import cn.iocoder.yudao.framework.web.core.filter.ApiRequestFilter;
import cn.iocoder.yudao.framework.web.core.handler.GlobalExceptionHandler;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import java.io.IOException;
import java.util.Objects;
import java.util.Set;

/**
 * Multi-tenant Security Web Filter
 * 1. If you are a logged-in user, verify whether you have permission to access the tenant to avoid unauthorized access.
 * 2. If the request does not carry the tenant ID, check whether it is an ignored URL, otherwise access will not be allowed.
 * 3. Verify that the tenant is legal, such as being disabled or expired
 *
 * @author Yudao Source Code
 */
@Slf4j
public class TenantSecurityWebFilter extends ApiRequestFilter {

    private final TenantProperties tenantProperties;

    /**
     * List of URLs that allow tenants to be ignored
     *
     * Purpose: Solve <a href="https://gitee.com/zhijiantianya/yudao-cloud/issues/ICUQL9">Modifying the configuration will cause @TenantIgnore Controller API filtering to fail</>
     */
    private final Set<String> ignoreUrls;

    private final AntPathMatcher pathMatcher;

    private final GlobalExceptionHandler globalExceptionHandler;
    private final TenantFrameworkService tenantFrameworkService;

    public TenantSecurityWebFilter(WebProperties webProperties,
                                   TenantProperties tenantProperties,
                                   Set<String> ignoreUrls,
                                   GlobalExceptionHandler globalExceptionHandler,
                                   TenantFrameworkService tenantFrameworkService) {
        super(webProperties);
        this.tenantProperties = tenantProperties;
        this.ignoreUrls = ignoreUrls;
        this.pathMatcher = new AntPathMatcher();
        this.globalExceptionHandler = globalExceptionHandler;
        this.tenantFrameworkService = tenantFrameworkService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        Long tenantId = TenantContextHolder.getTenantId();
        // 1. Verify that the logged-in user has permission to access the tenant to avoid unauthorized access.
        LoginUser user = SecurityFrameworkUtils.getLoginUser();
        if (user != null) {
            // If the tenant ID cannot be obtained, try to use the tenant ID of the logged in user.
            if (tenantId == null) {
                tenantId = user.getTenantId();
                TenantContextHolder.setTenantId(tenantId);
            // If the tenant ID is passed, the tenant ID will be compared to avoid override issues.
            } else if (!Objects.equals(user.getTenantId(), TenantContextHolder.getTenantId())) {
                log.error("[doFilterInternal][Tenant({}) User({}/{}) Unauthorized access to Tenant({}) URL({}/{})]",
                        user.getTenantId(), user.getId(), user.getUserType(),
                        TenantContextHolder.getTenantId(), request.getRequestURI(), request.getMethod());
                ServletUtils.writeJSON(response, CommonResult.error(GlobalErrorCodeConstants.FORBIDDEN.getCode(),
                        "You do not have permission to access data for this tenant"));
                return;
            }
        }

        // If the tenant's URL is not allowed, then verify whether the tenant is legal.
        if (!isIgnoreUrl(request)) {
            // 2. If the request does not include the tenant ID, access is not allowed.
            if (tenantId == null) {
                log.error("[doFilterInternal][URL({}/{}) did not pass tenant ID]", request.getRequestURI(), request.getMethod());
                ServletUtils.writeJSON(response, CommonResult.error(GlobalErrorCodeConstants.BAD_REQUEST.getCode(),
                        "The request tenant identifier was not passed. Please investigate."));
                return;
            }
            // 3. Verify that the tenant is legal, such as being disabled or expired
            try {
                tenantFrameworkService.validTenant(tenantId);
            } catch (Throwable ex) {
                CommonResult<?> result = globalExceptionHandler.allExceptionHandler(request, ex);
                ServletUtils.writeJSON(response, result);
                return;
            }
        } else { // If the URL of the tenant is allowed to be ignored, if the tenant ID is not passed, the tenant ID will be ignored by default to avoid error reporting.
            if (tenantId == null) {
                TenantContextHolder.setIgnore(true);
            }
        }

        // continue filtering
        chain.doFilter(request, response);
    }

    private boolean isIgnoreUrl(HttpServletRequest request) {
        String apiUri = request.getRequestURI().substring(request.getContextPath().length());
        // Fast matching, guaranteed performance
        if (CollUtil.contains(tenantProperties.getIgnoreUrls(), apiUri)
            || CollUtil.contains(ignoreUrls, apiUri)) {
            return true;
        }
        // Match Ant paths one by one
        for (String url : tenantProperties.getIgnoreUrls()) {
            if (pathMatcher.match(url, apiUri)) {
                return true;
            }
        }
        for (String url : ignoreUrls) {
            if (pathMatcher.match(url, apiUri)) {
                return true;
            }
        }
        return false;
    }

}
