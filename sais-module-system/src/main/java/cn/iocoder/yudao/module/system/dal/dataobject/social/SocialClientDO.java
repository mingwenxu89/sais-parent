package cn.iocoder.yudao.module.system.dal.dataobject.social;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import cn.iocoder.yudao.module.system.enums.social.SocialTypeEnum;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import me.zhyd.oauth.config.AuthConfig;

/**
 * Social client DO
 *
 * Corresponding to the {@link AuthConfig} configuration, it can satisfy different tenants and has its own client configuration to achieve social (three-party) login.
 *
 * @author Yudao Source Code
 */
@TableName(value = "system_social_client", autoResultMap = true)
@KeySequence("system_social_client_seq") // Primary key auto-increment for Oracle, PostgreSQL, Kingbase, DB2, H2 databases. If it is a database such as MySQL, you DO not need to write it.
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SocialClientDO extends TenantBaseDO {

    /**
     * ID, auto-increment
     */
    @TableId
    private Long id;
    /**
     * Application name
     */
    private String name;
    /**
     * social type
     *
     * Enumeration {@link SocialTypeEnum}
     */
    private Integer socialType;
    /**
     * User type
     *
     * Purpose: Different user types correspond to different miniapps and require their own configuration.
     *
     * Enumeration {@link UserTypeEnum}
     */
    private Integer userType;
    /**
     * Status
     *
     * Enum {@link CommonStatusEnum}
     */
    private Integer status;

    /**
     * client id
     */
    private String clientId;
    /**
     * Client Secret
     */
    private String clientSecret;

    /**
     * Agent ID
     *
     * Currently only some "social types" are in use:
     * 1. Enterprise WeChat: corresponding to the authorized party’s web application ID
     */
    private String agentId;

    /**
     * publicKey public key
     *
     * Currently only some "social types" are in use:
     * 1. Alipay: Alipay public key
     */
    private String publicKey;

}
