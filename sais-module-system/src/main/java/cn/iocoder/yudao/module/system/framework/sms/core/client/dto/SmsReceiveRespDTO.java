package cn.iocoder.yudao.module.system.framework.sms.core.client.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Message receiving Response DTO
 *
 * @author Yudao Source Code
 */
@Data
public class SmsReceiveRespDTO {

    /**
     * Whether the reception is successful
     */
    private Boolean success;
    /**
     * Encoding of API received results
     */
    private String errorCode;
    /**
     * Description of API receiving results
     */
    private String errorMsg;

    /**
     * Mobile phone ID
     */
    private String mobile;
    /**
     * User reception time
     */
    private LocalDateTime receiveTime;

    /**
     * Serial ID returned by SMS API sending
     */
    private String serialNo;
    /**
     * SMS log ID
     *
     * ID corresponding to SysSmsLogDO
     */
    private Long logId;

}
