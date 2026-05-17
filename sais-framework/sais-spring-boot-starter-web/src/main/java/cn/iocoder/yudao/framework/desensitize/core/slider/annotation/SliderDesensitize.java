package cn.iocoder.yudao.framework.desensitize.core.slider.annotation;

import cn.iocoder.yudao.framework.desensitize.core.base.annotation.DesensitizeBy;
import cn.iocoder.yudao.framework.desensitize.core.slider.handler.DefaultDesensitizationHandler;
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Sliding desensitization notes
 *
 * @author gaibu
 */
@Documented
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@DesensitizeBy(handler = DefaultDesensitizationHandler.class)
public @interface SliderDesensitize {

 /**
     * Suffix reserved length
 */
 int suffixKeep() default 0;

 /**
     * Replacement rules will retain the prefix and suffix and replace them all with replacer
 *
     * For example: prefixKeep = 1; suffixKeep = 2; replacer = "*";
     * Original string 123456
     * After desensitization 1***56
 */
 String replacer() default "*";

 /**
     * Prefix reserved length
 */
 int prefixKeep() default 0;

 /**
     * Whether to disable desensitization
 *
     * Support Spring EL expressions, skip desensitization if true is returned
 */
 String disable() default "";

}
