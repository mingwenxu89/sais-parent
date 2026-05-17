package cn.iocoder.yudao.framework.ratelimiter.core.keyresolver.impl;

import cn.hutool.crypto.SecureUtil;
import cn.iocoder.yudao.framework.common.util.string.StrUtils;
import cn.iocoder.yudao.framework.ratelimiter.core.annotation.RateLimiter;
import cn.iocoder.yudao.framework.ratelimiter.core.keyresolver.RateLimiterKeyResolver;
import cn.iocoder.yudao.framework.web.core.util.WebFrameworkUtils;
import org.aspectj.lang.JoinPoint;

/**
 * User-level current-limiting Key parser uses method name + method parameters + userID + userType to assemble a Key
 *
 * In order to avoID the Key being too long, use MD5 for "compression"
 *
 * @author Yudao Source Code
 */
public class UserRateLimiterKeyResolver implements RateLimiterKeyResolver {

 @Override
 public String resolver(JoinPoint joinPoint, RateLimiter rateLimiter) {
 String methodName = joinPoint.getSignature().toString();
 String argsStr = StrUtils.joinMethodArgs(joinPoint);
 Long userId = WebFrameworkUtils.getLoginUserId();
 Integer userType = WebFrameworkUtils.getLoginUserType();
 return SecureUtil.md5(methodName + argsStr + userId + userType);
 }

}
