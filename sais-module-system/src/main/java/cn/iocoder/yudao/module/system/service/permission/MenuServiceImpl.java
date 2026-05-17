package cn.iocoder.yudao.module.system.service.permission;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.system.controller.admin.permission.vo.menu.MenuListReqVO;
import cn.iocoder.yudao.module.system.controller.admin.permission.vo.menu.MenuSaveVO;
import cn.iocoder.yudao.module.system.dal.dataobject.permission.MenuDO;
import cn.iocoder.yudao.module.system.dal.mysql.permission.MenuMapper;
import cn.iocoder.yudao.module.system.dal.redis.RedisKeyConstants;
import cn.iocoder.yudao.module.system.enums.permission.MenuTypeEnum;
import cn.iocoder.yudao.module.system.service.tenant.TenantService;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.util.*;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertMap;
import static cn.iocoder.yudao.module.system.dal.dataobject.permission.MenuDO.ID_ROOT;
import static cn.iocoder.yudao.module.system.enums.ErrorCodeConstants.*;

/**
 * Menu Service implementation
 *
 * @author Yudao Source Code
 */
@Service
@Slf4j
public class MenuServiceImpl implements MenuService {

    @Resource
    private MenuMapper menuMapper;
    @Resource
    private PermissionService permissionService;
    @Resource
    @Lazy // Delay to avoid circular dependency errors
    private TenantService tenantService;

    @Override
    @CacheEvict(value = RedisKeyConstants.PERMISSION_MENU_ID_LIST, key = "#createReqVO.permission",
            condition = "#createReqVO.permission != null")
    public Long createMenu(MenuSaveVO createReqVO) {
        // Verify that parent menu exists
        validateParentMenu(createReqVO.getParentId(), null);
        // Verification menu (self)
        validateMenuName(createReqVO.getParentId(), createReqVO.getName(), null);
        validateMenuComponentName(createReqVO.getComponentName(), null);

        // Insert into database
        MenuDO menu = BeanUtils.toBean(createReqVO, MenuDO.class);
        initMenuProperty(menu);
        menuMapper.insert(menu);
        // Return
        return menu.getId();
    }

    @Override
    @CacheEvict(value = RedisKeyConstants.PERMISSION_MENU_ID_LIST,
            allEntries = true) // allEntries Clear all caches, because if the permission changes, both the old and new permissions are involved. Direct cleaning, simple and effective
    public void updateMenu(MenuSaveVO updateReqVO) {
        // Verify whether the updated menu exists
        if (menuMapper.selectById(updateReqVO.getId()) == null) {
            throw exception(MENU_NOT_EXISTS);
        }
        // Verify that parent menu exists
        validateParentMenu(updateReqVO.getParentId(), updateReqVO.getId());
        // Verification menu (self)
        validateMenuName(updateReqVO.getParentId(), updateReqVO.getName(), updateReqVO.getId());
        validateMenuComponentName(updateReqVO.getComponentName(), updateReqVO.getId());

        // Update to database
        MenuDO updateObj = BeanUtils.toBean(updateReqVO, MenuDO.class);
        initMenuProperty(updateObj);
        menuMapper.updateById(updateObj);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = RedisKeyConstants.PERMISSION_MENU_ID_LIST,
            allEntries = true) // allEntries Clear all caches, because we don’t know what the permission corresponding to the id is at this time. Direct cleaning, simple and effective
    public void deleteMenu(Long id) {
        // Check if there are submenus
        if (menuMapper.selectCountByParentId(id) > 0) {
            throw exception(MENU_EXISTS_CHILDREN);
        }
        // Verify whether the deleted menu exists
        if (menuMapper.selectById(id) == null) {
            throw exception(MENU_NOT_EXISTS);
        }
        // Mark for deletion
        menuMapper.deleteById(id);
        // Remove permissions granted to a role
        permissionService.processMenuDeleted(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = RedisKeyConstants.PERMISSION_MENU_ID_LIST,
            allEntries = true) // allEntries Clear all caches because Spring Cache does not support batch deletion by ids
    public void deleteMenuList(List<Long> ids) {
        // Check if there are submenus
        ids.forEach(id -> {
            if (menuMapper.selectCountByParentId(id) > 0) {
                throw exception(MENU_EXISTS_CHILDREN);
            }
        });

        // Mark for deletion
        menuMapper.deleteByIds(ids);
        // Remove permissions granted to a role
        ids.forEach(id -> permissionService.processMenuDeleted(id));
    }

    @Override
    public List<MenuDO> getMenuList() {
        return menuMapper.selectList();
    }

    @Override
    public List<MenuDO> getMenuListByTenant(MenuListReqVO reqVO) {
        // Query all menus and filter out closed nodes
        List<MenuDO> menus = getMenuList(reqVO);
        // When multi-tenancy is enabled, unactivated menus need to be filtered out.
        tenantService.handleTenantMenu(menuIds -> menus.removeIf(menu -> !CollUtil.contains(menuIds, menu.getId())));
        return menus;
    }

    @Override
    public List<MenuDO> filterDisableMenus(List<MenuDO> menuList) {
        if (CollUtil.isEmpty(menuList)){
            return Collections.emptyList();
        }
        Map<Long, MenuDO> menuMap = convertMap(menuList, MenuDO::getId);

        // Traverse the menu menu, find menus that are not disabled, and add them to the enabledMenus result
        List<MenuDO> enabledMenus = new ArrayList<>();
        Set<Long> disabledMenuCache = new HashSet<>(); // Save recursively searched menus that have been disabled to prevent repeated searches
        for (MenuDO menu : menuList) {
            if (isMenuDisabled(menu, menuMap, disabledMenuCache)) {
                continue;
            }
            enabledMenus.add(menu);
        }
        return enabledMenus;
    }

    private boolean isMenuDisabled(MenuDO node, Map<Long, MenuDO> menuMap, Set<Long> disabledMenuCache) {
        // If it has been determined to be a disabled node, end it directly.
        if (disabledMenuCache.contains(node.getId())) {
            return true;
        }

        // 1. First determine whether it is disabled
        if (CommonStatusEnum.isDisable(node.getStatus())) {
            disabledMenuCache.add(node.getId());
            return true;
        }

        // 2. Traverse until parentId is the root node, no need to judge
        Long parentId = node.getParentId();
        if (ObjUtil.equal(parentId, ID_ROOT)) {
            return false;
        }

        // 3. Continue traversing the parent node
        MenuDO parent = menuMap.get(parentId);
        if (parent == null || isMenuDisabled(parent, menuMap, disabledMenuCache)) {
            disabledMenuCache.add(node.getId());
            return true;
        }
        return false;
    }

    @Override
    public List<MenuDO> getMenuList(MenuListReqVO reqVO) {
        return menuMapper.selectList(reqVO);
    }

    @Override
    @Cacheable(value = RedisKeyConstants.PERMISSION_MENU_ID_LIST, key = "#permission")
    public List<Long> getMenuIdListByPermissionFromCache(String permission) {
        List<MenuDO> menus = menuMapper.selectListByPermission(permission);
        return convertList(menus, MenuDO::getId);
    }

    @Override
    public MenuDO getMenu(Long id) {
        return menuMapper.selectById(id);
    }

    @Override
    public List<MenuDO> getMenuList(Collection<Long> ids) {
        // When ids is empty, returns an empty instance object
        if (CollUtil.isEmpty(ids)) {
            return Lists.newArrayList();
        }
        return menuMapper.selectByIds(ids);
    }

    /**
     * Verify whether the parent menu is legal
     * <p>
     * 1. Cannot set itself as the parent menu
     * 2. The parent menu does not exist
     * 3. The parent menu must be {@link MenuTypeEnum#MENU} menu type
     *
     * @param parentId Parent menu ID
     * @param childId  Current menu ID
     */
    @VisibleForTesting
    void validateParentMenu(Long parentId, Long childId) {
        if (parentId == null || ID_ROOT.equals(parentId)) {
            return;
        }
        // Cannot set itself as parent menu
        if (parentId.equals(childId)) {
            throw exception(MENU_PARENT_ERROR);
        }
        MenuDO menu = menuMapper.selectById(parentId);
        // Parent menu does not exist
        if (menu == null) {
            throw exception(MENU_PARENT_NOT_EXISTS);
        }
        // The parent menu must be a catalog or menu type
        if (!MenuTypeEnum.DIR.getType().equals(menu.getType())
                && !MenuTypeEnum.MENU.getType().equals(menu.getType())) {
            throw exception(MENU_PARENT_NOT_DIR_OR_MENU);
        }
    }

    /**
     * Verify whether the menu is legal
     * <p>
     * 1. Verify whether the same menu name exists under the same parent menu ID
     *
     * @param name     Menu name
     * @param parentId Parent menu ID
     * @param id       menu ID
     */
    @VisibleForTesting
    void validateMenuName(Long parentId, String name, Long id) {
        MenuDO menu = menuMapper.selectByParentIdAndName(parentId, name);
        if (menu == null) {
            return;
        }
        // If the id is empty, it means there is no need to compare whether it is a menu with the same id.
        if (id == null) {
            throw exception(MENU_NAME_DUPLICATE);
        }
        if (!menu.getId().equals(id)) {
            throw exception(MENU_NAME_DUPLICATE);
        }
    }

    /**
     * Verify whether the menu component name is legal
     *
     * @param componentName Component name
     * @param id            menu ID
     */
    @VisibleForTesting
    void validateMenuComponentName(String componentName, Long id) {
        if (StrUtil.isBlank(componentName)) {
            return;
        }
        MenuDO menu = menuMapper.selectByComponentName(componentName);
        if (menu == null) {
            return;
        }
        // If the id is empty, it means there is no need to compare whether it is a menu with the same id.
        if (id == null) {
            throw exception(MENU_COMPONENT_NAME_DUPLICATE);
        }
        if (!menu.getId().equals(id)) {
            throw exception(MENU_COMPONENT_NAME_DUPLICATE);
        }
    }

    /**
     * Initialize the common properties of the menu.
     * <p>
     * For example, only directory or menu type menus can set icons.
     *
     * @param menu Menu
     */
    private void initMenuProperty(MenuDO menu) {
        // When the menu is a button type, the component, icon, and path attributes are not required and should be left blank.
        if (MenuTypeEnum.BUTTON.getType().equals(menu.getType())) {
            menu.setComponent("");
            menu.setComponentName("");
            menu.setIcon("");
            menu.setPath("");
        }
    }

}
