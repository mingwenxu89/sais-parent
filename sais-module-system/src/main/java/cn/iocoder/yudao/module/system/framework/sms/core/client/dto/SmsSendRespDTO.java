package cn.iocoder.yudao.module.system.framework.sms.core.client.dto;

import lombok.Data;

/**
 * Send SMS Response DTO
 *
 * @author Yudao Source Code
 */
@Data
public class SmsSendRespDTO {

    /**
     * Is it successful?
     */
    private Boolean success;

    /**
     * API request ID
     */
    private String apiRequestId;

    // ==================== Fields on success ====================

    /**
     * Serial ID returned by SMS API sending
     */
    private String serialNo;

    // ==================== Fields on failure ====================

    /**
     * API return error code
     *
     * Since the third-party error code may be a string, the String type is used
     */
    private String apiCode;
    /**
     * API return prompt
     */
    private String apiMsg;

}
