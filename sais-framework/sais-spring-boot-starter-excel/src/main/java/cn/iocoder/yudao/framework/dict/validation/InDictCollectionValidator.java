package cn.iocoder.yudao.framework.dict.validation;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.yudao.framework.dict.core.DictFrameworkUtils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Collection;
import java.util.List;

public class InDictCollectionValidator implements ConstraintValidator<InDict, Collection<?>> {

 private String dictType;

 @Override
 public void initialize(InDict annotation) {
 this.dictType = annotation.type();
 }

 @Override
 public boolean isValid(Collection<?> list, ConstraintValidatorContext context) {
        // When it is empty, no verification is performed by default, which means it is considered passed.
 if (CollUtil.isEmpty(list)) {
 return true;
 }
        // All verification passed
 List<String> dbValues = DictFrameworkUtils.getDictDataValueList(dictType);
 boolean match = list.stream().allMatch(v -> dbValues.stream()
.anyMatch(dbValue -> dbValue.equalsIgnoreCase(v.toString())));
 if (match) {
 return true;
 }

        // Verification failed, custom prompt statement
        context.disableDefaultConstraintViolation(); // Disable the default message value
 context.buildConstraintViolationWithTemplate(
 context.getDefaultConstraintMessageTemplate().replaceAll("\\{value}", dbValues.toString())
        ).addConstraintViolation(); // Add error message again
 return false;
 }

}

