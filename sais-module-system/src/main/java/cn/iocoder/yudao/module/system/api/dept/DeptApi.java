package cn.iocoder.yudao.module.system.api.dept;

import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.module.system.api.dept.dto.DeptRespDTO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Department API API
 *
 * @author Yudao Source Code
 */
public interface DeptApi {

    /**
     * Get department information
     *
     * @param id Department ID
     * @return Department information
     */
    DeptRespDTO getDept(Long id);

    /**
     * Get department information array
     *
     * @param ids Department ID array
     * @return Department information array
     */
    List<DeptRespDTO> getDeptList(Collection<Long> ids);

    /**
     * Check whether the departments are valid. The following situations will be deemed invalid:
     * 1. The department ID does not exist
     * 2. Department is disabled
     *
     * @param ids Role ID array
     */
    void validateDeptList(Collection<Long> ids);

    /**
     * Get the department map with the specified ID
     *
     * @param ids Department ID array
     * @return Department Map
     */
    default Map<Long, DeptRespDTO> getDeptMap(Collection<Long> ids) {
        List<DeptRespDTO> list = getDeptList(ids);
        return CollectionUtils.convertMap(list, DeptRespDTO::getId);
    }

    /**
     * Get all sub-departments of the specified department
     *
     * @param id Department ID
     * @return List of subdepartments
     */
    List<DeptRespDTO> getChildDeptList(Long id);

}
