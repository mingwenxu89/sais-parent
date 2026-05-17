package cn.iocoder.yudao.module.system.service.tenant;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.system.controller.admin.tenant.vo.packages.TenantPackagePageReqVO;
import cn.iocoder.yudao.module.system.controller.admin.tenant.vo.packages.TenantPackageSaveReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.tenant.TenantDO;
import cn.iocoder.yudao.module.system.dal.dataobject.tenant.TenantPackageDO;
import cn.iocoder.yudao.module.system.dal.mysql.tenant.TenantPackageMapper;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.google.common.annotations.VisibleForTesting;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.system.enums.ErrorCodeConstants.*;

/**
 * Tenant package Service implementation class
 *
 * @author Yudao Source Code
 */
@Service
@Validated
public class TenantPackageServiceImpl implements TenantPackageService {

    @Resource
    private TenantPackageMapper tenantPackageMapper;

    @Resource
    @Lazy // Avoid circular dependency errors
    private TenantService tenantService;

    @Override
    public Long createTenantPackage(TenantPackageSaveReqVO createReqVO) {
        // Verify whether the package name is duplicated
        validateTenantPackageNameUnique(null, createReqVO.getName());
        // Insert
        TenantPackageDO tenantPackage = BeanUtils.toBean(createReqVO, TenantPackageDO.class);
        tenantPackageMapper.insert(tenantPackage);
        // Return
        return tenantPackage.getId();
    }

    @Override
    @DSTransactional // Multiple data sources, using @DSTransactional Ensure local transactions and data source switching
    public void updateTenantPackage(TenantPackageSaveReqVO updateReqVO) {
        // Check existence
        TenantPackageDO tenantPackage = validateTenantPackageExists(updateReqVO.getId());
        // Verify whether the package name is duplicated
        validateTenantPackageNameUnique(updateReqVO.getId(), updateReqVO.getName());
        // Update
        TenantPackageDO updateObj = BeanUtils.toBean(updateReqVO, TenantPackageDO.class);
        tenantPackageMapper.updateById(updateObj);
        // Modify the menu for each tenant if the menu changes
        if (!CollUtil.isEqualList(tenantPackage.getMenuIds(), updateReqVO.getMenuIds())) {
            List<TenantDO> tenants = tenantService.getTenantListByPackageId(tenantPackage.getId());
            tenants.forEach(tenant -> tenantService.updateTenantRoleMenu(tenant.getId(), updateReqVO.getMenuIds()));
        }
    }

    @Override
    public void deleteTenantPackage(Long id) {
        // Check existence
        validateTenantPackageExists(id);
        // Check is in use
        validateTenantUsed(id);
        // Delete
        tenantPackageMapper.deleteById(id);
    }

    @Override
    public void deleteTenantPackageList(List<Long> ids) {
        // 1. Verify whether any tenant is using the package
        for (Long id : ids) {
            if (tenantService.getTenantCountByPackageId(id) > 0) {
                throw exception(TENANT_PACKAGE_USED);
            }
        }

        // 2. Batch deletion
        tenantPackageMapper.deleteByIds(ids);
    }

    private TenantPackageDO validateTenantPackageExists(Long id) {
        TenantPackageDO tenantPackage = tenantPackageMapper.selectById(id);
        if (tenantPackage == null) {
            throw exception(TENANT_PACKAGE_NOT_EXISTS);
        }
        return tenantPackage;
    }

    private void validateTenantUsed(Long id) {
        if (tenantService.getTenantCountByPackageId(id) > 0) {
            throw exception(TENANT_PACKAGE_USED);
        }
    }

    @Override
    public TenantPackageDO getTenantPackage(Long id) {
        return tenantPackageMapper.selectById(id);
    }

    @Override
    public PageResult<TenantPackageDO> getTenantPackagePage(TenantPackagePageReqVO pageReqVO) {
        return tenantPackageMapper.selectPage(pageReqVO);
    }

    @Override
    public TenantPackageDO validTenantPackage(Long id) {
        TenantPackageDO tenantPackage = tenantPackageMapper.selectById(id);
        if (tenantPackage == null) {
            throw exception(TENANT_PACKAGE_NOT_EXISTS);
        }
        if (tenantPackage.getStatus().equals(CommonStatusEnum.DISABLE.getStatus())) {
            throw exception(TENANT_PACKAGE_DISABLE, tenantPackage.getName());
        }
        return tenantPackage;
    }

    @Override
    public List<TenantPackageDO> getTenantPackageListByStatus(Integer status) {
        return tenantPackageMapper.selectListByStatus(status);
    }


    @VisibleForTesting
    void validateTenantPackageNameUnique(Long id, String name) {
        if (StrUtil.isBlank(name)) {
            return;
        }
        TenantPackageDO tenantPackage = tenantPackageMapper.selectByName(name);
        if (tenantPackage == null) {
            return;
        }
        // If the id is empty, it means there is no need to compare whether they are users with the same id.
        if (id == null) {
            throw exception(TENANT_PACKAGE_NAME_DUPLICATE);
        }
        if (!tenantPackage.getId().equals(id)) {
            throw exception(TENANT_PACKAGE_NAME_DUPLICATE);
        }
    }

}
