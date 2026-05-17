package cn.iocoder.yudao.framework.common.validation;

import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.util.validation.ValidationUtils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MobileValidator implements ConstraintValidator<Mobile, String> {

 @Override
 public void initialize(Mobile annotation) {
 }

 @Override
 public boolean isValid(String value, ConstraintValidatorContext context) {
        // If the mobile phone number is empty, it will not be verified by default, that is, the verification is passed.
 if (StrUtil.isEmpty(value)) {
 return true;
 }
        // Verify mobile phone
 return ValidationUtils.isMobile(value);
 }

}
