package cn.iocoder.yudao.module.system.service.permission;

import cn.iocoder.yudao.framework.common.biz.system.permission.dto.DeptDataPermissionRespDTO;

import java.util.Collection;
import java.util.Set;

import static java.util.Collections.singleton;

/**
 * Permission Service API
 * <p>
 * Provides user-role, role-menu, role-department associated permission processing
 *
 * @author Yudao Source Code
 */
public interface PermissionService {

    /**
     * To determine whether you have permission, any one of them can be used
     *
     * @param userId      User ID
     * @param permissions Permission
     * @return Whether
     */
    boolean hasAnyPermissions(Long userId, String... permissions);

    /**
     * To determine whether there is a role, any one will DO
     *
     * @param roles role array
     * @return Whether
     */
    boolean hasAnyRoles(Long userId, String... roles);

    // ========== Role-menu related methods ==========

    /**
     * Set role menu
     *
     * @param roleId  role ID
     * @param menuIds menu ID collection
     */
    void assignRoleMenu(Long roleId, Set<Long> menuIds);

    /**
     * When processing role deletion, delete associated authorization data
     *
     * @param roleId role ID
     */
    void processRoleDeleted(Long roleId);

    /**
     * When processing menu deletion, delete associated authorization data
     *
     * @param menuId menu ID
     */
    void processMenuDeleted(Long menuId);

    /**
     * Get the set of menu IDs owned by the character
     *
     * @param roleId role ID
     * @return menu ID collection
     */
    default Set<Long> getRoleMenuListByRoleId(Long roleId) {
        return getRoleMenuListByRoleId(singleton(roleId));
    }

    /**
     * Get the set of menu IDs owned by characters
     *
     * @param roleIds Role ID array
     * @return menu ID collection
     */
    Set<Long> getRoleMenuListByRoleId(Collection<Long> roleIds);

    /**
     * Get the array of character IDs with the specified menu, obtained from the cache
     *
     * @param menuId menu ID
     * @return Role ID array
     */
    Set<Long> getMenuRoleIdListByMenuIdFromCache(Long menuId);

    // ========== User-role related methods ==========

    /**
     * Set user role
     *
     * @param userId  role ID
     * @param roleIds role ID collection
     */
    void assignUserRole(Long userId, Set<Long> roleIds);

    /**
     * When processing user deletion, delete associated authorization data
     *
     * @param userId User ID
     */
    void processUserDeleted(Long userId);

    /**
     * Get a collection of user IDs with multiple roles
     *
     * @param roleIds role ID collection
     * @return User ID set
     */
    Set<Long> getUserRoleIdListByRoleId(Collection<Long> roleIds);

    /**
     * Get the set of role IDs owned by the user
     *
     * @param userId User ID
     * @return role ID collection
     */
    Set<Long> getUserRoleIdListByUserId(Long userId);

    /**
     * Get the set of role IDs owned by the user, obtained from the cache
     *
     * @param userId User ID
     * @return role ID collection
     */
    Set<Long> getUserRoleIdListByUserIdFromCache(Long userId);

    // ========== User-department related methods ==========

    /**
     * Set data permissions for roles
     *
     * @param roleId           role ID
     * @param dataScope        data range
     * @param dataScopeDeptIds Department ID array
     */
    void assignRoleDataScope(Long roleId, Integer dataScope, Set<Long> dataScopeDeptIds);

    /**
     * Obtain the department data permissions of the logged in user
     *
     * @param userId User ID
     * @return Department data permissions
     */
    DeptDataPermissionRespDTO getDeptDataPermission(Long userId);

}
