package cn.iocoder.yudao.framework.signature.core.annotation;

import cn.iocoder.yudao.framework.common.exception.enums.GlobalErrorCodeConstants;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;


/**
 * HTTP API signature annotations
 *
 * @author Zhougang
 */
@Inherited
@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiSignature {

 /**
     * How long is the same request valid? Default 60 seconds
 */
 int timeout() default 60;

 /**
     * Time unit, default is SECONDS seconds
 */
 TimeUnit timeUnit() default TimeUnit.SECONDS;

    // ========================== Signature parameters ==========================

 /**
     * Prompt message, prompt for signature failure
 *
 * @see GlobalErrorCodeConstants#BAD_REQUEST
 */
    String message() default "Incorrect signature"; // When empty, use BAD_REQUEST error message

 /**
     * Signature field: appID application ID
 */
 String appId() default "appId";

 /**
     * Signature field: timestamp timestamp
 */
 String timestamp() default "timestamp";

 /**
     * Signature field: nonce random number, more than 10 digits
 */
 String nonce() default "nonce";

 /**
     * sign client signature
 */
 String sign() default "sign";

}
