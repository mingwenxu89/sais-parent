package cn.iocoder.yudao.framework.dict.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({
 ElementType.METHOD,
 ElementType.FIELD,
 ElementType.ANNOTATION_TYPE,
 ElementType.CONSTRUCTOR,
 ElementType.PARAMETER,
 ElementType.TYPE_USE
})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
 validatedBy = {InDictValidator.class, InDictCollectionValidator.class}
)
public @interface InDict {

 /**
 * shu judictionary type
 */
 String type();

 String message() default "must be in the specified range {value}";

 Class<?>[] groups() default {};

 Class<? extends Payload>[] payload() default {};

}
