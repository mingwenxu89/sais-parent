package cn.iocoder.yudao.module.system.dal.dataobject.logger;

import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.module.system.enums.logger.LoginLogTypeEnum;
import cn.iocoder.yudao.module.system.enums.logger.LoginResultEnum;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Login log table
 *
 * Note, including both login and logout behaviors
 *
 * @author Yudao Source Code
 */
@TableName("system_login_log")
@KeySequence("system_login_log_seq") // Primary key auto-increment for Oracle, PostgreSQL, Kingbase, DB2, H2 databases. If it is a database such as MySQL, you DO not need to write it.
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class LoginLogDO extends BaseDO {

    /**
     * Log primary key
     */
    private Long id;
    /**
     * Log type
     *
     * Enum {@link LoginLogTypeEnum}
     */
    private Integer logType;
    /**
     * link tracking ID
     */
    private String traceId;
    /**
     * User ID
     */
    private Long userId;
    /**
     * User type
     *
     * Enumeration {@link UserTypeEnum}
     */
    private Integer userType;
    /**
     * User account
     *
     * Redundant because accounts can be changed
     */
    private String username;
    /**
     * Login results
     *
     * Enum {@link LoginResultEnum}
     */
    private Integer result;
    /**
     * User IP
     */
    private String userIp;
    /**
     * Browser UA
     */
    private String userAgent;

}
