package cn.iocoder.yudao.module.system.service.permission;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.datapermission.core.annotation.DataPermission;
import cn.iocoder.yudao.framework.common.biz.system.permission.dto.DeptDataPermissionRespDTO;
import cn.iocoder.yudao.module.system.dal.dataobject.permission.MenuDO;
import cn.iocoder.yudao.module.system.dal.dataobject.permission.RoleDO;
import cn.iocoder.yudao.module.system.dal.dataobject.permission.RoleMenuDO;
import cn.iocoder.yudao.module.system.dal.dataobject.permission.UserRoleDO;
import cn.iocoder.yudao.module.system.dal.mysql.permission.RoleMenuMapper;
import cn.iocoder.yudao.module.system.dal.mysql.permission.UserRoleMapper;
import cn.iocoder.yudao.module.system.dal.redis.RedisKeyConstants;
import cn.iocoder.yudao.module.system.enums.permission.DataScopeEnum;
import cn.iocoder.yudao.module.system.service.dept.DeptService;
import cn.iocoder.yudao.module.system.service.user.AdminUserService;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Suppliers;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.util.*;
import java.util.function.Supplier;

import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertSet;
import static cn.iocoder.yudao.framework.common.util.json.JsonUtils.toJsonString;

/**
 * Permission Service implementation class
 *
 * @author Yudao Source Code
 */
@Service
@Slf4j
public class PermissionServiceImpl implements PermissionService {

    @Resource
    private RoleMenuMapper roleMenuMapper;
    @Resource
    private UserRoleMapper userRoleMapper;

    @Resource
    private RoleService roleService;
    @Resource
    private MenuService menuService;
    @Resource
    private DeptService deptService;
    @Resource
    private AdminUserService userService;

    @Override
    public boolean hasAnyPermissions(Long userId, String... permissions) {
        // If it is empty, it means you already have permission.
        if (ArrayUtil.isEmpty(permissions)) {
            return true;
        }

        // Get the currently logged in role. If it is empty, it means there is no permission
        List<RoleDO> roles = getEnableUserRoleListByUserIdFromCache(userId);
        if (CollUtil.isEmpty(roles)) {
            return false;
        }

        // Case 1: Traverse and judge each permission. If one of them is satisfied, it means you have permission.
        for (String permission : permissions) {
            if (hasAnyPermission(roles, permission)) {
                return true;
            }
        }

        // Scenario 2: If it is over-administration, it also means that you have permission.
        return roleService.hasAnySuperAdmin(convertSet(roles, RoleDO::getId));
    }

    /**
     * Determine whether the specified role has the permission permission
     *
     * @param roles Specify role array
     * @param permission Permission ID
     * @return DO you have
     */
    private boolean hasAnyPermission(List<RoleDO> roles, String permission) {
        List<Long> menuIds = menuService.getMenuIdListByPermissionFromCache(permission);
        // Using strict mode, if the permission cannot find the corresponding Menu, it will be considered that there is no permission.
        if (CollUtil.isEmpty(menuIds)) {
            return false;
        }

        // Determine whether there is permission
        Set<Long> roleIds = convertSet(roles, RoleDO::getId);
        for (Long menuId : menuIds) {
            // Get the collection of character IDs that own this menu
            Set<Long> menuRoleIds = getSelf().getMenuRoleIdListByMenuIdFromCache(menuId);
            // If there is an intersection, it means you have permission.
            if (CollUtil.containsAny(menuRoleIds, roleIds)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasAnyRoles(Long userId, String... roles) {
        // If it is empty, it means you already have permission.
        if (ArrayUtil.isEmpty(roles)) {
            return true;
        }

        // Get the currently logged in role. If it is empty, it means there is no permission
        List<RoleDO> roleList = getEnableUserRoleListByUserIdFromCache(userId);
        if (CollUtil.isEmpty(roleList)) {
            return false;
        }

        // Determine if there is a role
        Set<String> userRoles = convertSet(roleList, RoleDO::getCode);
        return CollUtil.containsAny(userRoles, Sets.newHashSet(roles));
    }

    // ========== Role-menu related methods ==========

    @Override
    @DSTransactional // Multiple data sources, using @DSTransactional Ensure local transactions and data source switching
    @Caching(evict = {
            @CacheEvict(value = RedisKeyConstants.MENU_ROLE_ID_LIST,
            allEntries = true),
            @CacheEvict(value = RedisKeyConstants.PERMISSION_MENU_ID_LIST,
            allEntries = true) // allEntries Clear all caches, mainly because one update involves more menuIds, but batching will be faster.
    })
    public void assignRoleMenu(Long roleId, Set<Long> menuIds) {
        // Get the character's menu ID
        Set<Long> dbMenuIds = convertSet(roleMenuMapper.selectListByRoleId(roleId), RoleMenuDO::getMenuId);
        // Calculate new and deleted menu IDs
        Set<Long> menuIdList = CollUtil.emptyIfNull(menuIds);
        Collection<Long> createMenuIds = CollUtil.subtract(menuIdList, dbMenuIds);
        Collection<Long> deleteMenuIds = CollUtil.subtract(dbMenuIds, menuIdList);
        // Perform additions and deletions. For menus that have been authorized, no processing is required.
        if (CollUtil.isNotEmpty(createMenuIds)) {
            roleMenuMapper.insertBatch(CollectionUtils.convertList(createMenuIds, menuId -> {
                RoleMenuDO entity = new RoleMenuDO();
                entity.setRoleId(roleId);
                entity.setMenuId(menuId);
                return entity;
            }));
        }
        if (CollUtil.isNotEmpty(deleteMenuIds)) {
            roleMenuMapper.deleteListByRoleIdAndMenuIds(roleId, deleteMenuIds);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @Caching(evict = {
            @CacheEvict(value = RedisKeyConstants.MENU_ROLE_ID_LIST,
                    allEntries = true), // allEntries Clear all caches. The menu caches corresponding to roleId cannot be easily obtained here.
            @CacheEvict(value = RedisKeyConstants.USER_ROLE_ID_LIST,
                    allEntries = true) // allEntries Clear all caches. The user caches corresponding to roleId cannot be easily obtained here.
    })
    public void processRoleDeleted(Long roleId) {
        // Mark Delete UserRole
        userRoleMapper.deleteListByRoleId(roleId);
        // Mark Delete RoleMenu
        roleMenuMapper.deleteListByRoleId(roleId);
    }

    @Override
    @CacheEvict(value = RedisKeyConstants.MENU_ROLE_ID_LIST, key = "#menuId")
    public void processMenuDeleted(Long menuId) {
        roleMenuMapper.deleteListByMenuId(menuId);
    }

    @Override
    public Set<Long> getRoleMenuListByRoleId(Collection<Long> roleIds) {
        if (CollUtil.isEmpty(roleIds)) {
            return Collections.emptySet();
        }

        // If you are an administrator, get all menu IDs
        if (roleService.hasAnySuperAdmin(roleIds)) {
            return convertSet(menuService.getMenuList(), MenuDO::getId);
        }
        // If you are a non-administrator, get the menu ID you own.
        return convertSet(roleMenuMapper.selectListByRoleId(roleIds), RoleMenuDO::getMenuId);
    }

    @Override
    @Cacheable(value = RedisKeyConstants.MENU_ROLE_ID_LIST, key = "#menuId")
    public Set<Long> getMenuRoleIdListByMenuIdFromCache(Long menuId) {
        return convertSet(roleMenuMapper.selectListByMenuId(menuId), RoleMenuDO::getRoleId);
    }

    // ========== User-role related methods ==========

    @Override
    @DSTransactional // Multiple data sources, using @DSTransactional Ensure local transactions and data source switching
    @CacheEvict(value = RedisKeyConstants.USER_ROLE_ID_LIST, key = "#userId")
    public void assignUserRole(Long userId, Set<Long> roleIds) {
        // Get the role ID owned by the character
        Set<Long> dbRoleIds = convertSet(userRoleMapper.selectListByUserId(userId),
                UserRoleDO::getRoleId);
        // Calculate newly added and deleted role IDs
        Set<Long> roleIdList = CollUtil.emptyIfNull(roleIds);
        Collection<Long> createRoleIds = CollUtil.subtract(roleIdList, dbRoleIds);
        Collection<Long> deleteMenuIds = CollUtil.subtract(dbRoleIds, roleIdList);
        // Perform additions and deletions. For already authorized roles, no processing is required.
        if (!CollectionUtil.isEmpty(createRoleIds)) {
            userRoleMapper.insertBatch(CollectionUtils.convertList(createRoleIds, roleId -> {
                UserRoleDO entity = new UserRoleDO();
                entity.setUserId(userId);
                entity.setRoleId(roleId);
                return entity;
            }));
        }
        if (!CollectionUtil.isEmpty(deleteMenuIds)) {
            userRoleMapper.deleteListByUserIdAndRoleIdIds(userId, deleteMenuIds);
        }
    }

    @Override
    @CacheEvict(value = RedisKeyConstants.USER_ROLE_ID_LIST, key = "#userId")
    public void processUserDeleted(Long userId) {
        userRoleMapper.deleteListByUserId(userId);
    }

    @Override
    public Set<Long> getUserRoleIdListByUserId(Long userId) {
        return convertSet(userRoleMapper.selectListByUserId(userId), UserRoleDO::getRoleId);
    }

    @Override
    @Cacheable(value = RedisKeyConstants.USER_ROLE_ID_LIST, key = "#userId")
    public Set<Long> getUserRoleIdListByUserIdFromCache(Long userId) {
        return getUserRoleIdListByUserId(userId);
    }

    @Override
    public Set<Long> getUserRoleIdListByRoleId(Collection<Long> roleIds) {
        return convertSet(userRoleMapper.selectListByRoleIds(roleIds), UserRoleDO::getUserId);
    }

    /**
     * Get the roles owned by the user and these roles are enabled
     *
     * @param userId User ID
     * @return Roles owned by the user
     */
    @VisibleForTesting
    List<RoleDO> getEnableUserRoleListByUserIdFromCache(Long userId) {
        // Get the role ID owned by the user
        Set<Long> roleIds = getSelf().getUserRoleIdListByUserIdFromCache(userId);
        // Get the role array and remove the disabled ones
        List<RoleDO> roles = roleService.getRoleListFromCache(roleIds);
        roles.removeIf(role -> !CommonStatusEnum.ENABLE.getStatus().equals(role.getStatus()));
        return roles;
    }

    // ========== User-department related methods ==========

    @Override
    public void assignRoleDataScope(Long roleId, Integer dataScope, Set<Long> dataScopeDeptIds) {
        roleService.updateRoleDataScope(roleId, dataScope, dataScopeDeptIds);
    }

    @Override
    @DataPermission(enable = false) // Turn off data permissions, otherwise there will be problems with recursively obtaining data permissions.
    public DeptDataPermissionRespDTO getDeptDataPermission(Long userId) {
        // Get user's role
        List<RoleDO> roles = getEnableUserRoleListByUserIdFromCache(userId);

        // If the role is empty, you can only view yourself
        DeptDataPermissionRespDTO result = new DeptDataPermissionRespDTO();
        if (CollUtil.isEmpty(roles)) {
            result.setSelf(true);
            return result;
        }

        // Obtain the cache of the user's department ID through lazy evaluation of Guava's Suppliers, that is, there is and is only the first time the DB query is initiated.
        Supplier<Long> userDeptId = Suppliers.memoize(() -> userService.getUser(userId).getDeptId());
        // Iterate through each role and calculate
        for (RoleDO role : roles) {
            // When empty, skip
            if (role.getDataScope() == null) {
                continue;
            }
            // Situation 1, ALL
            if (Objects.equals(role.getDataScope(), DataScopeEnum.ALL.getScope())) {
                result.setAll(true);
                continue;
            }
            // Case 2, DEPT_CUSTOM
            if (Objects.equals(role.getDataScope(), DataScopeEnum.DEPT_CUSTOM.getScope())) {
                CollUtil.addAll(result.getDeptIds(), role.getDataScopeDeptIds());
                // When customizing visible departments, you are guaranteed to be able to see the department you are in. Otherwise, there may be problems in some scenarios.
                // For example, when logging in, the username query based on t_user may be filtered out by dept_id
                CollUtil.addAll(result.getDeptIds(), userDeptId.get());
                continue;
            }
            // Case three, DEPT_ONLY
            if (Objects.equals(role.getDataScope(), DataScopeEnum.DEPT_ONLY.getScope())) {
                CollectionUtils.addIfNotNull(result.getDeptIds(), userDeptId.get());
                continue;
            }
            // Case 4, DEPT_DEPT_AND_CHILD
            if (Objects.equals(role.getDataScope(), DataScopeEnum.DEPT_AND_CHILD.getScope())) {
                CollUtil.addAll(result.getDeptIds(), deptService.getChildDeptIdListFromCache(userDeptId.get()));
                // Add own department ID
                CollUtil.addAll(result.getDeptIds(), userDeptId.get());
                continue;
            }
            // Situation five, SELF
            if (Objects.equals(role.getDataScope(), DataScopeEnum.SELF.getScope())) {
                result.setSelf(true);
                continue;
            }
            // Unknown situation, error log can be
            log.error("[getDeptDataPermission][LoginUser({}) role({}) cannot be processed]", userId, toJsonString(result));
        }
        return result;
    }

    /**
     * Obtain its own proxy object to solve the problem of AOP taking effect
     *
     * @return myself
     */
    private PermissionServiceImpl getSelf() {
        return SpringUtil.getBean(getClass());
    }

}
