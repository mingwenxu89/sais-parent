package cn.iocoder.yudao.framework.dict.validation;

import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.dict.core.DictFrameworkUtils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;

public class InDictValidator implements ConstraintValidator<InDict, Object> {

 private String dictType;

 @Override
 public void initialize(InDict annotation) {
 this.dictType = annotation.type();
 }

 @Override
 public boolean isValid(Object value, ConstraintValidatorContext context) {
        // When it is empty, no verification is performed by default, which means it is considered passed.
 if (value == null) {
 return true;
 }
        // Verification passed
 final List<String> values = DictFrameworkUtils.getDictDataValueList(dictType);
 boolean match = values.stream().anyMatch(v -> StrUtil.equalsIgnoreCase(v, value.toString()));
 if (match) {
 return true;
 }

        // Verification failed, custom prompt statement
        context.disableDefaultConstraintViolation(); // Disable the default message value
 context.buildConstraintViolationWithTemplate(
 context.getDefaultConstraintMessageTemplate().replaceAll("\\{value}", values.toString())
        ).addConstraintViolation(); // Add error message again
 return false;
 }

}

