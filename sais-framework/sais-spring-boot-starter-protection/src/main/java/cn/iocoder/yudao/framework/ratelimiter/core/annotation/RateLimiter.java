package cn.iocoder.yudao.framework.ratelimiter.core.annotation;

import cn.iocoder.yudao.framework.common.exception.enums.GlobalErrorCodeConstants;
import cn.iocoder.yudao.framework.idempotent.core.keyresolver.impl.ExpressionIdempotentKeyResolver;
import cn.iocoder.yudao.framework.ratelimiter.core.keyresolver.RateLimiterKeyResolver;
import cn.iocoder.yudao.framework.ratelimiter.core.keyresolver.impl.ClientIpRateLimiterKeyResolver;
import cn.iocoder.yudao.framework.ratelimiter.core.keyresolver.impl.DefaultRateLimiterKeyResolver;
import cn.iocoder.yudao.framework.ratelimiter.core.keyresolver.impl.ServerNodeRateLimiterKeyResolver;
import cn.iocoder.yudao.framework.ratelimiter.core.keyresolver.impl.UserRateLimiterKeyResolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * Current limiting annotation
 *
 * @author Yudao Source Code
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimiter {

 /**
     * Current limiting time, default is 1 second
 */
 int time() default 1;
 /**
     * Time unit, default is SECONDS seconds
 */
 TimeUnit timeUnit() default TimeUnit.SECONDS;

 /**
     * Number of current limits
 */
 int count() default 100;

 /**
     * Prompt information, prompt that the request is too fast
 *
 * @see GlobalErrorCodeConstants#TOO_MANY_REQUESTS
 */
    String message() default ""; // When empty, use TOO_MANY_REQUESTS error message

 /**
     * Key parser used
 *
     * @see DefaultRateLimiterKeyResolver global level
     * @see UserRateLimiterKeyResolver user ID level
     * @see ClientIpRateLimiterKeyResolver User IP Level
     * @see ServerNodeRateLimiterKeyResolver Server Node level
     * @see ExpressionIdempotentKeyResolver Custom expression, calculated through {@link #keyArg()}
 */
 Class<? extends RateLimiterKeyResolver> keyResolver() default DefaultRateLimiterKeyResolver.class;
 /**
     * Key parameter used
 */
 String keyArg() default "";

}
