package cn.iocoder.yudao.module.system.api.user;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.datapermission.core.annotation.DataPermission;
import cn.iocoder.yudao.framework.datapermission.core.util.DataPermissionUtils;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import cn.iocoder.yudao.module.system.dal.dataobject.dept.DeptDO;
import cn.iocoder.yudao.module.system.dal.dataobject.user.AdminUserDO;
import cn.iocoder.yudao.module.system.service.dept.DeptService;
import cn.iocoder.yudao.module.system.service.user.AdminUserService;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertSet;

/**
 * Admin user API implementation class
 *
 * @author Yudao Source Code
 */
@Service
public class AdminUserApiImpl implements AdminUserApi {

    @Resource
    private AdminUserService userService;
    @Resource
    private DeptService deptService;

    @Override
    @DataPermission(enable = false) // Ignore data permissions to avoid being unable to query users due to filtering. Similar: https://github.com/YunaiV/ruoyi-vue-pro/issues/1051
    public AdminUserRespDTO getUser(Long id) {
        AdminUserDO user = userService.getUser(id);
        return BeanUtils.toBean(user, AdminUserRespDTO.class);
    }

    @Override
    public List<AdminUserRespDTO> getUserListBySubordinate(Long id) {
        // 1.1 Obtain the department the user is responsible for
        List<DeptDO> depts = deptService.getDeptListByLeaderUserId(id);
        if (CollUtil.isEmpty(depts)) {
            return Collections.emptyList();
        }
        // 1.2 Get all sub-departments
        Set<Long> deptIds = convertSet(depts, DeptDO::getId);
        List<DeptDO> childDeptList = deptService.getChildDeptList(deptIds);
        if (CollUtil.isNotEmpty(childDeptList)) {
            deptIds.addAll(convertSet(childDeptList, DeptDO::getId));
        }

        // 2. Obtain user information corresponding to the department
        List<AdminUserDO> users = userService.getUserListByDeptIds(deptIds);
        users.removeIf(item -> ObjUtil.equal(item.getId(), id)); // exclude yourself
        return BeanUtils.toBean(users, AdminUserRespDTO.class);
    }

    @Override
    public List<AdminUserRespDTO> getUserList(Collection<Long> ids) {
        return DataPermissionUtils.executeIgnore(() -> { // Disable data permissions. The reason is that generally API queries based on specified id are mainly based on data splicing.
            List<AdminUserDO> users = userService.getUserList(ids);
            return BeanUtils.toBean(users, AdminUserRespDTO.class);
        });
    }

    @Override
    public List<AdminUserRespDTO> getUserListByDeptIds(Collection<Long> deptIds) {
        List<AdminUserDO> users = userService.getUserListByDeptIds(deptIds);
        return BeanUtils.toBean(users, AdminUserRespDTO.class);
    }

    @Override
    public List<AdminUserRespDTO> getUserListByPostIds(Collection<Long> postIds) {
        List<AdminUserDO> users = userService.getUserListByPostIds(postIds);
        return BeanUtils.toBean(users, AdminUserRespDTO.class);
    }

    @Override
    public void validateUserList(Collection<Long> ids) {
        userService.validateUserList(ids);
    }

}
