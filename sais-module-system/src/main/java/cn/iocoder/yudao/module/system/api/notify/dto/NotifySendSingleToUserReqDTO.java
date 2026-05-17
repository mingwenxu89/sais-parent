package cn.iocoder.yudao.module.system.api.notify.dto;

import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.Map;

/**
 * In-site messages are sent to Admin or Member users
 *
 * @author xrcoder
 */
@Data
public class NotifySendSingleToUserReqDTO {

    /**
     * User ID
     */
    @NotNull(message = "User ID cannot be empty")
    private Long userId;

    /**
     * Site letter template ID
     */
    @NotEmpty(message = "The site letter template ID cannot be empty")
    private String templateCode;

    /**
     * Site letter template parameters
     */
    private Map<String, Object> templateParams;
}
