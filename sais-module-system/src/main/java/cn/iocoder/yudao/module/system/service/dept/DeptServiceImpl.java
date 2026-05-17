package cn.iocoder.yudao.module.system.service.dept;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.datapermission.core.annotation.DataPermission;
import cn.iocoder.yudao.module.system.controller.admin.dept.vo.dept.DeptListReqVO;
import cn.iocoder.yudao.module.system.controller.admin.dept.vo.dept.DeptSaveReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.dept.DeptDO;
import cn.iocoder.yudao.module.system.dal.mysql.dept.DeptMapper;
import cn.iocoder.yudao.module.system.dal.redis.RedisKeyConstants;
import com.google.common.annotations.VisibleForTesting;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;
import java.util.*;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertSet;
import static cn.iocoder.yudao.module.system.enums.ErrorCodeConstants.*;

/**
 * Department Service implementation class
 *
 * @author Yudao Source Code
 */
@Service
@Validated
@Slf4j
public class DeptServiceImpl implements DeptService {

    @Resource
    private DeptMapper deptMapper;

    @Override
    @CacheEvict(cacheNames = RedisKeyConstants.DEPT_CHILDREN_ID_LIST,
            allEntries = true) // allEntries Clear all caches, because operating a department involves multiple caches
    public Long createDept(DeptSaveReqVO createReqVO) {
        if (createReqVO.getParentId() == null) {
            createReqVO.setParentId(DeptDO.PARENT_ID_ROOT);
        }
        // Verify the validity of the parent department
        validateParentDept(null, createReqVO.getParentId());
        // Verify the uniqueness of department names
        validateDeptNameUnique(null, createReqVO.getParentId(), createReqVO.getName());

        // insert department
        DeptDO dept = BeanUtils.toBean(createReqVO, DeptDO.class);
        deptMapper.insert(dept);
        return dept.getId();
    }

    @Override
    @CacheEvict(cacheNames = RedisKeyConstants.DEPT_CHILDREN_ID_LIST,
            allEntries = true) // allEntries Clear all caches, because operating a department involves multiple caches
    public void updateDept(DeptSaveReqVO updateReqVO) {
        if (updateReqVO.getParentId() == null) {
            updateReqVO.setParentId(DeptDO.PARENT_ID_ROOT);
        }
        // Verify your own existence
        validateDeptExists(updateReqVO.getId());
        // Verify the validity of the parent department
        validateParentDept(updateReqVO.getId(), updateReqVO.getParentId());
        // Verify the uniqueness of department names
        validateDeptNameUnique(updateReqVO.getId(), updateReqVO.getParentId(), updateReqVO.getName());

        // Update department
        DeptDO updateObj = BeanUtils.toBean(updateReqVO, DeptDO.class);
        deptMapper.updateById(updateObj);
    }

    @Override
    @CacheEvict(cacheNames = RedisKeyConstants.DEPT_CHILDREN_ID_LIST,
            allEntries = true) // allEntries Clear all caches, because operating a department involves multiple caches
    public void deleteDept(Long id) {
        // Check if it exists
        validateDeptExists(id);
        // Check if there are sub-departments
        if (deptMapper.selectCountByParentId(id) > 0) {
            throw exception(DEPT_EXITS_CHILDREN);
        }
        // Delete department
        deptMapper.deleteById(id);
    }

    @Override
    @CacheEvict(cacheNames = RedisKeyConstants.DEPT_CHILDREN_ID_LIST,
            allEntries = true) // allEntries Clear all caches, because operating a department involves multiple caches
    public void deleteDeptList(List<Long> ids) {
        // Check if there are sub-departments
        for (Long id : ids) {
            if (deptMapper.selectCountByParentId(id) > 0) {
                throw exception(DEPT_EXITS_CHILDREN);
            }
        }

        // Delete departments in batches
        deptMapper.deleteByIds(ids);
    }

    @VisibleForTesting
    void validateDeptExists(Long id) {
        if (id == null) {
            return;
        }
        DeptDO dept = deptMapper.selectById(id);
        if (dept == null) {
            throw exception(DEPT_NOT_FOUND);
        }
    }

    @VisibleForTesting
    void validateParentDept(Long id, Long parentId) {
        if (parentId == null || DeptDO.PARENT_ID_ROOT.equals(parentId)) {
            return;
        }
        // 1. You cannot set yourself as the parent department
        if (Objects.equals(id, parentId)) {
            throw exception(DEPT_PARENT_ERROR);
        }
        // 2. The parent department does not exist
        DeptDO parentDept = deptMapper.selectById(parentId);
        if (parentDept == null) {
            throw exception(DEPT_PARENT_NOT_EXITS);
        }
        // 3. Recursively check the parent department. If the parent department is its own sub-department, an error will be reported to avoid forming a loop.
        if (id == null) { // id If it is empty, it means that it is newly added and there is no need to consider loops.
            return;
        }
        for (int i = 0; i < Short.MAX_VALUE; i++) {
            // 3.1 Verification loop
            parentId = parentDept.getParentId();
            if (Objects.equals(id, parentId)) {
                throw exception(DEPT_PARENT_IS_CHILD);
            }
            // 3.2 Continue recursively to the next-level parent department
            if (parentId == null || DeptDO.PARENT_ID_ROOT.equals(parentId)) {
                break;
            }
            parentDept = deptMapper.selectById(parentId);
            if (parentDept == null) {
                break;
            }
        }
    }

    @VisibleForTesting
    void validateDeptNameUnique(Long id, Long parentId, String name) {
        DeptDO dept = deptMapper.selectByParentIdAndName(parentId, name);
        if (dept == null) {
            return;
        }
        // If the id is empty, it means there is no need to compare whether it is a department with the same id.
        if (id == null) {
            throw exception(DEPT_NAME_DUPLICATE);
        }
        if (ObjectUtil.notEqual(dept.getId(), id)) {
            throw exception(DEPT_NAME_DUPLICATE);
        }
    }

    @Override
    public DeptDO getDept(Long id) {
        return deptMapper.selectById(id);
    }

    @Override
    public List<DeptDO> getDeptList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return deptMapper.selectByIds(ids);
    }

    @Override
    public List<DeptDO> getDeptList(DeptListReqVO reqVO) {
        List<DeptDO> list = deptMapper.selectList(reqVO);
        list.sort(Comparator.comparing(DeptDO::getSort));
        return list;
    }

    @Override
    public List<DeptDO> getChildDeptList(Collection<Long> ids) {
        List<DeptDO> children = new LinkedList<>();
        // Traverse each layer
        Collection<Long> parentIds = ids;
        for (int i = 0; i < Short.MAX_VALUE; i++) { // Use Short.MAX_VALUE to avoid infinite loops in bug scenarios
            // Query the current layer and all sub-departments
            List<DeptDO> depts = deptMapper.selectListByParentId(parentIds);
            // 1. If there are no sub-departments, end the traversal
            if (CollUtil.isEmpty(depts)) {
                break;
            }
            // 2. If there are sub-departments, continue traversing
            children.addAll(depts);
            parentIds = convertSet(depts, DeptDO::getId);
        }
        return children;
    }

    @Override
    public List<DeptDO> getDeptListByLeaderUserId(Long id) {
        return deptMapper.selectListByLeaderUserId(id);
    }

    @Override
    @DataPermission(enable = false) // Disable data permissions to avoid incorrect cache creation
    @Cacheable(cacheNames = RedisKeyConstants.DEPT_CHILDREN_ID_LIST, key = "#id")
    public Set<Long> getChildDeptIdListFromCache(Long id) {
        List<DeptDO> children = getChildDeptList(id);
        return convertSet(children, DeptDO::getId);
    }

    @Override
    public void validateDeptList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return;
        }
        // Get department information
        Map<Long, DeptDO> deptMap = getDeptMap(ids);
        // Verify
        ids.forEach(id -> {
            DeptDO dept = deptMap.get(id);
            if (dept == null) {
                throw exception(DEPT_NOT_FOUND);
            }
            if (!CommonStatusEnum.ENABLE.getStatus().equals(dept.getStatus())) {
                throw exception(DEPT_NOT_ENABLE, dept.getName());
            }
        });
    }

}
