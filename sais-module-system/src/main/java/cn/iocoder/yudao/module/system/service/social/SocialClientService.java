package cn.iocoder.yudao.module.system.service.social;

import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.system.api.social.dto.SocialWxQrcodeReqDTO;
import cn.iocoder.yudao.module.system.api.social.dto.SocialWxaOrderNotifyConfirmReceiveReqDTO;
import cn.iocoder.yudao.module.system.api.social.dto.SocialWxaOrderUploadShippingInfoReqDTO;
import cn.iocoder.yudao.module.system.api.social.dto.SocialWxaSubscribeMessageSendReqDTO;
import cn.iocoder.yudao.module.system.controller.admin.socail.vo.client.SocialClientPageReqVO;
import cn.iocoder.yudao.module.system.controller.admin.socail.vo.client.SocialClientSaveReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.social.SocialClientDO;
import cn.iocoder.yudao.module.system.enums.social.SocialTypeEnum;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.bean.subscribemsg.TemplateInfo;
import me.zhyd.oauth.model.AuthUser;

import jakarta.validation.Valid;
import java.util.List;

/**
 * Social application service API
 *
 * @author Yudao Source Code
 */
public interface SocialClientService {

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
     * Request social platform, authorized user
     *
     * @param socialType Types of social platforms
     * @param userType   User type
     * @param code       Authorization code
     * @param state      authorization state
     * @return authorized user
     */
    AuthUser getAuthUser(Integer socialType, Integer userType, String code, String state);

    // =================== Exclusive to WeChat official account ===================

    /**
     * Signature required for JS SDK initialization to create WeChat official account
     *
     * @param userType User type
     * @param url      Visited URL address
     * @return signature
     */
    WxJsapiSignature createWxMpJsapiSignature(Integer userType, String url);

    // =================== Exclusive to WeChat Mini Program ===================

    /**
     * Get mobile phone information from WeChat applet
     *
     * @param userType  User type
     * @param phoneCode Mobile authorization code
     * @return Mobile phone information
     */
    WxMaPhoneNumberInfo getWxMaPhoneNumberInfo(Integer userType, String phoneCode);

    /**
     * Obtain the QR code of the miniapp
     *
     * @param reqVO request information
     * @return Mini program QR code
     */
    byte[] getWxaQrcode(SocialWxQrcodeReqDTO reqVO);

    /**
     * Get WeChat Xiaocheng subscription template
     *
     * Purpose of caching: Considering that the WeChat applet subscription message rarely changes after selecting a template, caching increases query efficiency.
     *
     * @param userType User type
     * @return WeChat Xiaocheng Subscription Template
     */
    List<TemplateInfo> getSubscribeTemplateList(Integer userType);

    /**
     * Send WeChat applet subscription messages
     *
     * @param reqDTO     Request
     * @param templateId Template ID
     * @param openId     Member openId
     */
    void sendSubscribeMessage(SocialWxaSubscribeMessageSendReqDTO reqDTO, String templateId, String openId);

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

    // =================== Client Management ===================

    /**
     * Create a social client
     *
     * @param createReqVO Create information
     * @return ID
     */
    Long createSocialClient(@Valid SocialClientSaveReqVO createReqVO);

    /**
     * Update social client
     *
     * @param updateReqVO Update information
     */
    void updateSocialClient(@Valid SocialClientSaveReqVO updateReqVO);

    /**
     * Delete social client
     *
     * @param id ID
     */
    void deleteSocialClient(Long id);

    /**
     * Delete social clients in batches
     *
     * @param ids IDed array
     */
    void deleteSocialClientList(List<Long> ids);

    /**
     * Get social client
     *
     * @param id ID
     * @return social client
     */
    SocialClientDO getSocialClient(Long id);

    /**
     * Get social client pagination
     *
     * @param pageReqVO Page query
     * @return Social client pagination
     */
    PageResult<SocialClientDO> getSocialClientPage(SocialClientPageReqVO pageReqVO);

}
