package cn.iocoder.yudao.framework.tenant.core.aop;

import cn.iocoder.yudao.framework.tenant.config.TenantProperties;

import java.lang.annotation.*;

/**
 * Ignore the tenant and mark the specified method not to perform automatic filtering of tenants
 *
 * Note that only DB scenes will be filtered, other scenes will not be filtered for the time being:
 * 1. Redis scenario: Because it is based on Key to implement multi-tenancy capabilities, it makes no sense to ignore it, unlike DB which is implemented by a column.
 * 2. MQ scenario: It’s a bit difficult to choose. Currently, you can manually add @TenantIgnore to the consumption method through Consumer to ignore it.
 *
 * special:
 * 1. If added to the Controller class, the URL is automatically added to {@link TenantProperties#getIgnoreUrls()}
 * 2. If added to the DO entity class, its corresponding table name is "equivalent to" automatically added to {@link TenantProperties#getIgnoreTables()}
 *
 * @author Yudao Source Code
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface TenantIgnore {

 /**
     * Whether to enable ignoring tenants, the default is true and enabled
 *
     * Supports Spring EL expressions. If true is returned, the condition is met and the tenant is ignored.
 */
 String enable() default "true";

}
