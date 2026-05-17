package cn.iocoder.yudao.module.system.dal.dataobject.notify;

import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.module.system.dal.dataobject.mail.MailTemplateDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

/**
 * Site message DO
 *
 * @author xrcoder
 */
@TableName(value = "system_notify_message", autoResultMap = true)
@KeySequence("system_notify_message_seq") // Primary key auto-increment for Oracle, PostgreSQL, Kingbase, DB2, H2 databases. If it is a database such as MySQL, you DO not need to write it.
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotifyMessageDO extends BaseDO {

    /**
     * Site message ID, self-increment
     */
    @TableId
    private Long id;
    /**
     * User ID
     *
     * Associate the id field of MemberUserDO or the id field of AdminUserDO
     */
    private Long userId;
    /**
     * User type
     *
     * Enumeration {@link UserTypeEnum}
     */
    private Integer userType;

    // ========= Template related fields =========

    /**
     * Template ID
     *
     * Association {@link NotifyTemplateDO#getId()}
     */
    private Long templateId;
    /**
     * Template coding
     *
     * Association {@link NotifyTemplateDO#getCode()}
     */
    private String templateCode;
    /**
     * template type
     *
     * Redundant {@link NotifyTemplateDO#getType()}
     */
    private Integer templateType;
    /**
     * Template sender name
     *
     * Redundant {@link NotifyTemplateDO#getNickname()}
     */
    private String templateNickname;
    /**
     * Template content
     *
     * Content formatted based on {@link NotifyTemplateDO#getContent()}
     */
    private String templateContent;
    /**
     * Template parameters
     *
     * Parameters entered based on {@link NotifyTemplateDO#getParams()}
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> templateParams;

    // ========= Read related fields =========

    /**
     * Has it been read?
     */
    private Boolean readStatus;
    /**
     * reading time
     */
    private LocalDateTime readTime;

}
