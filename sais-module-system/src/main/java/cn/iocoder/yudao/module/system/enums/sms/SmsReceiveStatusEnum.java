package cn.iocoder.yudao.module.system.enums.sms;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * SMS receiving status enumeration
 *
 * @author Yudao Source Code
 * @date 2021/2/1 13:39
 */
@Getter
@AllArgsConstructor
public enum SmsReceiveStatusEnum {

    INIT(0), // initialization
    SUCCESS(10), // Received successfully
    FAILURE(20), // Reception failed
    ;

    private final int status;

}
