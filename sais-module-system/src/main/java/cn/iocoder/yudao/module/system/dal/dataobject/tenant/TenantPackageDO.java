package cn.iocoder.yudao.module.system.dal.dataobject.tenant;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.framework.tenant.core.aop.TenantIgnore;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.*;

import java.util.Set;

/**
 * Tenant Package DO
 *
 * @author Yudao Source Code
 */
@TableName(value = "system_tenant_package", autoResultMap = true)
@KeySequence("system_tenant_package_seq") // Primary key auto-increment for Oracle, PostgreSQL, Kingbase, DB2, H2 databases. If it is a database such as MySQL, you DO not need to write it.
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TenantIgnore
public class TenantPackageDO extends BaseDO {

    /**
     * Package ID, auto-increment
     */
    private Long id;
    /**
     * Package name, unique
     */
    private String name;
    /**
     * Tenant package status
     *
     * Enum {@link CommonStatusEnum}
     */
    private Integer status;
    /**
     * Remark
     */
    private String remark;
    /**
     * associated menu ID
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Set<Long> menuIds;

}
