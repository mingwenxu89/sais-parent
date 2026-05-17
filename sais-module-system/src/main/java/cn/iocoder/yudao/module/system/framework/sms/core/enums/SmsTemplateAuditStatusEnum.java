package cn.iocoder.yudao.module.system.framework.sms.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Review status enumeration of SMS templates
 *
 * @author Yudao Source Code
 */
@AllArgsConstructor
@Getter
public enum SmsTemplateAuditStatusEnum {

    CHECKING(1),
    SUCCESS(2),
    FAIL(3);

    private final Integer status;

}
