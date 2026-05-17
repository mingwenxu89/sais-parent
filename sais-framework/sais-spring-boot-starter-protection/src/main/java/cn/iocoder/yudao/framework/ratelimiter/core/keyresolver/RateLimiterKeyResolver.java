package cn.iocoder.yudao.framework.ratelimiter.core.keyresolver;

import cn.iocoder.yudao.framework.ratelimiter.core.annotation.RateLimiter;
import org.aspectj.lang.JoinPoint;

/**
 * Current-limiting Key parser interface
 *
 * @author Yudao Source Code
 */
public interface RateLimiterKeyResolver {

    /**
     * Parse a Key
     *
     * @param rateLimiter Current limiting annotation
     * @param joinPoint AOP aspects
     * @return Key
     */
    String resolver(JoinPoint joinPoint, RateLimiter rateLimiter);

}
