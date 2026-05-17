package cn.iocoder.yudao.framework.desensitize.core.slider.handler;

import cn.iocoder.yudao.framework.desensitize.core.slider.annotation.FixedPhoneDesensitize;

/**
 * Desensitization handler for {@link FixedPhoneDesensitize}
 *
 * @author gaibu
 */
public class FixedPhoneDesensitization extends AbstractSliderDesensitizationHandler<FixedPhoneDesensitize> {

 @Override
 Integer getPrefixKeep(FixedPhoneDesensitize annotation) {
 return annotation.prefixKeep();
 }

 @Override
 Integer getSuffixKeep(FixedPhoneDesensitize annotation) {
 return annotation.suffixKeep();
 }

 @Override
 String getReplacer(FixedPhoneDesensitize annotation) {
 return annotation.replacer();
 }

}
