package cn.iocoder.yudao.framework.tenant.core.service;

import java.util.List;

/**
 * Tenant framework Service interface, defined to obtain tenant information
 *
 * @author Yudao Source Code
 */
public interface TenantFrameworkService {

 /**
     * Get all tenants
 *
     * @return Tenant number array
 */
 List<Long> getTenantIds();

 /**
     * Verify whether the tenant is legal
 *
     * @param id Tenant number
 */
 void validTenant(Long id);

}
