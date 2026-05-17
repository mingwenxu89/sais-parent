package cn.iocoder.yudao.framework.ratelimiter.core.keyresolver.impl;

import cn.hutool.crypto.SecureUtil;
import cn.iocoder.yudao.framework.common.util.servlet.ServletUtils;
import cn.iocoder.yudao.framework.common.util.string.StrUtils;
import cn.iocoder.yudao.framework.ratelimiter.core.annotation.RateLimiter;
import cn.iocoder.yudao.framework.ratelimiter.core.keyresolver.RateLimiterKeyResolver;
import org.aspectj.lang.JoinPoint;

/**
 * IP-level current-limiting Key parser, using method name + method parameters + IP, assembled into a Key
 *
 * In order to avoID the Key being too long, use MD5 for "compression"
 *
 * @author Yudao Source Code
 */
public class ClientIpRateLimiterKeyResolver implements RateLimiterKeyResolver {

 @Override
 public String resolver(JoinPoint joinPoint, RateLimiter rateLimiter) {
 String methodName = joinPoint.getSignature().toString();
 String argsStr = StrUtils.joinMethodArgs(joinPoint);
 String clientIp = ServletUtils.getClientIP();
 return SecureUtil.md5(methodName + argsStr + clientIp);
 }

}
