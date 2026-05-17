package cn.iocoder.yudao.module.system.api.sms.dto.code;

import cn.iocoder.yudao.framework.common.validation.InEnum;
import cn.iocoder.yudao.framework.common.validation.Mobile;
import cn.iocoder.yudao.module.system.enums.sms.SmsSceneEnum;
import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * Sending SMS captcha Request DTO
 *
 * @author Yudao Source Code
 */
@Data
public class SmsCodeSendReqDTO {

    /**
     * Mobile phone ID
     */
    @Mobile
    @NotEmpty(message = "Mobile phone ID cannot be empty")
    private String mobile;
    /**
     * Send scene
     */
    @NotNull(message = "The sending scene cannot be empty")
    @InEnum(SmsSceneEnum.class)
    private Integer scene;
    /**
     * Send IP
     */
    @NotEmpty(message = "Sending IP cannot be empty")
    private String createIp;

}
