package cn.iocoder.yudao.framework.apilog.core.annotation;

import cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Access log annotations
 *
 * @author Yudao Source Code
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiAccessLog {

    // ========== Switch field ==========

 /**
     * Whether to record access logs
 */
 boolean enable() default true;
 /**
     * Whether to record request parameters
 *
     * The default record is mainly due to the fact that the request data is generally not large. Can be manually set to false to turn off
 */
 boolean requestEnable() default true;
 /**
     * Whether to record response results
 *
     * Not recorded by default, mainly because the response data may be relatively large. Can be manually set to true to open
 */
 boolean responseEnable() default false;
 /**
     * Sensitive parameter array
 *
     * After adding, the request parameters and response results will not record this parameter.
 */
 String[] sanitizeKeys() default {};

    // ========== Module fields ==========

 /**
     * Operation module
 *
     * When empty, attempts to read the {@link io.swagger.v3.oas.annotations.tags.Tag#name()} property
 */
 String operateModule() default "";
 /**
     * Operation name
 *
     * When empty, attempts to read the {@link io.swagger.v3.oas.annotations.Operation#summary()} property
 */
 String operateName() default "";
 /**
     * Operation classification
 *
     * Not actually an array, because enumerations cannot set null as a default value
 */
 OperateTypeEnum[] operateType() default {};

}
