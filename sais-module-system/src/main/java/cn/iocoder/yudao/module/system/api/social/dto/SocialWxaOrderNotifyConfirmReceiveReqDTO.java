package cn.iocoder.yudao.module.system.api.social.dto;

import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Upload shopping details for miniapp orders
 *
 * @see <a href="https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/shopping-order/normal-shopping-detail/uploadShoppingInfo.html">Upload shopping details</a>
 * @author Yudao Source Code
 */
@Data
public class SocialWxaOrderNotifyConfirmReceiveReqDTO {

    /**
     * WeChat order ID corresponding to the original payment transaction
     */
    @NotEmpty(message = "The WeChat order ID corresponding to the original payment transaction cannot be empty.")
    private String transactionId;

    /**
     * Express receipt time
     */
    @NotNull(message = "Express receipt time cannot be empty")
    private LocalDateTime receivedTime;

}
