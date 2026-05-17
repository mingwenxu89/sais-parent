package cn.iocoder.yudao.framework.desensitize.core.slider.annotation;

import cn.iocoder.yudao.framework.desensitize.core.base.annotation.DesensitizeBy;
import cn.iocoder.yudao.framework.desensitize.core.slider.handler.ChineseNameDesensitization;
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Chinese name
 *
 * @author gaibu
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@DesensitizeBy(handler = ChineseNameDesensitization.class)
public @interface ChineseNameDesensitize {

 /**
     * Prefix reserved length
 */
 int prefixKeep() default 1;

 /**
     * Suffix reserved length
 */
 int suffixKeep() default 0;

 /**
     * Replacement rules, Chinese name; for example: Liu Zihao is Liu** after desensitization
 */
 String replacer() default "*";

 /**
     * Whether to disable desensitization
 *
     * Support Spring EL expressions, skip desensitization if true is returned
 */
 String disable() default "";

}
