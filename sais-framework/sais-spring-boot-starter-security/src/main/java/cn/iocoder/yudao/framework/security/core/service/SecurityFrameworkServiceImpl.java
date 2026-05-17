package cn.iocoder.yudao.framework.security.core.service;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.yudao.framework.common.biz.system.permission.PermissionCommonApi;
import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import lombok.AllArgsConstructor;

import java.util.Arrays;

import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.skipPermissionCheck;

/**
 * Default {@link SecurityFrameworkService} implementation class
 *
 * @author Yudao Source Code
 */
@AllArgsConstructor
public class SecurityFrameworkServiceImpl implements SecurityFrameworkService {

 private final PermissionCommonApi permissionApi;

 @Override
 public boolean hasPermission(String permission) {
 return hasAnyPermissions(permission);
 }

 @Override
 public boolean hasAnyPermissions(String... permissions) {
        // Special: Cross-tenant access
 if (skipPermissionCheck()) {
 return true;
 }

        // Permission verification
 Long userId = getLoginUserId();
 if (userId == null) {
 return false;
 }
 return permissionApi.hasAnyPermissions(userId, permissions);
 }

 @Override
 public boolean hasRole(String role) {
 return hasAnyRoles(role);
 }

 @Override
 public boolean hasAnyRoles(String... roles) {
        // Special: Cross-tenant access
 if (skipPermissionCheck()) {
 return true;
 }

        // Permission verification
 Long userId = getLoginUserId();
 if (userId == null) {
 return false;
 }
 return permissionApi.hasAnyRoles(userId, roles);
 }

 @Override
 public boolean hasScope(String scope) {
 return hasAnyScopes(scope);
 }

 @Override
 public boolean hasAnyScopes(String... scope) {
        // Special: Cross-tenant access
 if (skipPermissionCheck()) {
 return true;
 }

        // Permission verification
 LoginUser user = SecurityFrameworkUtils.getLoginUser();
 if (user == null) {
 return false;
 }
 return CollUtil.containsAny(user.getScopes(), Arrays.asList(scope));
 }

}
