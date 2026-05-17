package cn.iocoder.yudao.framework.common.validation;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.PhoneUtil;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TelephoneValidator implements ConstraintValidator<Telephone, String> {

 @Override
 public void initialize(Telephone annotation) {
 }

 @Override
 public boolean isValid(String value, ConstraintValidatorContext context) {
        // If the mobile phone number is empty, it will not be verified by default, that is, the verification is passed.
 if (CharSequenceUtil.isEmpty(value)) {
 return true;
 }
        // Verify mobile phone
 return PhoneUtil.isTel(value) || PhoneUtil.isPhone(value);
 }

}
