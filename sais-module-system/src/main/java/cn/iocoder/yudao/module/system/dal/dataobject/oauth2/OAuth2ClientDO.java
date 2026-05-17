package cn.iocoder.yudao.module.system.dal.dataobject.oauth2;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.framework.tenant.core.aop.TenantIgnore;
import cn.iocoder.yudao.module.system.enums.oauth2.OAuth2GrantTypeEnum;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * OAuth2 client DO
 *
 * @author Yudao Source Code
 */
@TableName(value = "system_oauth2_client", autoResultMap = true)
@KeySequence("system_oauth2_client_seq") // Primary key auto-increment for Oracle, PostgreSQL, Kingbase, DB2, H2 databases. If it is a database such as MySQL, you DO not need to write it.
@Data
@EqualsAndHashCode(callSuper = true)
@TenantIgnore
public class OAuth2ClientDO extends BaseDO {

    /**
     * ID, automatically incremented by the database
     *
     * Since SQL Server has some problems storing String primary keys, Long type is used for the time being.
     */
    @TableId
    private Long id;
    /**
     * client ID
     */
    private String clientId;
    /**
     * client key
     */
    private String secret;
    /**
     * Application name
     */
    private String name;
    /**
     * application icon
     */
    private String logo;
    /**
     * Application description
     */
    private String description;
    /**
     * Status
     *
     * Enum {@link CommonStatusEnum}
     */
    private Integer status;
    /**
     * Access token validity period
     */
    private Integer accessTokenValiditySeconds;
    /**
     * Refresh token validity period
     */
    private Integer refreshTokenValiditySeconds;
    /**
     * Redirectable URI address
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> redirectUris;
    /**
     * Authorization type (mode)
     *
     * Enum {@link OAuth2GrantTypeEnum}
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> authorizedGrantTypes;
    /**
     * Authorization scope
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> scopes;
    /**
     * Scope for automatic authorization
     *
     * When authorizing code, if the scope is within this scope, it will automatically pass
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> autoApproveScopes;
    /**
     * Permission
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> authorities;
    /**
     * Resources
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> resourceIds;
    /**
     * Additional information, JSON format
     */
    private String additionalInformation;

}
