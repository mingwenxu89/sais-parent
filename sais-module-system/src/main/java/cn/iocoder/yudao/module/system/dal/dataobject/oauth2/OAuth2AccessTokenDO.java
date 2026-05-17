package cn.iocoder.yudao.module.system.dal.dataobject.oauth2;

import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * OAuth2 access token DO
 *
 * The following fields are currently unused and not supported:
 * user_name, authentication (user information)
 *
 * @author Yudao Source Code
 */
@TableName(value = "system_oauth2_access_token", autoResultMap = true)
@KeySequence("system_oauth2_access_token_seq") // Primary key auto-increment for Oracle, PostgreSQL, Kingbase, DB2, H2 databases. If it is a database such as MySQL, you DO not need to write it.
@Data
@EqualsAndHashCode(callSuper = true)
public class OAuth2AccessTokenDO extends TenantBaseDO {

    /**
     * ID, database increments
     */
    @TableId
    private Long id;
    /**
     * Access Token
     */
    private String accessToken;
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
     * User information
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String, String> userInfo;
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
