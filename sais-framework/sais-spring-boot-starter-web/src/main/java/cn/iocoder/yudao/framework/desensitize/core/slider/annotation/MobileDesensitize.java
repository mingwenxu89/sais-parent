package cn.iocoder.yudao.framework.desensitize.core.slider.annotation;

import cn.iocoder.yudao.framework.desensitize.core.base.annotation.DesensitizeBy;
import cn.iocoder.yudao.framework.desensitize.core.slider.handler.MobileDesensitization;
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Phone number
 *
 * @author gaibu
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@DesensitizeBy(handler = MobileDesensitization.class)
public @interface MobileDesensitize {

 /**
     * Prefix reserved length
 */
 int prefixKeep() default 3;

 /**
     * Suffix reserved length
 */
 int suffixKeep() default 4;

 /**
     * Replacement rule, mobile phone number; for example: 13248765917 after desensitization is 132****5917
 */
 String replacer() default "*";

 /**
     * Whether to disable desensitization
 *
     * Support Spring EL expressions, skip desensitization if true is returned
 */
 String disable() default "";

}
