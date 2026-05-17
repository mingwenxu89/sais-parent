package cn.iocoder.yudao.module.system.dal.dataobject.oauth2;

import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

/**
 * OAuth2 authorization code DO
 *
 * @author Yudao Source Code
 */
@TableName(value = "system_oauth2_code", autoResultMap = true)
@KeySequence("system_oauth2_code_seq") // Primary key auto-increment for Oracle, PostgreSQL, Kingbase, DB2, H2 databases. If it is a database such as MySQL, you DO not need to write it.
@Data
@EqualsAndHashCode(callSuper = true)
public class OAuth2CodeDO extends BaseDO {

    /**
     * ID, database increments
     */
    private Long id;
    /**
     * Authorization code
     */
    private String code;
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
     * Association {@link OAuth2ClientDO#getClientId()}
     */
    private String clientId;
    /**
     * Authorization scope
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> scopes;
    /**
     * redirect address
     */
    private String redirectUri;
    /**
     * Status
     */
    private String state;
    /**
     * Expiration time
     */
    private LocalDateTime expiresTime;

}
