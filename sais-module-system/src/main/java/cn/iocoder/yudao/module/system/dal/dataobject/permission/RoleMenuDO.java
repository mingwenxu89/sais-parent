package cn.iocoder.yudao.module.system.dal.dataobject.permission;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Role and menu association
 *
 * @author ruoyi
 */
@TableName("system_role_menu")
@KeySequence("system_role_menu_seq") // Primary key auto-increment for Oracle, PostgreSQL, Kingbase, DB2, H2 databases. If it is a database such as MySQL, you DO not need to write it.
@Data
@EqualsAndHashCode(callSuper = true)
public class RoleMenuDO extends TenantBaseDO {

    /**
     * auto-increment primary key
     */
    @TableId
    private Long id;
    /**
     * Role ID
     */
    private Long roleId;
    /**
     * Menu ID
     */
    private Long menuId;

}
