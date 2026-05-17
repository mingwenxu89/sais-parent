package cn.iocoder.yudao.module.system.framework.sms.core.client.dto;

import cn.iocoder.yudao.module.system.framework.sms.core.enums.SmsTemplateAuditStatusEnum;
import lombok.Data;

/**
 * SMS Template Response DTO
 *
 * @author Yudao Source Code
 */
@Data
public class SmsTemplateRespDTO {

    /**
     * Template ID
     */
    private String id;
    /**
     * SMS content
     */
    private String content;
    /**
     * Review status
     *
     * Enum {@link SmsTemplateAuditStatusEnum}
     */
    private Integer auditStatus;
    /**
     * Reason for failure of review
     */
    private String auditReason;

}
