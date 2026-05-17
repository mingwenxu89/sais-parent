package cn.iocoder.yudao.framework.tenant.core.util;

import cn.iocoder.yudao.framework.tenant.core.context.TenantContextHolder;

import java.util.Map;
import java.util.concurrent.Callable;

import static cn.iocoder.yudao.framework.web.core.util.WebFrameworkUtils.HEADER_TENANT_ID;

/**
 * Multi-tenant Util
 *
 * @author Yudao Source Code
 */
public class TenantUtils {

 /**
     * Use the specified tenant to execute the corresponding logic
 *
     * Note that if the tenant is currently ignored, it will be forced to not ignore the tenant.
     * Of course, after the execution is completed, it will still be restored.
 *
     * @param tenantId Tenant number
     * @param runnable logic
 */
 public static void execute(Long tenantId, Runnable runnable) {
 Long oldTenantId = TenantContextHolder.getTenantId();
 Boolean oldIgnore = TenantContextHolder.isIgnore();
 try {
 TenantContextHolder.setTenantId(tenantId);
 TenantContextHolder.setIgnore(false);
            // execution logic
 runnable.run();
 } finally {
 TenantContextHolder.setTenantId(oldTenantId);
 TenantContextHolder.setIgnore(oldIgnore);
 }
 }

 /**
     * Use the specified tenant to execute the corresponding logic
 *
     * Note that if the tenant is currently ignored, it will be forced to not ignore the tenant.
     * Of course, after the execution is completed, it will still be restored.
 *
     * @param tenantId Tenant number
     * @param callable logic
     * @return result
 */
 public static <V> V execute(Long tenantId, Callable<V> callable) {
 Long oldTenantId = TenantContextHolder.getTenantId();
 Boolean oldIgnore = TenantContextHolder.isIgnore();
 try {
 TenantContextHolder.setTenantId(tenantId);
 TenantContextHolder.setIgnore(false);
            // execution logic
 return callable.call();
 } catch (Exception e) {
 throw new RuntimeException(e);
 } finally {
 TenantContextHolder.setTenantId(oldTenantId);
 TenantContextHolder.setIgnore(oldIgnore);
 }
 }

 /**
     * Ignore the tenant and execute the corresponding logic
 *
     * @param runnable logic
 */
 public static void executeIgnore(Runnable runnable) {
 Boolean oldIgnore = TenantContextHolder.isIgnore();
 try {
 TenantContextHolder.setIgnore(true);
            // execution logic
 runnable.run();
 } finally {
 TenantContextHolder.setIgnore(oldIgnore);
 }
 }

 /**
     * Ignore the tenant and execute the corresponding logic
 *
     * @param callable logic
     * @return result
 */
 public static <V> V executeIgnore(Callable<V> callable) {
 Boolean oldIgnore = TenantContextHolder.isIgnore();
 try {
 TenantContextHolder.setIgnore(true);
            // execution logic
 return callable.call();
 } catch (Exception e) {
 throw new RuntimeException(e);
 } finally {
 TenantContextHolder.setIgnore(oldIgnore);
 }
 }

 /**
     * Add the multi-tenant ID to the header
 *
     * @param headers HTTP request headers
     * @param tenantId Tenant number
 */
 public static void addTenantHeader(Map<String, String> headers, Long tenantId) {
 if (tenantId != null) {
 headers.put(HEADER_TENANT_ID, tenantId.toString());
 }
 }

}
