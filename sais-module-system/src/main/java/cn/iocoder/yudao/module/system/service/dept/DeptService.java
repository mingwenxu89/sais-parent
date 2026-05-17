package cn.iocoder.yudao.module.system.service.dept;

import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.module.system.controller.admin.dept.vo.dept.DeptListReqVO;
import cn.iocoder.yudao.module.system.controller.admin.dept.vo.dept.DeptSaveReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.dept.DeptDO;

import java.util.*;

/**
 * Department Service API
 *
 * @author Yudao Source Code
 */
public interface DeptService {

    /**
     * Create department
     *
     * @param createReqVO Department information
     * @return Department ID
     */
    Long createDept(DeptSaveReqVO createReqVO);

    /**
     * Update department
     *
     * @param updateReqVO Department information
     */
    void updateDept(DeptSaveReqVO updateReqVO);

    /**
     * Delete department
     *
     * @param id Department ID
     */
    void deleteDept(Long id);

    /**
     * Delete departments in batches
     *
     * @param ids Department ID array
     */
    void deleteDeptList(List<Long> ids);

    /**
     * Get department information
     *
     * @param id Department ID
     * @return Department information
     */
    DeptDO getDept(Long id);

    /**
     * Get department information array
     *
     * @param ids Department ID array
     * @return Department information array
     */
    List<DeptDO> getDeptList(Collection<Long> ids);

    /**
     * Filter department list
     *
     * @param reqVO Filter Request VO
     * @return Department list
     */
    List<DeptDO> getDeptList(DeptListReqVO reqVO);

    /**
     * Get the department map with the specified ID
     *
     * @param ids Department ID array
     * @return Department Map
     */
    default Map<Long, DeptDO> getDeptMap(Collection<Long> ids) {
        List<DeptDO> list = getDeptList(ids);
        return CollectionUtils.convertMap(list, DeptDO::getId);
    }

    /**
     * Get all sub-departments of the specified department
     *
     * @param id Department ID
     * @return List of subdepartments
     */
    default List<DeptDO> getChildDeptList(Long id) {
        return getChildDeptList(Collections.singleton(id));
    }

    /**
     * Get all sub-departments of the specified department
     *
     * @param ids Department ID array
     * @return List of subdepartments
     */
    List<DeptDO> getChildDeptList(Collection<Long> ids);

    /**
     * Get a list of departments with designated leaders
     *
     * @param id leader ID
     * @return Department list
     */
    List<DeptDO> getDeptListByLeaderUserId(Long id);

    /**
     * Get all sub-departments, from cache
     *
     * @param id Parent department ID
     * @return List of subdepartments
     */
    Set<Long> getChildDeptIdListFromCache(Long id);

    /**
     * Check whether the departments are valid. The following situations will be deemed invalid:
     * 1. The department ID does not exist
     * 2. Department is disabled
     *
     * @param ids Role ID array
     */
    void validateDeptList(Collection<Long> ids);

}
