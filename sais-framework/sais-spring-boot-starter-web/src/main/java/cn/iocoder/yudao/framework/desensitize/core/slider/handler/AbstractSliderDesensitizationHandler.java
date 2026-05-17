package cn.iocoder.yudao.framework.desensitize.core.slider.handler;

import cn.iocoder.yudao.framework.common.util.spring.SpringExpressionUtils;
import cn.iocoder.yudao.framework.desensitize.core.base.handler.DesensitizationHandler;

import java.lang.annotation.Annotation;

/**
 * Sliding desensitization processor abstract class, which has implemented common methods
 *
 * @author gaibu
 */
public abstract class AbstractSliderDesensitizationHandler<T extends Annotation>
        implements DesensitizationHandler<T> {

    @Override
    public String desensitize(String origin, T annotation) {
        // 1. Determine whether to disable desensitization
        Object disable = SpringExpressionUtils.parseExpression(getDisable(annotation));
        if (Boolean.TRUE.equals(disable)) {
            return origin;
        }

        // 2. Perform desensitization
        int prefixKeep = getPrefixKeep(annotation);
        int suffixKeep = getSuffixKeep(annotation);
        String replacer = getReplacer(annotation);
        int length = origin.length();
        int interval = length - prefixKeep - suffixKeep;

        // Case 1: The length of the original string is less than or equal to the length of the prefix and suffix retained strings, then all the original strings are replaced
        if (interval <= 0) {
            return buildReplacerByLength(replacer, length);
        }

        // Case 2: The length of the original string is greater than the length of the prefix and suffix reserved strings, and the middle string is replaced.
        return origin.substring(0, prefixKeep) +
                buildReplacerByLength(replacer, interval) +
                origin.substring(prefixKeep + interval);
    }

    /**
     * Loop to construct replacement characters based on length
     *
     * @param replacer replacement character
     * @param length length
     * @return Replacement characters after construction
     */
    private String buildReplacerByLength(String replacer, int length) {
        return replacer.repeat(length);
    }

    /**
     * Prefix reserved length
     *
     * @param annotation Annotation information
     * @return Prefix reserved length
     */
    abstract Integer getPrefixKeep(T annotation);

    /**
     * Suffix reserved length
     *
     * @param annotation Annotation information
     * @return Suffix reserved length
     */
    abstract Integer getSuffixKeep(T annotation);

    /**
     * replacement character
     *
     * @param annotation Annotation information
     * @return replacement character
     */
    abstract String getReplacer(T annotation);

}
