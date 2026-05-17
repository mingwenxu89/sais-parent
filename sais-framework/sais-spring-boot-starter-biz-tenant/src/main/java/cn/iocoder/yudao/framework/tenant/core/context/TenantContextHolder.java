package cn.iocoder.yudao.framework.tenant.core.context;

import cn.iocoder.yudao.framework.common.enums.DocumentEnum;
import com.alibaba.ttl.TransmittableThreadLocal;

/**
 * Multi-tenant context holder
 *
 * @author Yudao Source Code
 */
public class TenantContextHolder {

 /**
     * Current tenant ID
 */
 private static final ThreadLocal<Long> TENANT_ID = new TransmittableThreadLocal<>();

 /**
     * Whether to ignore tenants
 */
 private static final ThreadLocal<Boolean> IGNORE = new TransmittableThreadLocal<>();

 /**
     * Get tenant ID
 *
     * @return Tenant number
 */
 public static Long getTenantId() {
 return TENANT_ID.get();
 }

 /**
     * Obtain a tenant ID. If it does not exist, a NullPointerException is thrown.
 *
     * @return Tenant number
 */
 public static Long getRequiredTenantId() {
 Long tenantId = getTenantId();
 if (tenantId == null) {
            throw new NullPointerException("TenantContextHolder No tenant ID exists! Reference documents:"
 + DocumentEnum.TENANT.getUrl());
 }
 return tenantId;
 }

 public static void setTenantId(Long tenantId) {
 TENANT_ID.set(tenantId);
 }

 public static void setIgnore(Boolean ignore) {
 IGNORE.set(ignore);
 }

 /**
     * Whether to currently ignore tenants
 *
     * @return Ignore or not
 */
 public static boolean isIgnore() {
 return Boolean.TRUE.equals(IGNORE.get());
 }

 public static void clear() {
 TENANT_ID.remove();
 IGNORE.remove();
 }

}
