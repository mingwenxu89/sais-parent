package cn.iocoder.yudao.framework.desensitize.core.slider.annotation;

import cn.iocoder.yudao.framework.desensitize.core.base.annotation.DesensitizeBy;
import cn.iocoder.yudao.framework.desensitize.core.slider.handler.CarLicenseDesensitization;
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * license plate number
 *
 * @author gaibu
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@DesensitizeBy(handler = CarLicenseDesensitization.class)
public @interface CarLicenseDesensitize {

 /**
     * Prefix reserved length
 */
 int prefixKeep() default 3;

 /**
     * Suffix reserved length
 */
 int suffixKeep() default 1;

 /**
     * Replacement rules, license plate number; for example: Guangdong A66666 After desensitization, it is Guangdong A6***6
 */
 String replacer() default "*";

 /**
     * Whether to disable desensitization
 *
     * Support Spring EL expressions, skip desensitization if true is returned
 */
 String disable() default "";

}
