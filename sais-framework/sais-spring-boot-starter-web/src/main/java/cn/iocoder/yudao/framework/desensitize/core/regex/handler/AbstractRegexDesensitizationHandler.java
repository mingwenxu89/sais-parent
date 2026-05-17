package cn.iocoder.yudao.framework.desensitize.core.regex.handler;

import cn.iocoder.yudao.framework.common.util.spring.SpringExpressionUtils;
import cn.iocoder.yudao.framework.desensitize.core.base.handler.DesensitizationHandler;

import java.lang.annotation.Annotation;

/**
 * Regular expression desensitization processor abstract class, which has implemented common methods
 *
 * @author gaibu
 */
public abstract class AbstractRegexDesensitizationHandler<T extends Annotation>
 implements DesensitizationHandler<T> {

 @Override
 public String desensitize(String origin, T annotation) {
        // 1. Determine whether to disable desensitization
 Object disable = SpringExpressionUtils.parseExpression(getDisable(annotation));
 if (Boolean.TRUE.equals(disable)) {
 return origin;
 }

        // 2. Perform desensitization
 String regex = getRegex(annotation);
 String replacer = getReplacer(annotation);
 return origin.replaceAll(regex, replacer);
 }

 /**
     * Get the regex parameter on the annotation
 *
     * @param annotation Annotation information
     * @return regular expression
 */
 abstract String getRegex(T annotation);

 /**
     * Get the replacer parameter on the annotation
 *
     * @param annotation Annotation information
     * @return string to be replaced
 */
 abstract String getReplacer(T annotation);

}
