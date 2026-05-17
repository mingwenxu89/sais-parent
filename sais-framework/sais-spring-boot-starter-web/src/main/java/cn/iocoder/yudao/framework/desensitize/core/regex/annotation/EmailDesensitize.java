package cn.iocoder.yudao.framework.desensitize.core.regex.annotation;

import cn.iocoder.yudao.framework.desensitize.core.base.annotation.DesensitizeBy;
import cn.iocoder.yudao.framework.desensitize.core.regex.handler.EmailDesensitizationHandler;
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Email desensitization notes
 *
 * @author gaibu
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@DesensitizeBy(handler = EmailDesensitizationHandler.class)
public @interface EmailDesensitize {

 /**
     * Matching regular expression
 */
 String regex() default "(^.)[^@]*(@.*$)";

 /**
     * Replacement rules, mailbox;
 *
     * For example: example@gmail.com becomes e****@gmail.com after desensitization
 */
 String replacer() default "$1****$2";

 /**
     * Whether to disable desensitization
 *
     * Support Spring EL expressions, skip desensitization if true is returned
 */
 String disable() default "";

}
