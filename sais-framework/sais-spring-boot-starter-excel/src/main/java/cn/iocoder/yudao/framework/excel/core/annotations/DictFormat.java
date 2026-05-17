package cn.iocoder.yudao.framework.excel.core.annotations;

import java.lang.annotation.*;

/**
 * Dictionary formatting
 *
 * Achieve formatting the value of dictionary data into the label of dictionary data
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface DictFormat {

 /**
     * For example, SysDictTypeConstants, InfDictTypeConstants
 *
     * @return dictionary type
 */
 String value();

}
