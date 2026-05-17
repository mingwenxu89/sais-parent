package cn.iocoder.yudao.module.system.service.tenant;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.common.util.date.DateUtils;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.datapermission.core.annotation.DataPermission;
import cn.iocoder.yudao.framework.tenant.config.TenantProperties;
import cn.iocoder.yudao.framework.tenant.core.context.TenantContextHolder;
import cn.iocoder.yudao.framework.tenant.core.util.TenantUtils;
import cn.iocoder.yudao.module.system.controller.admin.permission.vo.role.RoleSaveReqVO;
import cn.iocoder.yudao.module.system.controller.admin.tenant.vo.tenant.TenantPageReqVO;
import cn.iocoder.yudao.module.system.controller.admin.tenant.vo.tenant.TenantSaveReqVO;
import cn.iocoder.yudao.module.system.convert.tenant.TenantConvert;
import cn.iocoder.yudao.module.system.dal.dataobject.permission.MenuDO;
import cn.iocoder.yudao.module.system.dal.dataobject.permission.RoleDO;
import cn.iocoder.yudao.module.system.dal.dataobject.tenant.TenantDO;
import cn.iocoder.yudao.module.system.dal.dataobject.tenant.TenantPackageDO;
import cn.iocoder.yudao.module.system.dal.mysql.tenant.TenantMapper;
import cn.iocoder.yudao.module.system.enums.permission.RoleCodeEnum;
import cn.iocoder.yudao.module.system.enums.permission.RoleTypeEnum;
import cn.iocoder.yudao.module.system.service.permission.MenuService;
import cn.iocoder.yudao.module.system.service.permission.PermissionService;
import cn.iocoder.yudao.module.system.service.permission.RoleService;
import cn.iocoder.yudao.module.system.service.tenant.handler.TenantInfoHandler;
import cn.iocoder.yudao.module.system.service.tenant.handler.TenantMenuHandler;
import cn.iocoder.yudao.module.system.service.user.AdminUserService;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.system.enums.ErrorCodeConstants.*;
import static java.util.Collections.singleton;

/**
 * Tenant Service implementation class
 *
 * @author Yudao Source Code
 */
@Service
@Validated
@Slf4j
public class TenantServiceImpl implements TenantService {

    @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
    @Autowired(required = false) // Since the yudao.tenant.enable configuration item can turn off the multi-tenant function, there is no forced injection here.
    private TenantProperties tenantProperties;

    @Resource
    private TenantMapper tenantMapper;

    @Resource
    private TenantPackageService tenantPackageService;
    @Resource
    @Lazy // Delay to avoid circular dependency errors
    private AdminUserService userService;
    @Resource
    private RoleService roleService;
    @Resource
    private MenuService menuService;
    @Resource
    private PermissionService permissionService;

    @Override
    public List<Long> getTenantIdList() {
        List<TenantDO> tenants = tenantMapper.selectList();
        return CollectionUtils.convertList(tenants, TenantDO::getId);
    }

    @Override
    public void validTenant(Long id) {
        TenantDO tenant = getTenant(id);
        if (tenant == null) {
            throw exception(TENANT_NOT_EXISTS);
        }
        if (tenant.getStatus().equals(CommonStatusEnum.DISABLE.getStatus())) {
            throw exception(TENANT_DISABLE, tenant.getName());
        }
        if (DateUtils.isExpired(tenant.getExpireTime())) {
            throw exception(TENANT_EXPIRE, tenant.getName());
        }
    }

    @Override
    @DSTransactional // Multiple data sources, using @DSTransactional Ensure local transactions and data source switching
    @DataPermission(enable = false) // See https://gitee.com/zhijiantianya/ruoyi-vue-pro/pulls/1154 instructions
    public Long createTenant(TenantSaveReqVO createReqVO) {
        // Verify whether the tenant name is duplicated
        validTenantNameDuplicate(createReqVO.getName(), null);
        // Verify whether the tenant domain name is duplicated
        validTenantWebsiteDuplicate(createReqVO.getWebsites(), null);
        // Verify that the package is disabled (the package is optional, and the full menu is given when it is empty)
        TenantPackageDO tenantPackage = createReqVO.getPackageId() != null
                ? tenantPackageService.validTenantPackage(createReqVO.getPackageId()) : null;

        // Create tenant
        TenantDO tenant = BeanUtils.toBean(createReqVO, TenantDO.class);
        tenant.setAccountCount(0); // accountCount The field has been removed from the API and is fixed to write 0 to satisfy the NOT NULL constraint.
        tenantMapper.insert(tenant);
        // Create an administrator for a tenant
        TenantUtils.execute(tenant.getId(), () -> {
            // Create a role
            Long roleId = createRole(tenantPackage);
            // Create users and assign roles
            Long userId = createUser(roleId, createReqVO);
            // Modify tenant administrator
            tenantMapper.updateById(new TenantDO().setId(tenant.getId()).setContactUserId(userId));
        });
        return tenant.getId();
    }

    private Long createUser(Long roleId, TenantSaveReqVO createReqVO) {
        // Create user
        Long userId = userService.createUser(TenantConvert.INSTANCE.convert02(createReqVO));
        // Assign roles
        permissionService.assignUserRole(userId, singleton(roleId));
        return userId;
    }

    private Long createRole(TenantPackageDO tenantPackage) {
        // Create a role
        RoleSaveReqVO reqVO = new RoleSaveReqVO();
        reqVO.setName(RoleCodeEnum.TENANT_ADMIN.getName()).setCode(RoleCodeEnum.TENANT_ADMIN.getCode())
                .setSort(0).setRemark("Generated automatically by the system");
        Long roleId = roleService.createRole(reqVO, RoleTypeEnum.SYSTEM.getType());
        // Assign permissions: If there is a set menu, use the set menu, otherwise give the full menu
        Set<Long> menuIds = tenantPackage != null
                ? tenantPackage.getMenuIds()
                : CollectionUtils.convertSet(menuService.getMenuList(), MenuDO::getId);
        permissionService.assignRoleMenu(roleId, menuIds);
        return roleId;
    }

    @Override
    @DSTransactional // Multiple data sources, using @DSTransactional Ensure local transactions and data source switching
    public void updateTenant(TenantSaveReqVO updateReqVO) {
        // Check existence
        TenantDO tenant = validateUpdateTenant(updateReqVO.getId());
        // Verify whether the tenant name is duplicated
        validTenantNameDuplicate(updateReqVO.getName(), updateReqVO.getId());
        // Verify whether the tenant domain name is duplicated
        validTenantWebsiteDuplicate(updateReqVO.getWebsites(), updateReqVO.getId());
        // Update tenant
        TenantDO updateObj = BeanUtils.toBean(updateReqVO, TenantDO.class);
        tenantMapper.updateById(updateObj);
        // Package optional: Role menu permissions will be synchronized only when there is a package and it changes.
        if (updateReqVO.getPackageId() != null
                && ObjectUtil.notEqual(tenant.getPackageId(), updateReqVO.getPackageId())) {
            TenantPackageDO tenantPackage = tenantPackageService.validTenantPackage(updateReqVO.getPackageId());
            updateTenantRoleMenu(tenant.getId(), tenantPackage.getMenuIds());
        }
    }

    private void validTenantNameDuplicate(String name, Long id) {
        TenantDO tenant = tenantMapper.selectByName(name);
        if (tenant == null) {
            return;
        }
        // If the id is empty, it means there is no need to compare whether they are tenants with the same name.
        if (id == null) {
            throw exception(TENANT_NAME_DUPLICATE, name);
        }
        if (!tenant.getId().equals(id)) {
            throw exception(TENANT_NAME_DUPLICATE, name);
        }
    }

    private void validTenantWebsiteDuplicate(List<String> websites, Long excludeId) {
        if (CollUtil.isEmpty(websites)) {
            return;
        }
        websites.forEach(website -> {
            List<TenantDO> tenants = tenantMapper.selectListByWebsite(website);
            if (excludeId != null) {
                tenants.removeIf(tenant -> tenant.getId().equals(excludeId));
            }
            if (CollUtil.isNotEmpty(tenants)) {
                throw exception(TENANT_WEBSITE_DUPLICATE, website);
            }
        });
    }

    @Override
    @DSTransactional
    public void updateTenantRoleMenu(Long tenantId, Set<Long> menuIds) {
        TenantUtils.execute(tenantId, () -> {
            // Get all characters
            List<RoleDO> roles = roleService.getRoleList();
            roles.forEach(role -> Assert.isTrue(tenantId.equals(role.getTenantId()), "Role({}/{}) tenant mismatch",
                    role.getId(), role.getTenantId(), tenantId)); // Check everything
            // Reassign permissions for each role
            roles.forEach(role -> {
                // If you are a tenant administrator, reassign its permissions to those of the tenant package.
                if (Objects.equals(role.getCode(), RoleCodeEnum.TENANT_ADMIN.getCode())) {
                    permissionService.assignRoleMenu(role.getId(), menuIds);
                    log.info("[updateTenantRoleMenu][The permissions of the tenant administrator ({}/{}) are modified to ({})]", role.getId(), role.getTenantId(), menuIds);
                    return;
                }
                // If it is another role, remove the permissions that exceed the package
                Set<Long> roleMenuIds = permissionService.getRoleMenuListByRoleId(role.getId());
                roleMenuIds = CollUtil.intersectionDistinct(roleMenuIds, menuIds);
                permissionService.assignRoleMenu(role.getId(), roleMenuIds);
                log.info("[updateTenantRoleMenu][The permissions of role ({}/{}) are modified to ({})]", role.getId(), role.getTenantId(), roleMenuIds);
            });
        });
    }

    @Override
    public void deleteTenant(Long id) {
        // Check existence
        validateUpdateTenant(id);
        // Delete
        tenantMapper.deleteById(id);
    }

    @Override
    public void deleteTenantList(List<Long> ids) {
        // 1. Verify existence
        ids.forEach(this::validateUpdateTenant);

        // 2. Batch deletion
        tenantMapper.deleteByIds(ids);
    }

    private TenantDO validateUpdateTenant(Long id) {
        TenantDO tenant = tenantMapper.selectById(id);
        if (tenant == null) {
            throw exception(TENANT_NOT_EXISTS);
        }
        // Built-in tenant, deletion is not allowed
        if (isSystemTenant(tenant)) {
            throw exception(TENANT_CAN_NOT_UPDATE_SYSTEM);
        }
        return tenant;
    }

    @Override
    public TenantDO getTenant(Long id) {
        return tenantMapper.selectById(id);
    }

    @Override
    public PageResult<TenantDO> getTenantPage(TenantPageReqVO pageReqVO) {
        return tenantMapper.selectPage(pageReqVO);
    }

    @Override
    public TenantDO getTenantByName(String name) {
        return tenantMapper.selectByName(name);
    }

    @Override
    public TenantDO getTenantByWebsite(String website) {
        List<TenantDO> tenants = tenantMapper.selectListByWebsite(website);
        return CollUtil.getFirst(tenants);
    }

    @Override
    public Long getTenantCountByPackageId(Long packageId) {
        return tenantMapper.selectCountByPackageId(packageId);
    }

    @Override
    public List<TenantDO> getTenantListByPackageId(Long packageId) {
        return tenantMapper.selectListByPackageId(packageId);
    }

    @Override
    public List<TenantDO> getTenantListByStatus(Integer status) {
        return tenantMapper.selectListByStatus(status);
    }

    @Override
    public void handleTenantInfo(TenantInfoHandler handler) {
        // If disabled, no logic is executed
        if (isTenantDisable()) {
            return;
        }
        // Get tenants
        TenantDO tenant = getTenant(TenantContextHolder.getRequiredTenantId());
        // execution processor
        handler.handle(tenant);
    }

    @Override
    public void handleTenantMenu(TenantMenuHandler handler) {
        // If disabled, no logic is executed
        if (isTenantDisable()) {
            return;
        }
        // Get the tenant, then get the menu
        TenantDO tenant = getTenant(TenantContextHolder.getRequiredTenantId());
        Set<Long> menuIds;
        if (isSystemTenant(tenant) || tenant.getPackageId() == null) { // System tenant or no package, the menu is full
            menuIds = CollectionUtils.convertSet(menuService.getMenuList(), MenuDO::getId);
        } else {
            menuIds = tenantPackageService.getTenantPackage(tenant.getPackageId()).getMenuIds();
        }
        // execution processor
        handler.handle(menuIds);
    }

    private static boolean isSystemTenant(TenantDO tenant) {
        return Objects.equals(tenant.getPackageId(), TenantDO.PACKAGE_ID_SYSTEM);
    }

    private boolean isTenantDisable() {
        return tenantProperties == null || Boolean.FALSE.equals(tenantProperties.getEnable());
    }

}
