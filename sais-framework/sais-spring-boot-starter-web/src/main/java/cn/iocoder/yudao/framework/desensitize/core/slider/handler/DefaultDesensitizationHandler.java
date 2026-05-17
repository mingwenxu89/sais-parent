package cn.iocoder.yudao.framework.desensitize.core.slider.handler;

import cn.iocoder.yudao.framework.desensitize.core.slider.annotation.SliderDesensitize;

/**
 * Desensitization handler for {@link SliderDesensitize}
 *
 * @author gaibu
 */
public class DefaultDesensitizationHandler extends AbstractSliderDesensitizationHandler<SliderDesensitize> {

 @Override
 Integer getPrefixKeep(SliderDesensitize annotation) {
 return annotation.prefixKeep();
 }

 @Override
 Integer getSuffixKeep(SliderDesensitize annotation) {
 return annotation.suffixKeep();
 }

 @Override
 String getReplacer(SliderDesensitize annotation) {
 return annotation.replacer();
 }

}
