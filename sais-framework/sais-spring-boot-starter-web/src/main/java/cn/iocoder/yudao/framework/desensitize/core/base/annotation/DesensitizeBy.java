package cn.iocoder.yudao.framework.desensitize.core.base.annotation;

import cn.iocoder.yudao.framework.desensitize.core.base.handler.DesensitizationHandler;
import cn.iocoder.yudao.framework.desensitize.core.base.serializer.StringDesensitizeSerializer;
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Top-level desensitization annotation, custom annotation needs to use this annotation
 *
 * @author gaibu
 */
@Documented
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside // This annotation is a meta-annotation for all other jackson annotations. Annotations marked with this annotation indicate that they are part of the jackson annotation.
@JsonSerialize(using = StringDesensitizeSerializer.class) // Specify serializer
public @interface DesensitizeBy {

 /**
     * Desensitization processor
 */
 @SuppressWarnings("rawtypes")
 Class<? extends DesensitizationHandler> handler();

}
