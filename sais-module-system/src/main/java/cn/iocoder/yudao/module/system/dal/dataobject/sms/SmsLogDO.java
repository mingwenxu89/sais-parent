package cn.iocoder.yudao.module.system.dal.dataobject.sms;

import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.framework.tenant.core.aop.TenantIgnore;
import cn.iocoder.yudao.module.system.enums.sms.SmsReceiveStatusEnum;
import cn.iocoder.yudao.module.system.enums.sms.SmsSendStatusEnum;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * SMS log DO
 *
 * @author zzf
 * @since 2021-01-25
 */
@TableName(value = "system_sms_log", autoResultMap = true)
@KeySequence("system_sms_log_seq") // Primary key auto-increment for Oracle, PostgreSQL, Kingbase, DB2, H2 databases. If it is a database such as MySQL, you DO not need to write it.
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TenantIgnore
public class SmsLogDO extends BaseDO {

    /**
     * self-increasing ID
     */
    private Long id;

    // ========= Channel related fields =========

    /**
     * SMS channel ID
     *
     * Association {@link SmsChannelDO#getId()}
     */
    private Long channelId;
    /**
     * SMS channel code
     *
     * Redundant {@link SmsChannelDO#getCode()}
     */
    private String channelCode;

    // ========= Template related fields =========

    /**
     * Template ID
     *
     * Association {@link SmsTemplateDO#getId()}
     */
    private Long templateId;
    /**
     * template encoding
     *
     * Redundant {@link SmsTemplateDO#getCode()}
     */
    private String templateCode;
    /**
     * SMS type
     *
     * Redundant {@link SmsTemplateDO#getType()}
     */
    private Integer templateType;
    /**
     * Content formatted based on {@link SmsTemplateDO#getContent()}
     */
    private String templateContent;
    /**
     * Parameters entered based on {@link SmsTemplateDO#getParams()}
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> templateParams;
    /**
     * SMS API template ID
     *
     * Redundant {@link SmsTemplateDO#getApiTemplateId()}
     */
    private String apiTemplateId;

    // ========= Mobile phone related fields =========

    /**
     * Mobile phone ID
     */
    private String mobile;
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

    // ========= Send related fields =========

    /**
     * Send status
     *
     * Enum {@link SmsSendStatusEnum}
     */
    private Integer sendStatus;
    /**
     * Send time
     */
    private LocalDateTime sendTime;
    /**
     * Encoding of results sent by SMS API
     *
     * Since the third-party error code may be a string, the String type is used
     */
    private String apiSendCode;
    /**
     * SMS API sending failure prompt
     */
    private String apiSendMsg;
    /**
     * Unique request ID returned by SMS API send
     *
     * Used for positioning and troubleshooting with the SMS API
     */
    private String apiRequestId;
    /**
     * Serial ID returned by SMS API sending
     *
     * Used to associate with the sending records of the SMS API platform
     */
    private String apiSerialNo;

    // ========= Receive related fields =========

    /**
     * receiving status
     *
     * Enum {@link SmsReceiveStatusEnum}
     */
    private Integer receiveStatus;
    /**
     * Receiving time
     */
    private LocalDateTime receiveTime;
    /**
     * Encoding of SMS API reception results
     */
    private String apiReceiveCode;
    /**
     * Tips for SMS API receiving results
     */
    private String apiReceiveMsg;

}
