package cn.iocoder.yudao.module.system.service.user;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.module.system.controller.admin.auth.vo.AuthRegisterReqVO;
import cn.iocoder.yudao.module.system.controller.admin.user.vo.profile.UserProfileUpdatePasswordReqVO;
import cn.iocoder.yudao.module.system.controller.admin.user.vo.profile.UserProfileUpdateReqVO;
import cn.iocoder.yudao.module.system.controller.admin.user.vo.user.UserImportExcelVO;
import cn.iocoder.yudao.module.system.controller.admin.user.vo.user.UserImportRespVO;
import cn.iocoder.yudao.module.system.controller.admin.user.vo.user.UserPageReqVO;
import cn.iocoder.yudao.module.system.controller.admin.user.vo.user.UserSaveReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.user.AdminUserDO;
import jakarta.validation.Valid;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Backend user service API
 *
 * @author Yudao Source Code
 */
public interface AdminUserService {

    /**
     * Create user
     *
     * @param createReqVO User information
     * @return User ID
     */
    Long createUser(@Valid UserSaveReqVO createReqVO);

    /**
     * Registered user
     *
     * @param registerReqVO User information
     * @return User ID
     */
    Long registerUser(@Valid AuthRegisterReqVO registerReqVO);

    /**
     * Modify user
     *
     * @param updateReqVO User information
     */
    void updateUser(@Valid UserSaveReqVO updateReqVO);

    /**
     * Update the user's last login information
     *
     * @param id User ID
     * @param loginIp Login IP
     */
    void updateUserLogin(Long id, String loginIp);

    /**
     * Modify user personal information
     *
     * @param id User ID
     * @param reqVO User personal information
     */
    void updateUserProfile(Long id, @Valid UserProfileUpdateReqVO reqVO);

    /**
     * Modify user personal password
     *
     * @param id User ID
     * @param reqVO Update user personal password
     */
    void updateUserPassword(Long id, @Valid UserProfileUpdatePasswordReqVO reqVO);

    /**
     * Change password
     *
     * @param id       User ID
     * @param password Password
     */
    void updateUserPassword(Long id, String password);

    /**
     * Modify status
     *
     * @param id     User ID
     * @param status Status
     */
    void updateUserStatus(Long id, Integer status);

    /**
     * Delete user
     *
     * @param id User ID
     */
    void deleteUser(Long id);

    /**
     * Delete users in batches
     *
     * @param ids User ID array
     */
    void deleteUserList(List<Long> ids);

    /**
     * Query users by username
     *
     * @param username Username
     * @return User object information
     */
    AdminUserDO getUserByUsername(String username);

    /**
     * Get users by mobile phone ID
     *
     * @param mobile Mobile phone ID
     * @return User object information
     */
    AdminUserDO getUserByMobile(String mobile);

    /**
     * Get user paginated list
     *
     * @param reqVO Paging conditions
     * @return Paginated list
     */
    PageResult<AdminUserDO> getUserPage(UserPageReqVO reqVO);

    /**
     * Query user by user ID
     *
     * @param id User ID
     * @return User object information
     */
    AdminUserDO getUser(Long id);

    /**
     * Get the user array of the specified department
     *
     * @param deptIds department array
     * @return User array
     */
    List<AdminUserDO> getUserListByDeptIds(Collection<Long> deptIds);

    /**
     * Get the user array of the specified position
     *
     * @param postIds Position array
     * @return User array
     */
    List<AdminUserDO> getUserListByPostIds(Collection<Long> postIds);

    /**
     * Get user list
     *
     * @param ids User ID array
     * @return User list
     */
    List<AdminUserDO> getUserList(Collection<Long> ids);

    /**
     * Verify that users are valid. The following situations will be deemed invalid:
     * 1. The user ID does not exist
     * 2. User is disabled
     *
     * @param ids User ID array
     */
    void validateUserList(Collection<Long> ids);

    /**
     * Get user map
     *
     * @param ids User ID array
     * @return User Map
     */
    default Map<Long, AdminUserDO> getUserMap(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return new HashMap<>();
        }
        return CollectionUtils.convertMap(getUserList(ids), AdminUserDO::getId);
    }

    /**
     * Get user list, fuzzy matching based on nickname
     *
     * @param nickname Nickname
     * @return User list
     */
    List<AdminUserDO> getUserListByNickname(String nickname);

    /**
     * Import users in batches
     *
     * @param importUsers     Import user list
     * @param isUpdateSupport Does it support updates?
     * @return Import results
     */
    UserImportRespVO importUserList(List<UserImportExcelVO> importUsers, boolean isUpdateSupport);

    /**
     * Users who have obtained the specified status
     *
     * @param status Status
     * @return users
     */
    List<AdminUserDO> getUserListByStatus(Integer status);

    /**
     * Determine whether the password matches
     *
     * @param rawPassword unencrypted password
     * @param encodedPassword Encrypted password
     * @return Does it match
     */
    boolean isPasswordMatch(String rawPassword, String encodedPassword);

}
