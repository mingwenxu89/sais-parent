package cn.iocoder.yudao.module.system.dal.dataobject.mail;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.framework.tenant.core.aop.TenantIgnore;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Email account DO
 *
 * Purpose: Configure the account for sending email
 *
 * @author wangjingyi
 * @since 2022-03-21
 */
@TableName(value = "system_mail_account", autoResultMap = true)
@KeySequence("system_mail_account_seq") // Primary key auto-increment for Oracle, PostgreSQL, Kingbase, DB2, H2 databases. If it is a database such as MySQL, you DO not need to write it.
@Data
@EqualsAndHashCode(callSuper = true)
@TenantIgnore
public class MailAccountDO extends BaseDO {

    /**
     * primary key
     */
    @TableId
    private Long id;
    /**
     * Email
     */
    private String mail;

    /**
     * Username
     */
    private String username;
    /**
     * Password
     */
    private String password;
    /**
     * SMTP server domain name
     */
    private String host;
    /**
     * SMTP server port
     */
    private Integer port;
    /**
     * Whether to enable SSL
     */
    private Boolean sslEnable;
    /**
     * Whether to enable STARTTLS
     */
    private Boolean starttlsEnable;

}
