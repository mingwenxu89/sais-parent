package cn.iocoder.yudao.framework.tenant.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Multi-tenant configuration
 *
 * @author Yudao Source Code
 */
@ConfigurationProperties(prefix = "yudao.tenant")
@Data
public class TenantProperties {

 /**
     * Is the tenant enabled?
 */
 private static final Boolean ENABLE_DEFAULT = true;

 /**
     * Whether to turn on
 */
 private Boolean enable = ENABLE_DEFAULT;

 /**
     * Multi-tenant requests need to be ignored
 *
     * By default, each request requires a tenant-ID request header. However, some requests do not need to be brought, such as SMS callbacks, payment callbacks, etc. Open API!
 */
 private Set<String> ignoreUrls = new HashSet<>();

 /**
     * Requests for cross (switch) tenant access need to be ignored
 *
     * The reason is: some interfaces access personal information, which cannot be obtained across tenants!
 */
 private Set<String> ignoreVisitUrls = Collections.emptySet();

 /**
     * Multi-tenant tables need to be ignored
 *
     * That is to say, all tables have multi-tenancy enabled by default, so remember to add the corresponding tenant_ID field.
 */
 private Set<String> ignoreTables = Collections.emptySet();

 /**
     * Spring Cache cache that needs to be ignored for multi-tenancy
 *
     * That is, by default, all caches have multi-tenancy enabled, so remember to add the corresponding tenant_ID field.
 */
 private Set<String> ignoreCaches = Collections.emptySet();

}
