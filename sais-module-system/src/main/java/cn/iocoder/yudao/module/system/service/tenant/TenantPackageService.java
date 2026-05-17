package cn.iocoder.yudao.module.system.service.tenant;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.system.controller.admin.tenant.vo.packages.TenantPackagePageReqVO;
import cn.iocoder.yudao.module.system.controller.admin.tenant.vo.packages.TenantPackageSaveReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.tenant.TenantPackageDO;

import jakarta.validation.Valid;
import java.util.List;

/**
 * Tenant package service API
 *
 * @author Yudao Source Code
 */
public interface TenantPackageService {

    /**
     * Create a tenant package
     *
     * @param createReqVO Create information
     * @return ID
     */
    Long createTenantPackage(@Valid TenantPackageSaveReqVO createReqVO);

    /**
     * Update tenant package
     *
     * @param updateReqVO Update information
     */
    void updateTenantPackage(@Valid TenantPackageSaveReqVO updateReqVO);

    /**
     * Delete tenant package
     *
     * @param id ID
     */
    void deleteTenantPackage(Long id);

    /**
     * Delete tenant packages in batches
     *
     * @param ids IDed array
     */
    void deleteTenantPackageList(List<Long> ids);

    /**
     * Get a tenant package
     *
     * @param id ID
     * @return Tenant package
     */
    TenantPackageDO getTenantPackage(Long id);

    /**
     * Get Tenant Packages Pagination
     *
     * @param pageReqVO Page query
     * @return Tenant Package Pagination
     */
    PageResult<TenantPackageDO> getTenantPackagePage(TenantPackagePageReqVO pageReqVO);

    /**
     * Verify tenant package
     *
     * @param id ID
     * @return Tenant package
     */
    TenantPackageDO validTenantPackage(Long id);

    /**
     * Get a list of tenant packages in a specified status
     *
     * @param status Status
     * @return Tenant package
     */
    List<TenantPackageDO> getTenantPackageListByStatus(Integer status);

}
