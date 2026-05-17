package cn.iocoder.yudao.framework.excel.core.annotations;

import java.lang.annotation.*;

/**
 * Add drop-down selection data to Excel column
 *
 * Among them, choose one of {@link #dictType()} and {@link #functionName()}
 *
 * @author HUIHUI
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ExcelColumnSelect {

 /**
     * @return dictionary type
 */
 String dictType() default "";

 /**
     * @return Get the method name of the drop-down data source
 */
 String functionName() default "";

}
