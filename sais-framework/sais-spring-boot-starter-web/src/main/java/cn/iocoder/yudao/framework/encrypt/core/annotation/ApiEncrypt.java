package cn.iocoder.yudao.framework.encrypt.core.annotation;

import java.lang.annotation.*;

/**
 * HTTP API encryption and decryption annotations
 */
@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiEncrypt {

 /**
     * Whether to decrypt request parameters, default true
 */
 boolean request() default true;

 /**
     * Whether to encrypt the response result, default true
 */
 boolean response() default true;

}
