package cn.iocoder.yudao.module.system.service.user;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.common.util.validation.ValidationUtils;
import cn.iocoder.yudao.framework.datapermission.core.util.DataPermissionUtils;
import cn.iocoder.yudao.module.infra.api.config.ConfigApi;
import cn.iocoder.yudao.module.system.controller.admin.auth.vo.AuthRegisterReqVO;
import cn.iocoder.yudao.module.system.controller.admin.user.vo.profile.UserProfileUpdatePasswordReqVO;
import cn.iocoder.yudao.module.system.controller.admin.user.vo.profile.UserProfileUpdateReqVO;
import cn.iocoder.yudao.module.system.controller.admin.user.vo.user.UserImportExcelVO;
import cn.iocoder.yudao.module.system.controller.admin.user.vo.user.UserImportRespVO;
import cn.iocoder.yudao.module.system.controller.admin.user.vo.user.UserPageReqVO;
import cn.iocoder.yudao.module.system.controller.admin.user.vo.user.UserSaveReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.dept.DeptDO;
import cn.iocoder.yudao.module.system.dal.dataobject.dept.UserPostDO;
import cn.iocoder.yudao.module.system.dal.dataobject.user.AdminUserDO;
import cn.iocoder.yudao.module.system.dal.mysql.dept.UserPostMapper;
import cn.iocoder.yudao.module.system.dal.mysql.user.AdminUserMapper;
import cn.iocoder.yudao.module.system.service.dept.DeptService;
import cn.iocoder.yudao.module.system.service.dept.PostService;
import cn.iocoder.yudao.module.system.service.oauth2.OAuth2TokenService;
import cn.iocoder.yudao.module.system.service.permission.PermissionService;
import cn.iocoder.yudao.module.system.service.tenant.TenantService;
import com.google.common.annotations.VisibleForTesting;
import com.mzt.logapi.context.LogRecordContext;
import com.mzt.logapi.service.impl.DiffParseFunction;
import com.mzt.logapi.starter.annotation.LogRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import jakarta.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.*;
import static cn.iocoder.yudao.module.system.enums.ErrorCodeConstants.*;
import static cn.iocoder.yudao.module.system.enums.LogRecordConstants.*;

/**
 * Background user Service implementation class
 *
 * @author Yudao Source Code
 */
@Service("adminUserService")
@Slf4j
public class AdminUserServiceImpl implements AdminUserService {

    static final String USER_INIT_PASSWORD_KEY = "system.user.init-password";

    static final String USER_REGISTER_ENABLED_KEY = "system.user.register-enabled";

    @Resource
    private AdminUserMapper userMapper;

    @Resource
    private DeptService deptService;
    @Resource
    private PostService postService;
    @Resource
    private PermissionService permissionService;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    @Lazy // Delay to avoid circular dependency errors
    private TenantService tenantService;
    @Resource
    @Lazy // Lazy loading to avoid circular dependencies
    private OAuth2TokenService oauth2TokenService;

    @Resource
    private UserPostMapper userPostMapper;

    @Resource
    private ConfigApi configApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = SYSTEM_USER_TYPE, subType = SYSTEM_USER_CREATE_SUB_TYPE, bizNo = "{{#user.id}}",
            success = SYSTEM_USER_CREATE_SUCCESS)
    public Long createUser(UserSaveReqVO createReqVO) {
        // 1.1 Verify account coordination
        tenantService.handleTenantInfo(tenant -> {
            long count = userMapper.selectCount();
            if (count >= tenant.getAccountCount()) {
                throw exception(USER_COUNT_MAX, tenant.getAccountCount());
            }
        });
        // 1.2 Verification correctness
        validateUserForCreateOrUpdate(null, createReqVO.getUsername(),
                createReqVO.getMobile(), createReqVO.getEmail(), createReqVO.getDeptId(), createReqVO.getPostIds());
        // 2.1 Insert user
        AdminUserDO user = BeanUtils.toBean(createReqVO, AdminUserDO.class);
        user.setStatus(CommonStatusEnum.ENABLE.getStatus()); // Enabled by default
        user.setPassword(encodePassword(createReqVO.getPassword())); // Encrypted password
        userMapper.insert(user);
        // 2.2 Insert related positions
        if (CollectionUtil.isNotEmpty(user.getPostIds())) {
            userPostMapper.insertBatch(convertList(user.getPostIds(),
                    postId -> new UserPostDO().setUserId(user.getId()).setPostId(postId)));
        }

        // 3. Record operation log context
        LogRecordContext.putVariable("user", user);
        return user.getId();
    }

    @Override
    public Long registerUser(AuthRegisterReqVO registerReqVO) {
        // 1.1 Verify whether registration is enabled
        if (ObjUtil.notEqual(configApi.getConfigValueByKey(USER_REGISTER_ENABLED_KEY), "true")) {
            throw exception(USER_REGISTER_DISABLED);
        }
        // 1.2 Verify account coordination
        tenantService.handleTenantInfo(tenant -> {
            long count = userMapper.selectCount();
            if (count >= tenant.getAccountCount()) {
                throw exception(USER_COUNT_MAX, tenant.getAccountCount());
            }
        });
        // 1.3 Verification correctness
        validateUserForCreateOrUpdate(null, registerReqVO.getUsername(), null, null, null, null);

        // 2. Insert user
        AdminUserDO user = BeanUtils.toBean(registerReqVO, AdminUserDO.class);
        user.setStatus(CommonStatusEnum.ENABLE.getStatus()); // Enabled by default
        user.setPassword(encodePassword(registerReqVO.getPassword())); // Encrypted password
        userMapper.insert(user);
        return user.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = SYSTEM_USER_TYPE, subType = SYSTEM_USER_UPDATE_SUB_TYPE, bizNo = "{{#updateReqVO.id}}",
            success = SYSTEM_USER_UPDATE_SUCCESS)
    public void updateUser(UserSaveReqVO updateReqVO) {
        updateReqVO.setPassword(null); // Special: DO not update password here
        // 1. Verify correctness
        AdminUserDO oldUser = validateUserForCreateOrUpdate(updateReqVO.getId(), updateReqVO.getUsername(),
                updateReqVO.getMobile(), updateReqVO.getEmail(), updateReqVO.getDeptId(), updateReqVO.getPostIds());

        // 2.1 Update user
        AdminUserDO updateObj = BeanUtils.toBean(updateReqVO, AdminUserDO.class);
        userMapper.updateById(updateObj);
        // 2.2 Update positions
        updateUserPost(updateReqVO, updateObj);

        // 3. Record operation log context
        LogRecordContext.putVariable(DiffParseFunction.OLD_OBJECT, BeanUtils.toBean(oldUser, UserSaveReqVO.class));
        LogRecordContext.putVariable("user", oldUser);
    }

    private void updateUserPost(UserSaveReqVO reqVO, AdminUserDO updateObj) {
        Long userId = reqVO.getId();
        Set<Long> dbPostIds = convertSet(userPostMapper.selectListByUserId(userId), UserPostDO::getPostId);
        // Calculate newly added and deleted position IDs
        Set<Long> postIds = CollUtil.emptyIfNull(updateObj.getPostIds());
        Collection<Long> createPostIds = CollUtil.subtract(postIds, dbPostIds);
        Collection<Long> deletePostIds = CollUtil.subtract(dbPostIds, postIds);
        // Perform additions and deletions. For positions that have been authorized, no processing is required.
        if (!CollectionUtil.isEmpty(createPostIds)) {
            userPostMapper.insertBatch(convertList(createPostIds,
                    postId -> new UserPostDO().setUserId(userId).setPostId(postId)));
        }
        if (!CollectionUtil.isEmpty(deletePostIds)) {
            userPostMapper.deleteByUserIdAndPostId(userId, deletePostIds);
        }
    }

    @Override
    public void updateUserLogin(Long id, String loginIp) {
        userMapper.updateById(new AdminUserDO().setId(id).setLoginIp(loginIp).setLoginDate(LocalDateTime.now()));
    }

    @Override
    public void updateUserProfile(Long id, UserProfileUpdateReqVO reqVO) {
        // Check correctness
        validateUserExists(id);
        validateEmailUnique(id, reqVO.getEmail());
        validateMobileUnique(id, reqVO.getMobile());
        // perform update
        userMapper.updateById(BeanUtils.toBean(reqVO, AdminUserDO.class).setId(id));
    }

    @Override
    public void updateUserPassword(Long id, UserProfileUpdatePasswordReqVO reqVO) {
        // Verify old password password
        validateOldPassword(id, reqVO.getOldPassword());
        // perform update
        AdminUserDO updateObj = new AdminUserDO().setId(id);
        updateObj.setPassword(encodePassword(reqVO.getNewPassword())); // Encrypted password
        userMapper.updateById(updateObj);
    }

    @Override
    @LogRecord(type = SYSTEM_USER_TYPE, subType = SYSTEM_USER_UPDATE_PASSWORD_SUB_TYPE, bizNo = "{{#id}}",
            success = SYSTEM_USER_UPDATE_PASSWORD_SUCCESS)
    public void updateUserPassword(Long id, String password) {
        // 1. Verify user existence
        AdminUserDO user = validateUserExists(id);

        // 2. Update password
        AdminUserDO updateObj = new AdminUserDO();
        updateObj.setId(id);
        updateObj.setPassword(encodePassword(password)); // Encrypted password
        userMapper.updateById(updateObj);

        // 3. Record operation log context
        LogRecordContext.putVariable("user", user);
        LogRecordContext.putVariable("newPassword", updateObj.getPassword());
    }

    @Override
    public void updateUserStatus(Long id, Integer status) {
        // Verify user exists
        validateUserExists(id);
        // update status
        AdminUserDO updateObj = new AdminUserDO();
        updateObj.setId(id);
        updateObj.setStatus(status);
        userMapper.updateById(updateObj);

        // If the user is disabled, delete his Token information
        if (CommonStatusEnum.isDisable(status)) {
            oauth2TokenService.removeAccessToken(id, UserTypeEnum.ADMIN.getValue());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = SYSTEM_USER_TYPE, subType = SYSTEM_USER_DELETE_SUB_TYPE, bizNo = "{{#id}}",
            success = SYSTEM_USER_DELETE_SUCCESS)
    public void deleteUser(Long id) {
        // 1. Verify user existence
        AdminUserDO user = validateUserExists(id);

        // 2.1 Delete user
        userMapper.deleteById(id);
        // 2.2 Delete user-related data
        permissionService.processUserDeleted(id);
        // 2.2 Delete user position
        userPostMapper.deleteByUserId(id);

        // 3. Record operation log context
        LogRecordContext.putVariable("user", user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUserList(List<Long> ids) {
        // 1. Delete users in batches
        userMapper.deleteByIds(ids);

        // 2. Delete user-related data in batches
        ids.forEach(id -> {
            permissionService.processUserDeleted(id);
            userPostMapper.deleteByUserId(id);
        });
    }

    @Override
    public AdminUserDO getUserByUsername(String username) {
        return userMapper.selectByUsername(username);
    }

    @Override
    public AdminUserDO getUserByMobile(String mobile) {
        return userMapper.selectByMobile(mobile);
    }

    @Override
    public PageResult<AdminUserDO> getUserPage(UserPageReqVO reqVO) {
        // If there is a role ID, query the user ID corresponding to the role.
        Set<Long> userIds = null;
        if (reqVO.getRoleId() != null) {
            userIds = permissionService.getUserRoleIdListByRoleId(singleton(reqVO.getRoleId()));
            if (CollUtil.isEmpty(userIds)) {
                return PageResult.empty();
            }
        }

        // Page query
        return userMapper.selectPage(reqVO, getDeptCondition(reqVO.getDeptId()), userIds);
    }

    @Override
    public AdminUserDO getUser(Long id) {
        return userMapper.selectById(id);
    }

    @Override
    public List<AdminUserDO> getUserListByDeptIds(Collection<Long> deptIds) {
        if (CollUtil.isEmpty(deptIds)) {
            return Collections.emptyList();
        }
        return userMapper.selectListByDeptIds(deptIds);
    }

    @Override
    public List<AdminUserDO> getUserListByPostIds(Collection<Long> postIds) {
        if (CollUtil.isEmpty(postIds)) {
            return Collections.emptyList();
        }
        Set<Long> userIds = convertSet(userPostMapper.selectListByPostIds(postIds), UserPostDO::getUserId);
        if (CollUtil.isEmpty(userIds)) {
            return Collections.emptyList();
        }
        return userMapper.selectByIds(userIds);
    }

    @Override
    public List<AdminUserDO> getUserList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return userMapper.selectByIds(ids);
    }

    @Override
    public void validateUserList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return;
        }
        // Get job information
        List<AdminUserDO> users = userMapper.selectByIds(ids);
        Map<Long, AdminUserDO> userMap = CollectionUtils.convertMap(users, AdminUserDO::getId);
        // Verify
        ids.forEach(id -> {
            AdminUserDO user = userMap.get(id);
            if (user == null) {
                throw exception(USER_NOT_EXISTS);
            }
            if (!CommonStatusEnum.ENABLE.getStatus().equals(user.getStatus())) {
                throw exception(USER_IS_DISABLE, user.getNickname());
            }
        });
    }

    @Override
    public List<AdminUserDO> getUserListByNickname(String nickname) {
        return userMapper.selectListByNickname(nickname);
    }

    /**
     * Obtain department conditions: Query the sub-department IDs of the specified department, including itself
     *
     * @param deptId Department ID
     * @return Department ID collection
     */
    private Set<Long> getDeptCondition(Long deptId) {
        if (deptId == null) {
            return Collections.emptySet();
        }
        Set<Long> deptIds = convertSet(deptService.getChildDeptList(deptId), DeptDO::getId);
        deptIds.add(deptId); // including self
        return deptIds;
    }

    private AdminUserDO validateUserForCreateOrUpdate(Long id, String username, String mobile, String email,
                                               Long deptId, Set<Long> postIds) {
        // Turn off data permissions to avoid not being able to query data due to lack of data permissions, resulting in incorrect unique verification
        return DataPermissionUtils.executeIgnore(() -> {
            // Verify user exists
            AdminUserDO user = validateUserExists(id);
            // Verify username is unique
            validateUsernameUnique(id, username);
            // Verify that the mobile phone ID is unique
            validateMobileUnique(id, mobile);
            // Verify that the email is unique
            validateEmailUnique(id, email);
            // The verification department is open
            deptService.validateDeptList(CollectionUtils.singleton(deptId));
            // The verification position is open
            postService.validatePostList(postIds);
            return user;
        });
    }

    @VisibleForTesting
    AdminUserDO validateUserExists(Long id) {
        if (id == null) {
            return null;
        }
        AdminUserDO user = userMapper.selectById(id);
        if (user == null) {
            throw exception(USER_NOT_EXISTS);
        }
        return user;
    }

    @VisibleForTesting
    void validateUsernameUnique(Long id, String username) {
        if (StrUtil.isBlank(username)) {
            return;
        }
        AdminUserDO user = userMapper.selectByUsername(username);
        if (user == null) {
            return;
        }
        // If the id is empty, it means there is no need to compare whether they are users with the same id.
        if (id == null) {
            throw exception(USER_USERNAME_EXISTS);
        }
        if (!user.getId().equals(id)) {
            throw exception(USER_USERNAME_EXISTS);
        }
    }

    @VisibleForTesting
    void validateEmailUnique(Long id, String email) {
        if (StrUtil.isBlank(email)) {
            return;
        }
        AdminUserDO user = userMapper.selectByEmail(email);
        if (user == null) {
            return;
        }
        // If the id is empty, it means there is no need to compare whether they are users with the same id.
        if (id == null) {
            throw exception(USER_EMAIL_EXISTS);
        }
        if (!user.getId().equals(id)) {
            throw exception(USER_EMAIL_EXISTS);
        }
    }

    @VisibleForTesting
    void validateMobileUnique(Long id, String mobile) {
        if (StrUtil.isBlank(mobile)) {
            return;
        }
        AdminUserDO user = userMapper.selectByMobile(mobile);
        if (user == null) {
            return;
        }
        // If the id is empty, it means there is no need to compare whether they are users with the same id.
        if (id == null) {
            throw exception(USER_MOBILE_EXISTS);
        }
        if (!user.getId().equals(id)) {
            throw exception(USER_MOBILE_EXISTS);
        }
    }

    /**
     * Verify old password
     * @param id          user id
     * @param oldPassword old password
     */
    @VisibleForTesting
    void validateOldPassword(Long id, String oldPassword) {
        AdminUserDO user = userMapper.selectById(id);
        if (user == null) {
            throw exception(USER_NOT_EXISTS);
        }
        if (!isPasswordMatch(oldPassword, user.getPassword())) {
            throw exception(USER_PASSWORD_FAILED);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class) // Add a transaction, roll back all imports if exception occurs
    public UserImportRespVO importUserList(List<UserImportExcelVO> importUsers, boolean isUpdateSupport) {
        // 1.1 Parameter verification
        if (CollUtil.isEmpty(importUsers)) {
            throw exception(USER_IMPORT_LIST_IS_EMPTY);
        }
        // 1.2 The initialization password cannot be empty
        String initPassword = configApi.getConfigValueByKey(USER_INIT_PASSWORD_KEY);
        if (StrUtil.isEmpty(initPassword)) {
            throw exception(USER_IMPORT_INIT_PASSWORD);
        }

        // 2. Traverse, create or update one by one
        UserImportRespVO respVO = UserImportRespVO.builder().createUsernames(new ArrayList<>())
                .updateUsernames(new ArrayList<>()).failureUsernames(new LinkedHashMap<>()).build();
        AtomicInteger index = new AtomicInteger(1);
        importUsers.forEach(importUser -> {
            int currentIndex = index.getAndIncrement();
            // 2.1.1 Check whether the fields meet the requirements
            try {
                ValidationUtils.validate(BeanUtils.toBean(importUser, UserSaveReqVO.class).setPassword(initPassword));
            } catch (ConstraintViolationException ex) {
                String key = StrUtil.blankToDefault(importUser.getUsername(), "No. " + currentIndex + " OK");
                respVO.getFailureUsernames().put(key, ex.getMessage());
                return;
            }
            // 2.1.2 Verification to determine whether there are any reasons for noncompliance
            try {
                validateUserForCreateOrUpdate(null, null, importUser.getMobile(), importUser.getEmail(),
                        importUser.getDeptId(), null);
            } catch (ServiceException ex) {
                respVO.getFailureUsernames().put(importUser.getUsername(), ex.getMessage());
                return;
            }

            // 2.2.1 Determine if it does not exist, insert it
            AdminUserDO existUser = userMapper.selectByUsername(importUser.getUsername());
            if (existUser == null) {
                userMapper.insert(BeanUtils.toBean(importUser, AdminUserDO.class)
                        .setPassword(encodePassword(initPassword)).setPostIds(new HashSet<>())); // Set default password and empty position ID array
                respVO.getCreateUsernames().add(importUser.getUsername());
                return;
            }
            // 2.2.2 If it exists, determine whether the update is allowed
            if (!isUpdateSupport) {
                respVO.getFailureUsernames().put(importUser.getUsername(), USER_USERNAME_EXISTS.getMsg());
                return;
            }
            AdminUserDO updateUser = BeanUtils.toBean(importUser, AdminUserDO.class);
            updateUser.setId(existUser.getId());
            userMapper.updateById(updateUser);
            respVO.getUpdateUsernames().add(importUser.getUsername());
        });
        return respVO;
    }

    @Override
    public List<AdminUserDO> getUserListByStatus(Integer status) {
        return userMapper.selectListByStatus(status);
    }

    @Override
    public boolean isPasswordMatch(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    /**
     * Encrypt password
     *
     * @param password Password
     * @return Encrypted password
     */
    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

}
