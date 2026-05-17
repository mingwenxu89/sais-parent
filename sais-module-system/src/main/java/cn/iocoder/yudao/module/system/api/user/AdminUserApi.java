package cn.iocoder.yudao.module.system.api.user;

import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Admin user API API
 *
 * @author Yudao Source Code
 */
public interface AdminUserApi {

    /**
     * Query user by user ID
     *
     * @param id User ID
     * @return User object information
     */
    AdminUserRespDTO getUser(Long id);

    /**
     * Query user's subordinates by user ID
     *
     * @param id User ID
     * @return User's subordinate user list
     */
    List<AdminUserRespDTO> getUserListBySubordinate(Long id);

    /**
     * Query users by user ID
     *
     * @param ids User IDs
     * @return User object information
     */
    List<AdminUserRespDTO> getUserList(Collection<Long> ids);

    /**
     * Get the user array of the specified department
     *
     * @param deptIds department array
     * @return User array
     */
    List<AdminUserRespDTO> getUserListByDeptIds(Collection<Long> deptIds);

    /**
     * Get the user array of the specified position
     *
     * @param postIds Position array
     * @return User array
     */
    List<AdminUserRespDTO> getUserListByPostIds(Collection<Long> postIds);

    /**
     * Get user map
     *
     * @param ids User ID array
     * @return User Map
     */
    default Map<Long, AdminUserRespDTO> getUserMap(Collection<Long> ids) {
        List<AdminUserRespDTO> users = getUserList(ids);
        return CollectionUtils.convertMap(users, AdminUserRespDTO::getId);
    }

    /**
     * Verify whether the user is valid. The following situations will be deemed invalid:
     * 1. The user ID does not exist
     * 2. User is disabled
     *
     * @param id User ID
     */
    default void validateUser(Long id) {
        validateUserList(Collections.singleton(id));
    }

    /**
     * Verify that users are valid. The following situations will be deemed invalid:
     * 1. The user ID does not exist
     * 2. User is disabled
     *
     * @param ids User ID array
     */
    void validateUserList(Collection<Long> ids);

}
