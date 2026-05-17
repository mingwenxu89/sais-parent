package cn.iocoder.yudao.module.system.enums.notice;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * notification type
 *
 * @author Yudao Source Code
 */
@Getter
@AllArgsConstructor
public enum NoticeTypeEnum {

    NOTICE(1),
    ANNOUNCEMENT(2);

    /**
     * Type
     */
    private final Integer type;

}
