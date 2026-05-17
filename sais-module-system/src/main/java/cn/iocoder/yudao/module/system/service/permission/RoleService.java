package cn.iocoder.yudao.module.system.service.permission;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.system.controller.admin.permission.vo.role.RolePageReqVO;
import cn.iocoder.yudao.module.system.controller.admin.permission.vo.role.RoleSaveReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.permission.RoleDO;

import jakarta.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Role Service API
 *
 * @author Yudao Source Code
 */
public interface RoleService {

    /**
     * Create a role
     *
     * @param createReqVO Create role information
     * @param type role type
     * @return role ID
     */
    Long createRole(@Valid RoleSaveReqVO createReqVO, Integer type);

    /**
     * Update role
     *
     * @param updateReqVO Update role information
     */
    void updateRole(@Valid RoleSaveReqVO updateReqVO);

    /**
     * Delete role
     *
     * @param id role ID
     */
    void deleteRole(Long id);

    /**
     * Delete roles in batches
     *
     * @param ids Role ID array
     */
    void deleteRoleList(List<Long> ids);

    /**
     * Set data permissions for roles
     *
     * @param id role ID
     * @param dataScope data range
     * @param dataScopeDeptIds Department ID array
     */
    void updateRoleDataScope(Long id, Integer dataScope, Set<Long> dataScopeDeptIds);

    /**
     * Get a role
     *
     * @param id role ID
     * @return Role
     */
    RoleDO getRole(Long id);

    /**
     * Get the role from the cache
     *
     * @param id role ID
     * @return Role
     */
    RoleDO getRoleFromCache(Long id);

    /**
     * Get a list of roles
     *
     * @param ids Role ID array
     * @return role list
     */
    List<RoleDO> getRoleList(Collection<Long> ids);

    /**
     * Get the role array, from cache
     *
     * @param ids Role ID array
     * @return role array
     */
    List<RoleDO> getRoleListFromCache(Collection<Long> ids);

    /**
     * Get a list of roles
     *
     * @param statuses filter status
     * @return role list
     */
    List<RoleDO> getRoleListByStatus(Collection<Integer> statuses);

    /**
     * Get a list of all roles
     *
     * @return role list
     */
    List<RoleDO> getRoleList();

    /**
     * Get role pagination
     *
     * @param reqVO Role paging query
     * @return Role paging results
     */
    PageResult<RoleDO> getRolePage(RolePageReqVO reqVO);

    /**
     * Determine whether there is an administrator in the role ID array
     *
     * @param ids Role ID array
     * @return Is there an administrator
     */
    boolean hasAnySuperAdmin(Collection<Long> ids);

    /**
     * Verify that the characters are valid. The following situations will be deemed invalid:
     * 1. The character ID does not exist
     * 2. The character is disabled
     *
     * @param ids Role ID array
     */
    void validateRoleList(Collection<Long> ids);

}
