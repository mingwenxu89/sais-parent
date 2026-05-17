package cn.iocoder.yudao.framework.idempotent.core.annotation;

import cn.iocoder.yudao.framework.idempotent.core.keyresolver.impl.DefaultIdempotentKeyResolver;
import cn.iocoder.yudao.framework.idempotent.core.keyresolver.IdempotentKeyResolver;
import cn.iocoder.yudao.framework.idempotent.core.keyresolver.impl.ExpressionIdempotentKeyResolver;
import cn.iocoder.yudao.framework.idempotent.core.keyresolver.impl.UserIdempotentKeyResolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * Idempotent annotation
 *
 * @author Yudao Source Code
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Idempotent {

 /**
     * Idempotent timeout, default is 1 second
 *
     * Note that if the execution time exceeds this, the request will still come in
 */
 int timeout() default 1;
 /**
     * Time unit, default is SECONDS seconds
 */
 TimeUnit timeUnit() default TimeUnit.SECONDS;

 /**
     * Prompt information, prompts during execution
 */
    String message() default "Repeat request, please try again later";

 /**
     * Key parser used
 *
     * @see DefaultIdempotentKeyResolver global level
     * @see UserIdempotentKeyResolver user level
     * @see ExpressionIdempotentKeyResolver Custom expression, calculated through {@link #keyArg()}
 */
 Class<? extends IdempotentKeyResolver> keyResolver() default DefaultIdempotentKeyResolver.class;
 /**
     * Key parameter used
 */
 String keyArg() default "";

 /**
     * Delete Key when an exception occurs
 *
     * Question: Why do we need to delete the Key when an exception occurs?
     * Answer: When an exception occurs, it means that an error has occurred in the business. At this time, the Key needs to be deleted to prevent the next request from being executed normally.
 *
     * Question: Why don't you delete the Key when deleteWhenSuccess is executed successfully?
     * Answer: In this case, it is essentially a distributed lock, and it is recommended to use the @Lock4j annotation
 */
 boolean deleteKeyWhenException() default true;

}
