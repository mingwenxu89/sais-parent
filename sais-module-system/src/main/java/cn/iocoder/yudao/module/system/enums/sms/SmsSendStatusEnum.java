package cn.iocoder.yudao.module.system.enums.sms;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * SMS sending status enumeration
 *
 * @author zzf
 * @date 2021/2/1 13:39
 */
@Getter
@AllArgsConstructor
public enum SmsSendStatusEnum {

    INIT(0), // initialization
    SUCCESS(10), // Sent successfully
    FAILURE(20), // Sending failed
    IGNORE(30), // Ignore, that is, DO not send
    ;

    private final int status;

}
