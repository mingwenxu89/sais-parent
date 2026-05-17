package cn.iocoder.yudao.module.system.dal.dataobject.permission;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import cn.iocoder.yudao.module.system.enums.permission.DataScopeEnum;
import cn.iocoder.yudao.module.system.enums.permission.RoleTypeEnum;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

/**
 * Role DO
 *
 * @author ruoyi
 */
@TableName(value = "system_role", autoResultMap = true)
@KeySequence("system_role_seq") // Primary key auto-increment for Oracle, PostgreSQL, Kingbase, DB2, H2 databases. If it is a database such as MySQL, you DO not need to write it.
@Data
@EqualsAndHashCode(callSuper = true)
public class RoleDO extends TenantBaseDO {

    /**
     * Role ID
     */
    @TableId
    private Long id;
    /**
     * Character name
     */
    private String name;
    /**
     * Role ID
     *
     * enumeration
     */
    private String code;
    /**
     * Role sorting
     */
    private Integer sort;
    /**
     * character status
     *
     * Enum {@link CommonStatusEnum}
     */
    private Integer status;
    /**
     * role type
     *
     * Enum {@link RoleTypeEnum}
     */
    private Integer type;
    /**
     * Remark
     */
    private String remark;

    /**
     * data range
     *
     * Enum {@link DataScopeEnum}
     */
    private Integer dataScope;
    /**
     * Data range (specified department array)
     *
     * Applies when the value of {@link #dataScope} is {@link DataScopeEnum#DEPT_CUSTOM}
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Set<Long> dataScopeDeptIds;

}
