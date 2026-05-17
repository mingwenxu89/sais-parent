package cn.iocoder.yudao.framework.desensitize.core.regex.annotation;

import cn.iocoder.yudao.framework.desensitize.core.base.annotation.DesensitizeBy;
import cn.iocoder.yudao.framework.desensitize.core.regex.handler.DefaultRegexDesensitizationHandler;
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Regular desensitization annotation
 *
 * @author gaibu
 */
@Documented
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@DesensitizeBy(handler = DefaultRegexDesensitizationHandler.class)
public @interface RegexDesensitize {

 /**
     * Regular expression to match (default matches all)
 */
 String regex() default "^[\\s\\S]*$";

 /**
     * Replacement rules will replace all matched strings with replacer
 *
     * For example: regex=123; replacer=******
     * Original string 123456789
     * String after desensitization ******456789
 */
 String replacer() default "******";

 /**
     * Whether to disable desensitization
 *
     * Support Spring EL expressions, skip desensitization if true is returned
 */
 String disable() default "";

}
