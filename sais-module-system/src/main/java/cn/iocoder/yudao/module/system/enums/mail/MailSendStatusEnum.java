package cn.iocoder.yudao.module.system.enums.mail;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Email sending status enumeration
 *
 * @author wangjingyi
 * @since 2022/4/10 13:39
 */
@Getter
@AllArgsConstructor
public enum MailSendStatusEnum {

    INIT(0), // initialization
    SUCCESS(10), // Sent successfully
    FAILURE(20), // Sending failed
    IGNORE(30), // Ignore, that is, DO not send
    ;

    private final int status;

}
