package cn.iocoder.yudao.module.system.service.tenant;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.tenant.core.context.TenantContextHolder;
import cn.iocoder.yudao.module.system.controller.admin.tenant.vo.tenant.TenantPageReqVO;
import cn.iocoder.yudao.module.system.controller.admin.tenant.vo.tenant.TenantSaveReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.tenant.TenantDO;
import cn.iocoder.yudao.module.system.service.tenant.handler.TenantInfoHandler;
import cn.iocoder.yudao.module.system.service.tenant.handler.TenantMenuHandler;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Set;

/**
 * Tenant Service API
 *
 * @author Yudao Source Code
 */
public interface TenantService {

    /**
     * Create tenant
     *
     * @param createReqVO Create information
     * @return ID
     */
    Long createTenant(@Valid TenantSaveReqVO createReqVO);

    /**
     * Update tenant
     *
     * @param updateReqVO Update information
     */
    void updateTenant(@Valid TenantSaveReqVO updateReqVO);

    /**
     * Update tenant's role menu
     *
     * @param tenantId Tenant ID
     * @param menuIds  menu ID array
     */
    void updateTenantRoleMenu(Long tenantId, Set<Long> menuIds);

    /**
     * Delete tenant
     *
     * @param id ID
     */
    void deleteTenant(Long id);

    /**
     * Delete tenants in batches
     *
     * @param ids IDed array
     */
    void deleteTenantList(List<Long> ids);

    /**
     * Get tenants
     *
     * @param id ID
     * @return Tenant
     */
    TenantDO getTenant(Long id);

    /**
     * Get tenant pagination
     *
     * @param pageReqVO Page query
     * @return Tenant pagination
     */
    PageResult<TenantDO> getTenantPage(TenantPageReqVO pageReqVO);

    /**
     * Get the tenant corresponding to the name
     *
     * @param name Tenant name
     * @return Tenant
     */
    TenantDO getTenantByName(String name);

    /**
     * Get the tenant corresponding to the domain name
     *
     * @param website domain name
     * @return Tenant
     */
    TenantDO getTenantByWebsite(String website);

    /**
     * Get the ID of tenants using a specified package
     *
     * @param packageId Tenant Package ID
     * @return ID of tenants
     */
    Long getTenantCountByPackageId(Long packageId);

    /**
     * Get the array of tenants using the specified package
     *
     * @param packageId Tenant Package ID
     * @return tenant array
     */
    List<TenantDO> getTenantListByPackageId(Long packageId);

    /**
     * Get a list of tenants with a specified status
     *
     * @param status Status
     * @return Tenant list
     */
    List<TenantDO> getTenantListByStatus(Integer status);

    /**
     * Perform tenant information processing logic
     * Where, the tenant ID is obtained from the {@link TenantContextHolder} context
     *
     * @param handler Processor
     */
    void handleTenantInfo(TenantInfoHandler handler);

    /**
     * Perform tenant menu processing logic
     * Where, the tenant ID is obtained from the {@link TenantContextHolder} context
     *
     * @param handler Processor
     */
    void handleTenantMenu(TenantMenuHandler handler);

    /**
     * Get all tenants
     *
     * @return Tenant ID array
     */
    List<Long> getTenantIdList();

    /**
     * Verify whether the tenant is legal
     *
     * @param id Tenant ID
     */
    void validTenant(Long id);

}
