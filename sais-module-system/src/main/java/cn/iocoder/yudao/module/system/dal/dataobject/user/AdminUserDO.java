package cn.iocoder.yudao.module.system.dal.dataobject.user;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import cn.iocoder.yudao.module.system.enums.common.SexEnum;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * Manage background user DO
 *
 * @author Yudao Source Code
 */
@TableName(value = "system_users", autoResultMap = true) // Since system_user of SQL Server is a keyword, use system_users
@KeySequence("system_users_seq") // Primary key auto-increment for Oracle, PostgreSQL, Kingbase, DB2, H2 databases. If it is a database such as MySQL, you DO not need to write it.
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserDO extends TenantBaseDO {

    /**
     * User ID
     */
    @TableId
    private Long id;
    /**
     * User account
     */
    private String username;
    /**
     * Encrypted password
     *
     * Since you currently use the {@link BCryptPasswordEncoder} encryptor, you don't need to handle the salt yourself
     */
    private String password;
    /**
     * User nickname
     */
    private String nickname;
    /**
     * Remark
     */
    private String remark;
    /**
     * Department ID
     */
    private Long deptId;
    /**
     * Position ID array
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Set<Long> postIds;
    /**
     * User email
     */
    private String email;
    /**
     * Mobile phone ID
     */
    private String mobile;
    /**
     * User gender
     *
     * Enum class {@link SexEnum}
     */
    private Integer sex;
    /**
     * User avatar
     */
    private String avatar;
    /**
     * Account status
     *
     * Enum {@link CommonStatusEnum}
     */
    private Integer status;
    /**
     * Last login IP
     */
    private String loginIp;
    /**
     * Last login time
     */
    private LocalDateTime loginDate;

}
