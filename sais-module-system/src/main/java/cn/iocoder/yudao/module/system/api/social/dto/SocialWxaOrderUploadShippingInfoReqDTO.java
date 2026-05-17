package cn.iocoder.yudao.module.system.api.social.dto;

import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * Upload shopping details for miniapp orders
 *
 * @see <a href="https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/shopping-order/normal-shopping-detail/uploadShoppingInfo.html">Upload shopping details</a>
 * @author Yudao Source Code
 */
@Data
public class SocialWxaOrderUploadShippingInfoReqDTO {

    /**
     * Logistics model - physical logistics distribution adopts the form of physical logistics distribution by express companies
     */
    public static final Integer LOGISTICS_TYPE_EXPRESS = 1;
    /**
     * Logistics model - virtual goods, virtual goods, such as phone recharge, point cards, etc., no physical delivery form
     */
    public static final Integer LOGISTICS_TYPE_VIRTUAL = 3;
    /**
     * Logistics model - user pick-up
     */
    public static final Integer LOGISTICS_TYPE_PICK_UP = 4;

    /**
     * Payer, payer information (openid)
     */
    @NotEmpty(message = "Payer, payer information (openid) cannot be empty")
    private String openid;

    /**
     * WeChat order ID corresponding to the original payment transaction
     */
    @NotEmpty(message = "The WeChat order ID corresponding to the original payment transaction cannot be empty.")
    private String transactionId;

    /**
     * Logistics model
     */
    @NotNull(message = "Logistics mode cannot be empty")
    private Integer logisticsType;
    /**
     * Logistics delivery order ID
     */
    private String logisticsNo;
    /**
     * Logistics company ID
     *
     * @see <a href="https://developers.weixin.qq.com/miniprogram/dev/platform-capabilities/industry/express/business/express_search.html#%E8%8E%B7%E5%8F%96%E8%BF%90%E5%8A%9Bid%E5%88%97%E8%A1%A8get-delivery-list">Introduction to the logistics query plug-in</a>
     */
    private String expressCompany;
    /**
     * Product information
     */
    @NotEmpty(message = "Product information cannot be empty")
    private String itemDesc;
    /**
     * Recipient’s mobile phone ID
     */
    @NotEmpty(message = "Recipient’s mobile phone ID")
    private String receiverContact;

}
