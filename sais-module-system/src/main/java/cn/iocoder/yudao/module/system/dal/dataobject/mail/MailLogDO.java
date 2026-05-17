package cn.iocoder.yudao.module.system.dal.dataobject.mail;

import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.framework.mybatis.core.type.StringListTypeHandler;
import cn.iocoder.yudao.framework.tenant.core.aop.TenantIgnore;
import cn.iocoder.yudao.module.system.enums.mail.MailSendStatusEnum;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Mailbox log DO
 * Record every email sent
 *
 * @author wangjingyi
 * @since 2022-03-21
 */
@TableName(value = "system_mail_log", autoResultMap = true)
@KeySequence("system_mail_log_seq") // Primary key auto-increment for Oracle, PostgreSQL, Kingbase, DB2, H2 databases. If it is a database such as MySQL, you DO not need to write it.
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TenantIgnore
public class MailLogDO extends BaseDO implements Serializable {

    /**
     * Log ID, auto-increment
     */
    private Long id;

    /**
     * user code
     */
    private Long userId;
    /**
     * User type
     *
     * Enumeration {@link UserTypeEnum}
     */
    private Integer userType;

    /**
     * Receive email address
     */
    @TableField(typeHandler = StringListTypeHandler.class)
    private List<String> toMails;
    /**
     * Receive email address
     */
    @TableField(typeHandler = StringListTypeHandler.class)
    private List<String> ccMails;
    /**
     * Bcc email address
     */
    @TableField(typeHandler = StringListTypeHandler.class)
    private List<String> bccMails;

    /**
     * Email account ID
     *
     * Association {@link MailAccountDO#getId()}
     */
    private Long accountId;
    /**
     * Send email address
     *
     * Redundant {@link MailAccountDO#getMail()}
     */
    private String fromMail;

    // ========= Template related fields =========
    /**
     * Template ID
     *
     * Association {@link MailTemplateDO#getId()}
     */
    private Long templateId;
    /**
     * Template coding
     *
     * Redundant {@link MailTemplateDO#getCode()}
     */
    private String templateCode;
    /**
     * Template sender name
     *
     * Redundant {@link MailTemplateDO#getNickname()}
     */
    private String templateNickname;
    /**
     * Template title
     */
    private String templateTitle;
    /**
     * Template content
     *
     * Content formatted based on {@link MailTemplateDO#getContent()}
     */
    private String templateContent;
    /**
     * Template parameters
     *
     * Parameters entered based on {@link MailTemplateDO#getParams()}
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> templateParams;

    // ========= Send related fields =========
    /**
     * Send status
     *
     * Enumeration {@link MailSendStatusEnum}
     */
    private Integer sendStatus;
    /**
     * Send time
     */
    private LocalDateTime sendTime;
    /**
     * Send returned message ID
     */
    private String sendMessageId;
    /**
     * Send exception
     */
    private String sendException;

}
