package cn.iocoder.yudao.module.system.dal.dataobject.oauth2;

import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * OAuth2 approve DO
 *
 * When the user is in the sso.vue API, record the accepted scope list
 *
 * @author Yudao Source Code
 */
@TableName(value = "system_oauth2_approve", autoResultMap = true)
@KeySequence("system_oauth2_approve_seq") // Primary key auto-increment for Oracle, PostgreSQL, Kingbase, DB2, H2 databases. If it is a database such as MySQL, you DO not need to write it.
@Data
@EqualsAndHashCode(callSuper = true)
public class OAuth2ApproveDO extends BaseDO {

    /**
     * ID, automatically incremented by the database
     */
    @TableId
    private Long id;
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
     * client ID
     *
     * Association {@link OAuth2ClientDO#getId()}
     */
    private String clientId;
    /**
     * Authorization scope
     */
    private String scope;
    /**
     * DO you accept
     *
     * true - accept
     * false - reject
     */
    private Boolean approved;
    /**
     * Expiration time
     */
    private LocalDateTime expiresTime;

}
