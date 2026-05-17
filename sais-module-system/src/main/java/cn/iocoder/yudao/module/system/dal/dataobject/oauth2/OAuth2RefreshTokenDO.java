package cn.iocoder.yudao.module.system.dal.dataobject.oauth2;

import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * OAuth2 refresh token
 *
 * @author Yudao Source Code
 */
@TableName(value = "system_oauth2_refresh_token", autoResultMap = true)
// Since the name length of Oracle's SEQ is limited, let's use system_oauth2_access_token_seq first. There is no problem anyway.
@KeySequence("system_oauth2_access_token_seq") // Primary key auto-increment for Oracle, PostgreSQL, Kingbase, DB2, H2 databases. If it is a database such as MySQL, you DO not need to write it.
@Data
public class OAuth2RefreshTokenDO extends TenantBaseDO {

    /**
     * ID, database dict
     */
    private Long id;
    /**
     * Refresh Token
     */
    private String refreshToken;
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
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> scopes;
    /**
     * Expiration time
     */
    private LocalDateTime expiresTime;

}
