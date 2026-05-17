package cn.iocoder.yudao.framework.desensitize.core.regex.handler;

import cn.iocoder.yudao.framework.desensitize.core.regex.annotation.RegexDesensitize;

/**
 * Regular desensitization processor for {@link RegexDesensitize}
 *
 * @author gaibu
 */
public class DefaultRegexDesensitizationHandler extends AbstractRegexDesensitizationHandler<RegexDesensitize> {

 @Override
 String getRegex(RegexDesensitize annotation) {
 return annotation.regex();
 }

 @Override
 String getReplacer(RegexDesensitize annotation) {
 return annotation.replacer();
 }

 @Override
 public String getDisable(RegexDesensitize annotation) {
 return annotation.disable();
 }

}
