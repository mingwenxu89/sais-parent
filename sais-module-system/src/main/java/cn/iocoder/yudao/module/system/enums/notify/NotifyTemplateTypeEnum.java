package cn.iocoder.yudao.module.system.enums.notify;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Notification template type enum
 *
 * @author HUIHUI
 */
@Getter
@AllArgsConstructor
public enum NotifyTemplateTypeEnum {

    /**
     * System messages
     */
    SYSTEM_MESSAGE(2),
    /**
     * notification message
     */
    NOTIFICATION_MESSAGE(1);

    private final Integer type;

}
