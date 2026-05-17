package cn.iocoder.yudao.module.system.dal.dataobject.tenant;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.framework.mybatis.core.type.StringListTypeHandler;
import cn.iocoder.yudao.framework.tenant.core.aop.TenantIgnore;
import cn.iocoder.yudao.module.system.dal.dataobject.user.AdminUserDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Tenant DO
 *
 * @author Yudao Source Code
 */
@TableName(value = "system_tenant", autoResultMap = true)
@KeySequence("system_tenant_seq") // Primary key auto-increment for Oracle, PostgreSQL, Kingbase, DB2, H2 databases. If it is a database such as MySQL, you DO not need to write it.
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TenantIgnore
public class TenantDO extends BaseDO {

    /**
     * Package ID - System
     */
    public static final Long PACKAGE_ID_SYSTEM = 0L;

    /**
     * Tenant ID, self-incrementing
     */
    private Long id;
    /**
     * Tenant name, unique
     */
    private String name;
    /**
     * Contact user ID
     *
     * Association {@link AdminUserDO#getId()}
     */
    private Long contactUserId;
    /**
     * Contact person
     */
    private String contactName;
    /**
     * Contact mobile phone
     */
    private String contactMobile;
    /**
     * Tenant status
     *
     * Enum {@link CommonStatusEnum}
     */
    private Integer status;
    /**
     * Bind domain name list
     *
     * 1. Considering the compatibility with WeChat applet, appid is also allowed to be passed.
     * 2. Why is it an array? Considering that the management backend and member frontend have independent domain names, or there are multiple management backends.
     */
    @TableField(typeHandler = StringListTypeHandler.class)
    private List<String> websites;
    /**
     * Tenant Package ID
     *
     * Association {@link TenantPackageDO#getId()}
     * Special logic: The system has built-in tenants, does not use packages, and temporarily uses the {@link #PACKAGE_ID_SYSTEM} identifier.
     */
    private Long packageId;
    /**
     * Expiration time
     */
    private LocalDateTime expireTime;
    /**
     * ID of accounts
     */
    private Integer accountCount;

}
