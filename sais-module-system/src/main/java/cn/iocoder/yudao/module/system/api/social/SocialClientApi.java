package cn.iocoder.yudao.module.system.api.social;

import cn.iocoder.yudao.module.system.api.social.dto.*;
import cn.iocoder.yudao.module.system.enums.social.SocialTypeEnum;

import jakarta.validation.Valid;

import java.util.List;

/**
 * Social application API API
 *
 * @author Yudao Source Code
 */
public interface SocialClientApi {

    /**
     * Obtain the authorized URL of the social platform
     *
     * @param socialType  Type of social platform {@link SocialTypeEnum}
     * @param userType    User type
     * @param redirectUri Redirect URL
     * @return Authorization URL for social platforms
     */
    String getAuthorizeUrl(Integer socialType, Integer userType, String redirectUri);

    /**
     * Signature required to create WeChat official account JS SDK initialization
     *
     * @param userType User type
     * @param url      Visited URL address
     * @return signature
     */
    SocialWxJsapiSignatureRespDTO createWxMpJsapiSignature(Integer userType, String url);

    //======================= Exclusive to WeChat miniapp =======================

    /**
     * Get mobile phone information from WeChat applet
     *
     * @param userType  User type
     * @param phoneCode Mobile authorization code
     * @return Mobile phone information
     */
    SocialWxPhoneNumberInfoRespDTO getWxMaPhoneNumberInfo(Integer userType, String phoneCode);

    /**
     * Obtain the QR code of the miniapp
     *
     * @param reqVO request information
     * @return Mini program QR code
     */
    byte[] getWxaQrcode(@Valid SocialWxQrcodeReqDTO reqVO);

    /**
     * Get WeChat Xiaocheng subscription template
     *
     * @return Mini program subscription message template
     */
    List<SocialWxaSubscribeTemplateRespDTO> getWxaSubscribeTemplateList(Integer userType);

    /**
     * Send WeChat applet subscription messages
     *
     * @param reqDTO Request
     */
    void sendWxaSubscribeMessage(SocialWxaSubscribeMessageSendReqDTO reqDTO);

    /**
     * Upload orders and ship them to WeChat miniapp
     *
     * @param userType User type
     * @param reqDTO Request
     */
    void uploadWxaOrderShippingInfo(Integer userType, SocialWxaOrderUploadShippingInfoReqDTO reqDTO);

    /**
     * Notify order receipt to WeChat applet
     *
     * @param userType User type
     * @param reqDTO Request
     */
    void notifyWxaOrderConfirmReceive(Integer userType, SocialWxaOrderNotifyConfirmReceiveReqDTO reqDTO);

}
