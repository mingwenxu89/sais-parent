package cn.iocoder.yudao.framework.common.biz.system.permission;

import cn.iocoder.yudao.framework.common.biz.system.permission.dto.DeptDataPermissionRespDTO;

/**
 * Permission API interface
 *
 * @author Yudao Source Code
 */
public interface PermissionCommonApi {

 /**
     * To determine whether you have permission, any one of them can be used
 *
     * @param userId User ID
     * @param permissions Permissions
     * @return whether
 */
 boolean hasAnyPermissions(Long userId, String... permissions);

 /**
     * To determine whether there is a role, any one will do
 *
     * @param userId User ID
     * @param roles role array
     * @return whether
 */
 boolean hasAnyRoles(Long userId, String... roles);

 /**
     * Obtain the department data permissions of the logged in user
 *
     * @param userId User ID
     * @return Department data permissions
 */
 DeptDataPermissionRespDTO getDeptDataPermission(Long userId);

}
