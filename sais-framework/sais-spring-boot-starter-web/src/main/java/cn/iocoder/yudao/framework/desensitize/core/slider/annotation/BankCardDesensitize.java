package cn.iocoder.yudao.framework.desensitize.core.slider.annotation;

import cn.iocoder.yudao.framework.desensitize.core.base.annotation.DesensitizeBy;
import cn.iocoder.yudao.framework.desensitize.core.slider.handler.BankCardDesensitization;
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Bank card number
 *
 * @author gaibu
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@DesensitizeBy(handler = BankCardDesensitization.class)
public @interface BankCardDesensitize {

 /**
     * Prefix reserved length
 */
 int prefixKeep() default 6;

 /**
     * Suffix reserved length
 */
 int suffixKeep() default 2;

 /**
     * Replacement rule, bank card number; for example: 9988002866797031 after desensitization is 998800********31
 */
 String replacer() default "*";

 /**
     * Whether to disable desensitization
 *
     * Support Spring EL expressions, skip desensitization if true is returned
 */
 String disable() default "";

}
