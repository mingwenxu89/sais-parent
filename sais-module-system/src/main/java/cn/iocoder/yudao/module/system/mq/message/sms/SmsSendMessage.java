package cn.iocoder.yudao.module.system.mq.message.sms;

import cn.iocoder.yudao.framework.common.core.KeyValue;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * Send message via SMS
 *
 * @author Yudao Source Code
 */
@Data
public class SmsSendMessage {

    /**
     * SMS log ID
     */
    @NotNull(message = "SMS log ID cannot be empty")
    private Long logId;
    /**
     * Mobile phone ID
     */
    @NotNull(message = "Mobile phone ID cannot be empty")
    private String mobile;
    /**
     * SMS channel ID
     */
    @NotNull(message = "SMS channel ID cannot be empty")
    private Long channelId;
    /**
     * SMS API template ID
     */
    @NotNull(message = "The template ID of SMS API cannot be empty")
    private String apiTemplateId;
    /**
     * SMS template parameters
     */
    private List<KeyValue<String, Object>> templateParams;

}
