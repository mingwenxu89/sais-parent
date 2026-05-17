package cn.iocoder.yudao.module.system.api.social;

import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.system.api.social.dto.*;
import cn.iocoder.yudao.module.system.enums.social.SocialTypeEnum;
import cn.iocoder.yudao.module.system.service.social.SocialClientService;
import cn.iocoder.yudao.module.system.service.social.SocialUserService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.bean.subscribemsg.TemplateInfo;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;
import java.util.List;

import static cn.hutool.core.collection.CollUtil.findOne;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;

/**
 * API implementation class for social applications
 *
 * @author Yudao Source Code
 */
@Service
@Validated
@Slf4j
public class SocialClientApiImpl implements SocialClientApi {

    @Resource
    private SocialClientService socialClientService;
    @Resource
    private SocialUserService socialUserService;

    @Override
    public String getAuthorizeUrl(Integer socialType, Integer userType, String redirectUri) {
        return socialClientService.getAuthorizeUrl(socialType, userType, redirectUri);
    }

    @Override
    public SocialWxJsapiSignatureRespDTO createWxMpJsapiSignature(Integer userType, String url) {
        WxJsapiSignature signature = socialClientService.createWxMpJsapiSignature(userType, url);
        return BeanUtils.toBean(signature, SocialWxJsapiSignatureRespDTO.class);
    }

    //======================= Exclusive to WeChat miniapp =======================

    @Override
    public SocialWxPhoneNumberInfoRespDTO getWxMaPhoneNumberInfo(Integer userType, String phoneCode) {
        WxMaPhoneNumberInfo info = socialClientService.getWxMaPhoneNumberInfo(userType, phoneCode);
        return BeanUtils.toBean(info, SocialWxPhoneNumberInfoRespDTO.class);
    }

    @Override
    public byte[] getWxaQrcode(SocialWxQrcodeReqDTO reqVO) {
        return socialClientService.getWxaQrcode(reqVO);
    }

    @Override
    public List<SocialWxaSubscribeTemplateRespDTO> getWxaSubscribeTemplateList(Integer userType) {
        List<TemplateInfo> list = socialClientService.getSubscribeTemplateList(userType);
        return convertList(list, item -> BeanUtils.toBean(item, SocialWxaSubscribeTemplateRespDTO.class).setId(item.getPriTmplId()));
    }

    @Override
    public void sendWxaSubscribeMessage(SocialWxaSubscribeMessageSendReqDTO reqDTO) {
        // 1.1 Get subscription template list
        List<TemplateInfo> templateList = socialClientService.getSubscribeTemplateList(reqDTO.getUserType());
        if (CollUtil.isEmpty(templateList)) {
            log.warn("[sendSubscribeMessage][reqDTO({}) failed to send subscription message, reason: no subscription template found]", reqDTO);
            return;
        }
        // 1.2 Obtain the template you need to use
        TemplateInfo template = findOne(templateList, item ->
                ObjUtil.equal(item.getTitle(), reqDTO.getTemplateTitle()));
        if (template == null) {
            log.warn("[sendWxaSubscribeMessage][reqDTO({}) failed to send subscription message, reason: no subscription template found]", reqDTO);
            return;
        }

        // 2. Get social users
        SocialUserRespDTO socialUser = socialUserService.getSocialUserByUserId(reqDTO.getUserType(), reqDTO.getUserId(),
                SocialTypeEnum.WECHAT_MINI_PROGRAM.getType());
        if (ObjUtil.isNull(socialUser) || StrUtil.isBlankIfStr(socialUser.getOpenid())) {
            log.warn("[sendWxaSubscribeMessage][reqDTO({}) failed to send subscription message, reason: member openid is missing]", reqDTO);
            return;
        }

        // 3. Send subscription message
        socialClientService.sendSubscribeMessage(reqDTO, template.getPriTmplId(), socialUser.getOpenid());
    }

    @Override
    public void uploadWxaOrderShippingInfo(Integer userType, SocialWxaOrderUploadShippingInfoReqDTO reqDTO) {
        socialClientService.uploadWxaOrderShippingInfo(userType, reqDTO);
    }

    @Override
    public void notifyWxaOrderConfirmReceive(Integer userType, SocialWxaOrderNotifyConfirmReceiveReqDTO reqDTO) {
        socialClientService.notifyWxaOrderConfirmReceive(userType, reqDTO);
    }

}
