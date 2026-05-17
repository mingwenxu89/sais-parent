package cn.iocoder.yudao.framework.web.core.filter;

import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.servlet.ServletUtils;
import cn.iocoder.yudao.framework.web.core.util.WebFrameworkUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static cn.iocoder.yudao.framework.common.exception.enums.GlobalErrorCodeConstants.DEMO_DENY;

/**
 * Demo Filter, prohibiting users from initiating write operations to avoID affecting test data
 *
 * @author Yudao Source Code
 */
public class DemoFilter extends OncePerRequestFilter {

 @Override
 protected boolean shouldNotFilter(HttpServletRequest request) {
 String method = request.getMethod();
        return !StrUtil.equalsAnyIgnoreCase(method, "POST", "PUT", "DELETE")  // During write operations, filtering is not performed
                || WebFrameworkUtils.getLoginUserId(request) == null; // If you are not a logged-in user, no filtering will be performed.
 }

 @Override
 protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) {
        // Return the result of DEMO_DENY directly. That is, the request does not continue
 ServletUtils.writeJSON(response, CommonResult.error(DEMO_DENY));
 }

}
