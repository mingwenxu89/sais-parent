package cn.iocoder.yudao.framework.security.core.service;

/**
 * Security framework Service interface defines permission-related verification operations.
 *
 * @author Yudao Source Code
 */
public interface SecurityFrameworkService {

 /**
     * Determine whether there is permission
 *
     * @param permission Permissions
     * @return whether
 */
 boolean hasPermission(String permission);

 /**
     * To determine whether you have permission, any one of them can be used
 *
     * @param permissions Permissions
     * @return whether
 */
 boolean hasAnyPermissions(String... permissions);

 /**
     * Determine if there is a role
 *
     * Note that the role uses the code identifier of SysRoleDO
 *
     * @param role Role
     * @return whether
 */
 boolean hasRole(String role);

 /**
     * To determine whether there is a role, any one will do
 *
     * @param roles role array
     * @return whether
 */
 boolean hasAnyRoles(String... roles);

 /**
     * Determine whether there is authorization
 *
     * @param scope Authorize
     * @return whether
 */
 boolean hasScope(String scope);

 /**
     * Determine whether there is authorization scope, any one will do
 *
     * @param scope Authorization scope array
     * @return whether
 */
 boolean hasAnyScopes(String... scope);
}
