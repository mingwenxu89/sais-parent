package cn.iocoder.yudao.module.system.service.permission;

import cn.iocoder.yudao.module.system.controller.admin.permission.vo.menu.MenuListReqVO;
import cn.iocoder.yudao.module.system.controller.admin.permission.vo.menu.MenuSaveVO;
import cn.iocoder.yudao.module.system.dal.dataobject.permission.MenuDO;

import java.util.Collection;
import java.util.List;

/**
 * Menu Service API
 *
 * @author Yudao Source Code
 */
public interface MenuService {

    /**
     * Create menu
     *
     * @param createReqVO Menu information
     * @return Created menu ID
     */
    Long createMenu(MenuSaveVO createReqVO);

    /**
     * Update menu
     *
     * @param updateReqVO Menu information
     */
    void updateMenu(MenuSaveVO updateReqVO);

    /**
     * delete menu
     *
     * @param id menu ID
     */
    void deleteMenu(Long id);

    /**
     * Batch delete menu
     *
     * @param ids menu ID array
     */
    void deleteMenuList(List<Long> ids);

    /**
     * Get a list of all menus
     *
     * @return Menu list
     */
    List<MenuDO> getMenuList();

    /**
     * Filter menu list based on tenant
     * Note that if it is a system tenant, the full menu will still be returned.
     *
     * @param reqVO Filter Request VO
     * @return Menu list
     */
    List<MenuDO> getMenuListByTenant(MenuListReqVO reqVO);

    /**
     * Filter out closed menus and their submenus
     *
     * @param list Menu list
     * @return Filtered menu list
     */
    List<MenuDO> filterDisableMenus(List<MenuDO> list);

    /**
     * Filter menu list
     *
     * @param reqVO Filter Request VO
     * @return Menu list
     */
    List<MenuDO> getMenuList(MenuListReqVO reqVO);

    /**
     * Get the menu ID array corresponding to the permission
     *
     * @param permission Permission ID
     * @return array
     */
    List<Long> getMenuIdListByPermissionFromCache(String permission);

    /**
     * get menu
     *
     * @param id menu ID
     * @return Menu
     */
    MenuDO getMenu(Long id);

    /**
     * getmenu array
     *
     * @param ids menu ID array
     * @return menu array
     */
    List<MenuDO> getMenuList(Collection<Long> ids);

}
