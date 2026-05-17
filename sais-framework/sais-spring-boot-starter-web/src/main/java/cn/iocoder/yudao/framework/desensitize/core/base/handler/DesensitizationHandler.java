package cn.iocoder.yudao.framework.desensitize.core.base.handler;

import cn.hutool.core.util.ReflectUtil;

import java.lang.annotation.Annotation;

/**
 * Desensitization processor interface
 *
 * @author gaibu
 */
public interface DesensitizationHandler<T extends Annotation> {

 /**
     * Desensitization
 *
     * @param origin raw string
     * @param annotation Annotation information
     * @return Desensitized string
 */
 String desensitize(String origin, T annotation);

 /**
     * Whether to disable desensitized Spring EL expressions
 *
     * If true is returned, desensitization is skipped
 *
     * @param annotation Annotation information
     * @return Whether to disable desensitized Spring EL expressions
 */
 default String getDisable(T annotation) {
        // Convention: The default is the enable() attribute. If it does not match, the subclass overrides
 try {
 return (String) ReflectUtil.invoke(annotation, "disable");
 } catch (Exception ex) {
 return "";
 }
 }

}
