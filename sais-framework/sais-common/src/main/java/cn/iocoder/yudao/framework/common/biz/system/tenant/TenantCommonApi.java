package cn.iocoder.yudao.framework.common.biz.system.tenant;

import java.util.List;

/**
 * Multi-tenant API interface
 *
 * @author Yudao Source Code
 */
public interface TenantCommonApi {

 /**
     * Get all tenants
 *
     * @return Tenant number array
 */
 List<Long> getTenantIdList();

 /**
     * Verify whether the tenant is legal
 *
     * @param id Tenant number
 */
 void validateTenant(Long id);

}
