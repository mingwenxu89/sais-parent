package cn.iocoder.yudao.module.system.api.sms.dto.send;

import cn.iocoder.yudao.framework.common.validation.Mobile;
import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import java.util.Map;

/**
 * Send SMS to Admin or Member users
 *
 * @author Yudao Source Code
 */
@Data
public class SmsSendSingleToUserReqDTO {

    /**
     * User ID
     */
    private Long userId;
    /**
     * Mobile phone ID
     */
    @Mobile
    private String mobile;
    /**
     * SMS template ID
     */
    @NotEmpty(message = "SMS template ID cannot be empty")
    private String templateCode;
    /**
     * SMS template parameters
     */
    private Map<String, Object> templateParams;

}
