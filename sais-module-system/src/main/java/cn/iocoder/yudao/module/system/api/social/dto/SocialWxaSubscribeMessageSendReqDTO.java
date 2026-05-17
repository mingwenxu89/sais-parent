package cn.iocoder.yudao.module.system.api.social.dto;

import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

/**
 * WeChat applet subscription message sending Request DTO
 *
 * @author HUIHUI
 */
@Data
public class SocialWxaSubscribeMessageSendReqDTO {

    /**
     * User ID
     *
     * The id ID associated with MemberUserDO
     * The id ID associated with AdminUserDO
     */
    @NotNull(message = "User ID cannot be empty")
    private Long userId;
    /**
     * User type
     *
     * Association {@link UserTypeEnum}
     */
    @NotNull(message = "User type cannot be empty")
    private Integer userType;

    /**
     * Message template title
     */
    @NotEmpty(message = "Message template title cannot be empty")
    private String templateTitle;

    /**
     * The jump page after clicking the template card is limited to pages within this miniapp.
     *
     * Supports parameters, (example index?foo=bar). If this field is not filled in, the template will not jump.
     */
    private String page;

    /**
     * Template content parameters
     */
    private Map<String, String> messages;

    public SocialWxaSubscribeMessageSendReqDTO addMessage(String key, String value) {
        if (messages == null) {
            messages = new HashMap<>();
        }
        messages.put(key, value);
        return this;
    }

}
