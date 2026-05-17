package cn.iocoder.yudao.module.system.dal.dataobject.permission;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.framework.tenant.core.aop.TenantIgnore;
import cn.iocoder.yudao.module.system.enums.permission.MenuTypeEnum;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Menu DO
 *
 * @author ruoyi
 */
@TableName("system_menu")
@KeySequence("system_menu_seq") // Primary key auto-increment for Oracle, PostgreSQL, Kingbase, DB2, H2 databases. If it is a database such as MySQL, you DO not need to write it.
@Data
@EqualsAndHashCode(callSuper = true)
@TenantIgnore
public class MenuDO extends BaseDO {

    /**
     * Menu ID - root node
     */
    public static final Long ID_ROOT = 0L;

    /**
     * menu ID
     */
    @TableId
    private Long id;
    /**
     * Menu name
     */
    private String name;
    /**
     * Permission ID
     *
     * The general format is: ${system}:${module}:${operation}
     * For example: system:admin:add, which is the add administrator of the system service.
     *
     * When we assign the MenuDO to the role, it means that the role has this resource:
     * - For the backend, with the @PreAuthorize annotation, this permission is required to configure the API API, so as to control the permissions of the API API.
     * - For the frontend, use the frontend label to configure whether the button is displayed to prevent the user from being able to see the operation if they DO not have the permission.
     */
    private String permission;
    /**
     * Menu type
     *
     * Enum {@link MenuTypeEnum}
     */
    private Integer type;
    /**
     * Display order
     */
    private Integer sort;
    /**
     * Parent menu ID
     */
    private Long parentId;
    /**
     * routing address
     *
     * If path is http(s), it is an external link
     */
    private String path;
    /**
     * menu icon
     */
    private String icon;
    /**
     * component path
     */
    private String component;
    /**
     * Component name
     */
    private String componentName;
    /**
     * Status
     *
     * Enum {@link CommonStatusEnum}
     */
    private Integer status;
    /**
     * visible or not
     *
     * Only used for menus and directories
     * When set to true, the menu will not be displayed in the sidebar, but the route will still exist. For example, some independent editing pages /edit/1024 etc.
     */
    private Boolean visible;
    /**
     * Whether to cache
     *
     * Only used for menus and directories. DO not use the keep-alive feature of Vue routing.
     * Note: If caching is enabled, the {@link #componentName} attribute must be filled in, otherwise it cannot be cached
     */
    private Boolean keepAlive;
    /**
     * Whether to always display
     *
     * If it is false, when the menu has only one submenu, it will not display itself and display the submenu directly.
     */
    private Boolean alwaysShow;

}
