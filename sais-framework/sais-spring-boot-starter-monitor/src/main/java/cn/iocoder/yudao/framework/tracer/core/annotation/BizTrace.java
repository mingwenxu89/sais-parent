package cn.iocoder.yudao.framework.tracer.core.annotation;

import java.lang.annotation.*;

/**
 * Annotation for printing business ID / business type
 *
 * When using, you need to configure the SkyWalking OAP Server's application.yaml file,
 * modify the SW_SEARCHABLE_TAG_KEYS configuration item by adding biz.type and biz.id,
 * then restart the SkyWalking OAP Server.
 *
 * @author mashu
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface BizTrace {

    /**
     * Business ID tag name
     */
    String ID_TAG = "biz.id";
    /**
     * Business type tag name
     */
    String TYPE_TAG = "biz.type";

    /**
     * @return operation name
     */
    String operationName() default "";

    /**
     * @return business ID
     */
    String id();

    /**
     * @return business type
     */
    String type();

}
