package cn.iocoder.yudao.module.system.api.permission;

import cn.iocoder.yudao.framework.common.biz.system.permission.PermissionCommonApi;

import java.util.Collection;
import java.util.Set;

/**
 * Permission API API
 *
 * @author Yudao Source Code
 */
public interface PermissionApi extends PermissionCommonApi {

    /**
     * Get a collection of user IDs with multiple roles
     *
     * @param roleIds role ID collection
     * @return User ID set
     */
    Set<Long> getUserRoleIdListByRoleIds(Collection<Long> roleIds);

}
