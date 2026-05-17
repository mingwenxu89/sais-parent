package cn.iocoder.yudao.framework.ratelimiter.core.aop;

import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.exception.enums.GlobalErrorCodeConstants;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.ratelimiter.core.annotation.RateLimiter;
import cn.iocoder.yudao.framework.ratelimiter.core.keyresolver.RateLimiterKeyResolver;
import cn.iocoder.yudao.framework.ratelimiter.core.redis.RateLimiterRedisDAO;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;

/**
 * Intercept methods declared with the {@link RateLimiter} annotation to implement current limiting operations.
 *
 * @author Yudao Source Code
 */
@Aspect
@Slf4j
public class RateLimiterAspect {

 /**
     * RateLimiterKeyResolver collection
 */
 private final Map<Class<? extends RateLimiterKeyResolver>, RateLimiterKeyResolver> keyResolvers;

 private final RateLimiterRedisDAO rateLimiterRedisDAO;

 public RateLimiterAspect(List<RateLimiterKeyResolver> keyResolvers, RateLimiterRedisDAO rateLimiterRedisDAO) {
 this.keyResolvers = CollectionUtils.convertMap(keyResolvers, RateLimiterKeyResolver::getClass);
 this.rateLimiterRedisDAO = rateLimiterRedisDAO;
 }

 @Before("@annotation(rateLimiter)")
 public void beforePointCut(JoinPoint joinPoint, RateLimiter rateLimiter) {
        // Get the RateLimiterKeyResolver object
 RateLimiterKeyResolver keyResolver = keyResolvers.get(rateLimiter.keyResolver());
        Assert.notNull(keyResolver, "The corresponding RateLimiterKeyResolver cannot be found");
        // Parse Key
 String key = keyResolver.resolver(joinPoint, rateLimiter);

        // Get 1 current limit
 boolean success = rateLimiterRedisDAO.tryAcquire(key,
 rateLimiter.count(), rateLimiter.time(), rateLimiter.timeUnit());
 if (!success) {
            log.info("[beforePointCut][Method ({}) Parameters ({}) requested too frequently]", joinPoint.getSignature().toString(), joinPoint.getArgs());
 String message = StrUtil.blankToDefault(rateLimiter.message(),
 GlobalErrorCodeConstants.TOO_MANY_REQUESTS.getMsg());
 throw new ServiceException(GlobalErrorCodeConstants.TOO_MANY_REQUESTS.getCode(), message);
 }
 }

}

