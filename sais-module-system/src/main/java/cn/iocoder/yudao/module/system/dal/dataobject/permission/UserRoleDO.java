package cn.iocoder.yudao.module.system.dal.dataobject.permission;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * User and role association
 *
 * @author ruoyi
 */
@TableName("system_user_role")
@KeySequence("system_user_role_seq") // Primary key auto-increment for Oracle, PostgreSQL, Kingbase, DB2, H2 databases. If it is a database such as MySQL, you DO not need to write it.
@Data
@EqualsAndHashCode(callSuper = true)
public class UserRoleDO extends BaseDO {

    /**
     * auto-increment primary key
     */
    @TableId
    private Long id;
    /**
     * User ID
     */
    private Long userId;
    /**
     * Role ID
     */
    private Long roleId;

}
