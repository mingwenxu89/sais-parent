package cn.iocoder.yudao.framework.desensitize.core.slider.handler;

import cn.iocoder.yudao.framework.desensitize.core.slider.annotation.MobileDesensitize;

/**
 * Desensitization handler for {@link MobileDesensitize}
 *
 * @author gaibu
 */
public class MobileDesensitization extends AbstractSliderDesensitizationHandler<MobileDesensitize> {

 @Override
 Integer getPrefixKeep(MobileDesensitize annotation) {
 return annotation.prefixKeep();
 }

 @Override
 Integer getSuffixKeep(MobileDesensitize annotation) {
 return annotation.suffixKeep();
 }

 @Override
 String getReplacer(MobileDesensitize annotation) {
 return annotation.replacer();
 }

}
