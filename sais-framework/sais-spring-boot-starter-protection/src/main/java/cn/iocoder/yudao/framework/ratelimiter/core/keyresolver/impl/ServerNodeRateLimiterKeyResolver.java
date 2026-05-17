package cn.iocoder.yudao.framework.ratelimiter.core.keyresolver.impl;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.system.SystemUtil;
import cn.iocoder.yudao.framework.common.util.string.StrUtils;
import cn.iocoder.yudao.framework.ratelimiter.core.annotation.RateLimiter;
import cn.iocoder.yudao.framework.ratelimiter.core.keyresolver.RateLimiterKeyResolver;
import org.aspectj.lang.JoinPoint;

/**
 * Server node-level current-limiting Key parser uses method name + method parameter + IP to assemble a Key
 *
 * In order to avoID the Key being too long, use MD5 for "compression"
 *
 * @author Yudao Source Code
 */
public class ServerNodeRateLimiterKeyResolver implements RateLimiterKeyResolver {

    @Override
    public String resolver(JoinPoint joinPoint, RateLimiter rateLimiter) {
        String methodName = joinPoint.getSignature().toString();
        String argsStr = StrUtils.joinMethodArgs(joinPoint);
        String serverNode = String.format("%s@%d", SystemUtil.getHostInfo().getAddress(), SystemUtil.getCurrentPID());
        return SecureUtil.md5(methodName + argsStr + serverNode);
    }

}