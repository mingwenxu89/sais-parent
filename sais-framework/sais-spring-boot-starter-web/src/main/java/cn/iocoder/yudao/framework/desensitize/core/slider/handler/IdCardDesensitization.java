package cn.iocoder.yudao.framework.desensitize.core.slider.handler;

import cn.iocoder.yudao.framework.desensitize.core.slider.annotation.IdCardDesensitize;

/**
 * Desensitization handler for {@link IdCardDesensitize}
 *
 * @author gaibu
 */
public class IdCardDesensitization extends AbstractSliderDesensitizationHandler<IdCardDesensitize> {
 @Override
 Integer getPrefixKeep(IdCardDesensitize annotation) {
 return annotation.prefixKeep();
 }

 @Override
 Integer getSuffixKeep(IdCardDesensitize annotation) {
 return annotation.suffixKeep();
 }

 @Override
 String getReplacer(IdCardDesensitize annotation) {
 return annotation.replacer();
 }

}
