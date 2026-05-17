package cn.iocoder.yudao.module.system.dal.dataobject.dept;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import cn.iocoder.yudao.module.system.dal.dataobject.user.AdminUserDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Department table
 *
 * @author ruoyi
 * @author Yudao Source Code
 */
@TableName("system_dept")
@KeySequence("system_dept_seq") // Primary key auto-increment for Oracle, PostgreSQL, Kingbase, DB2, H2 databases. If it is a database such as MySQL, you DO not need to write it.
@Data
@EqualsAndHashCode(callSuper = true)
public class DeptDO extends TenantBaseDO {

    public static final Long PARENT_ID_ROOT = 0L;

    /**
     * Department ID
     */
    @TableId
    private Long id;
    /**
     * Department name
     */
    private String name;
    /**
     * Parent department ID
     *
     * Link to {@link #id}
     */
    private Long parentId;
    /**
     * Display order
     */
    private Integer sort;
    /**
     * person in charge
     *
     * Association {@link AdminUserDO#getId()}
     */
    private Long leaderUserId;
    /**
     * Contact ID
     */
    private String phone;
    /**
     * Email
     */
    private String email;
    /**
     * Department status
     *
     * Enum {@link CommonStatusEnum}
     */
    private Integer status;

}
