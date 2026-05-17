package cn.iocoder.yudao.framework.datapermission.core.annotation;

import cn.iocoder.yudao.framework.datapermission.core.rule.DataPermissionRule;

import java.lang.annotation.*;

/**
 * Data permission annotation
 * It can be declared on a class or method to identify the data permission rules used.
 *
 * @author Yudao Source Code
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataPermission {

 /**
     * Whether the current class or method has data permission enabled
     * Even if the @DataPermission annotation is not added, it is enabled by default
     * Can be disabled by setting enable to false
 */
 boolean enable() default true;

 /**
     * Array of effective data permission rules, with priority higher than {@link #excludeRules()}
 */
 Class<? extends DataPermissionRule>[] includeRules() default {};

 /**
     * Array of excluded data permission rules, lowest priority
 */
 Class<? extends DataPermissionRule>[] excludeRules() default {};

}
