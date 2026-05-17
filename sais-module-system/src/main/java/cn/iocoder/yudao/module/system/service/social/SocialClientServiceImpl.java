package cn.iocoder.yudao.module.system.service.social;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.WxMaSubscribeService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage;
import cn.binarywang.wx.miniapp.bean.shop.request.shipping.*;
import cn.binarywang.wx.miniapp.bean.shop.response.WxMaOrderShippingInfoBaseResponse;
import cn.binarywang.wx.miniapp.config.impl.WxMaRedisBetterConfigImpl;
import cn.binarywang.wx.miniapp.constant.WxMaConstants;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.cache.CacheUtils;
import cn.iocoder.yudao.framework.common.util.http.HttpUtils;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.system.api.social.dto.SocialWxQrcodeReqDTO;
import cn.iocoder.yudao.module.system.api.social.dto.SocialWxaOrderNotifyConfirmReceiveReqDTO;
import cn.iocoder.yudao.module.system.api.social.dto.SocialWxaOrderUploadShippingInfoReqDTO;
import cn.iocoder.yudao.module.system.api.social.dto.SocialWxaSubscribeMessageSendReqDTO;
import cn.iocoder.yudao.module.system.controller.admin.socail.vo.client.SocialClientPageReqVO;
import cn.iocoder.yudao.module.system.controller.admin.socail.vo.client.SocialClientSaveReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.social.SocialClientDO;
import cn.iocoder.yudao.module.system.dal.mysql.social.SocialClientMapper;
import cn.iocoder.yudao.module.system.dal.redis.RedisKeyConstants;
import cn.iocoder.yudao.module.system.enums.social.SocialTypeEnum;
import cn.iocoder.yudao.module.system.framework.justauth.core.AuthRequestFactory;
import com.binarywang.spring.starter.wxjava.miniapp.properties.WxMaProperties;
import com.binarywang.spring.starter.wxjava.mp.properties.WxMpProperties;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.bean.subscribemsg.TemplateInfo;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.redis.RedisTemplateWxRedisOps;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.impl.WxMpRedisConfigImpl;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthAlipayRequest;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.MapUtils.findAndThen;
import static cn.iocoder.yudao.framework.common.util.date.LocalDateTimeUtils.UTC_MS_WITH_XXX_OFFSET_FORMATTER;
import static cn.iocoder.yudao.framework.common.util.date.LocalDateTimeUtils.toEpochSecond;
import static cn.iocoder.yudao.framework.common.util.json.JsonUtils.toJsonString;
import static cn.iocoder.yudao.module.system.enums.ErrorCodeConstants.*;
import static java.util.Collections.singletonList;

/**
 * Social application service implementation class
 *
 * @author Yudao Source Code
 */
@Service
@Slf4j
public class SocialClientServiceImpl implements SocialClientService {

    /**
     * The miniapp version to be opened by the miniapp code
     *
     * 1. release: official version
     * 2. trial: trial version
     * 3. developer: development version
     */
    @Value("${yudao.wxa-code.env-version:release}")
    public String envVersion;
    /**
     * Subscription message jump applet type
     *
     * 1. developer: development version
     * 2. trial: trial version
     * 3. formal: official version
     */
    @Value("${yudao.wxa-subscribe-message.miniprogram-state:formal}")
    public String miniprogramState;

    /**
     * ID of retries to upload shipping information
     */
    private static final int UPLOAD_SHIPPING_INFO_MAX_RETRIES = 5;
    /**
     * Retry interval for uploading shipping information
     */
    private static final Duration UPLOAD_SHIPPING_INFO_RETRY_INTERVAL = Duration.ofMillis(500L);
    /**
     * WeChat error code: Payment order does not exist
     */
    private static final int WX_ERR_CODE_PAY_ORDER_NOT_EXIST = 10060001;

    @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
    @Autowired(required = false) // Due to the justauth.enable configuration item, the function of AuthRequestFactory can be turned off, so there is no forced injection here.
    private AuthRequestFactory authRequestFactory;

    @Resource
    private WxMpService wxMpService;
    @Resource
    private WxMpProperties wxMpProperties;
    @Resource
    private StringRedisTemplate stringRedisTemplate; // WxMpService It needs to be used, so it is injected into Service
    /**
     * Caching WxMpService objects
     *
     * key: Use the appId + secret of the WeChat official account to splice, that is, the clientId and clientSecret attributes of {@link SocialClientDO}.
     * Why does key use this format? Because {@link SocialClientDO} can be changed in the management background, its singleton is stored through this key.
     *
     * Why DO we need to cache WxMpService? Because the construction cost of WxMpService is relatively high, try to ensure that it is a singleton.
     */
    private final LoadingCache<String, WxMpService> wxMpServiceCache = CacheUtils.buildAsyncReloadingCache(
            Duration.ofSeconds(10L),
            new CacheLoader<String, WxMpService>() {

                @Override
                public WxMpService load(String key) {
                    String[] keys = key.split(":");
                    return buildWxMpService(keys[0], keys[1]);
                }

            });

    @Resource
    private WxMaService wxMaService;
    @Resource
    private WxMaProperties wxMaProperties;
    /**
     * Caching WxMaService objects
     *
     * The description is the same as {@link #wxMpServiceCache} variable
     */
    private final LoadingCache<String, WxMaService> wxMaServiceCache = CacheUtils.buildAsyncReloadingCache(
            Duration.ofSeconds(10L),
            new CacheLoader<String, WxMaService>() {

                @Override
                public WxMaService load(String key) {
                    String[] keys = key.split(":");
                    return buildWxMaService(keys[0], keys[1]);
                }

            });

    @Resource
    private SocialClientMapper socialClientMapper;

    @Override
    public String getAuthorizeUrl(Integer socialType, Integer userType, String redirectUri) {
        // Get the corresponding AuthRequest implementation
        AuthRequest authRequest = buildAuthRequest(socialType, userType);
        // Generate jump address
        String authorizeUri = authRequest.authorize(AuthStateUtils.createState());
        return HttpUtils.replaceUrlQuery(authorizeUri, "redirect_uri", redirectUri);
    }

    @Override
    public AuthUser getAuthUser(Integer socialType, Integer userType, String code, String state) {
        // Build request
        AuthRequest authRequest = buildAuthRequest(socialType, userType);
        AuthCallback authCallback = AuthCallback.builder().code(code).auth_code(code).state(state).build();
        // Execute request
        AuthResponse<?> authResponse = authRequest.login(authCallback);
        log.info("[getAuthUser][Request social platform type({}) request({}) response({})]", socialType,
                toJsonString(authCallback), toJsonString(authResponse));
        if (!authResponse.ok()) {
            throw exception(SOCIAL_USER_AUTH_FAILURE, authResponse.getMsg());
        }
        return (AuthUser) authResponse.getData();
    }

    /**
     * Construct AuthRequest object to support multi-tenant configuration
     *
     * @param socialType social type
     * @param userType   User type
     * @return AuthRequest object
     */
    @VisibleForTesting
    AuthRequest buildAuthRequest(Integer socialType, Integer userType) {
        // 1. First find the default configuration items and read them from application-*.yaml
        AuthRequest request = authRequestFactory.get(SocialTypeEnum.valueOfType(socialType).getSource());
        Assert.notNull(request, String.format("Social platform (%d) does not exist", socialType));
        // 2. Query the configuration items of DB and overwrite them if they exist.
        SocialClientDO client = socialClientMapper.selectBySocialTypeAndUserType(socialType, userType);
        if (client != null && Objects.equals(client.getStatus(), CommonStatusEnum.ENABLE.getStatus())) {
            // 2.1 Construct a new AuthConfig object
            AuthConfig authConfig = (AuthConfig) ReflectUtil.getFieldValue(request, "config");
            AuthConfig newAuthConfig = ReflectUtil.newInstance(authConfig.getClass());
            BeanUtil.copyProperties(authConfig, newAuthConfig);
            // 2.2 Modify the corresponding clientId + clientSecret key
            newAuthConfig.setClientId(client.getClientId());
            newAuthConfig.setClientSecret(client.getClientSecret());
            if (client.getAgentId() != null) { // If there is an agentId, modify the agentId
                newAuthConfig.setAgentId(client.getAgentId());
            }
            // If it is Alibaba’s miniapp
            if (SocialTypeEnum.ALIPAY_MINI_PROGRAM.getType().equals(socialType)) {
                return new AuthAlipayRequest(newAuthConfig, client.getPublicKey());
            }
            // 2.3 The setting will be in request for subsequent use.
            if (SocialTypeEnum.ALIPAY_MINI_PROGRAM.getType().equals(socialType)) {
                // Special: If it is an Alipay applet, there is an additional publicKey attribute. You can see the alipayPublicKey field description in AuthConfig.
                return new AuthAlipayRequest(newAuthConfig, client.getPublicKey());
            }
            ReflectUtil.setFieldValue(request, "config", newAuthConfig);
        }
        return request;
    }

    // =================== Exclusive to WeChat official account ===================

    @Override
    @SneakyThrows
    public WxJsapiSignature createWxMpJsapiSignature(Integer userType, String url) {
        WxMpService service = getWxMpService(userType);
        return service.createJsapiSignature(url);
    }

    /**
     * Get the WxMpService object corresponding to clientId + clientSecret
     *
     * @param userType User type
     * @return WxMpService object
     */
    @VisibleForTesting
    WxMpService getWxMpService(Integer userType) {
        // The first step is to query the DB configuration items and obtain the corresponding WxMpService object.
        SocialClientDO client = socialClientMapper.selectBySocialTypeAndUserType(
                SocialTypeEnum.WECHAT_MP.getType(), userType);
        if (client != null && Objects.equals(client.getStatus(), CommonStatusEnum.ENABLE.getStatus())) {
            return wxMpServiceCache.getUnchecked(client.getClientId() + ":" + client.getClientSecret());
        }
        // In the second step, if there is no DB configuration item, use the WxMpService object corresponding to application-*.yaml
        return wxMpService;
    }

    /**
     * Create a WxMpService object corresponding to clientId + clientSecret
     *
     * @param clientId     WeChat official account appId
     * @param clientSecret WeChat official account secret
     * @return WxMpService object
     */
    public WxMpService buildWxMpService(String clientId, String clientSecret) {
        // The first step is to create the WxMpRedisConfigImpl object
        WxMpRedisConfigImpl configStorage = new WxMpRedisConfigImpl(
                new RedisTemplateWxRedisOps(stringRedisTemplate),
                wxMpProperties.getConfigStorage().getKeyPrefix());
        configStorage.setAppId(clientId);
        configStorage.setSecret(clientSecret);

        // The second step is to create the WxMpService object
        WxMpService service = new WxMpServiceImpl();
        service.setWxMpConfigStorage(configStorage);
        return service;
    }

    // =================== Exclusive to WeChat Mini Program ===================

    @Override
    public WxMaPhoneNumberInfo getWxMaPhoneNumberInfo(Integer userType, String phoneCode) {
        WxMaService service = getWxMaService(userType);
        try {
            return service.getUserService().getPhoneNumber(phoneCode);
        } catch (WxErrorException e) {
            log.error("[getPhoneID][userType({}) phoneCode({}) failed to obtain phone ID]", userType, phoneCode, e);
            throw exception(SOCIAL_CLIENT_WEIXIN_MINI_APP_PHONE_CODE_ERROR);
        }
    }

    @Override
    public byte[] getWxaQrcode(SocialWxQrcodeReqDTO reqVO) {
        WxMaService service = getWxMaService(UserTypeEnum.MEMBER.getValue());
        try {
            return service.getQrcodeService().createWxaCodeUnlimitBytes(
                    ObjUtil.defaultIfEmpty(reqVO.getScene(), SocialWxQrcodeReqDTO.SCENE),
                    reqVO.getPath(),
                    ObjUtil.defaultIfNull(reqVO.getCheckPath(), SocialWxQrcodeReqDTO.CHECK_PATH),
                    envVersion,
                    ObjUtil.defaultIfNull(reqVO.getWidth(), SocialWxQrcodeReqDTO.WIDTH),
                    ObjUtil.defaultIfNull(reqVO.getAutoColor(), SocialWxQrcodeReqDTO.AUTO_COLOR),
                    null,
                    ObjUtil.defaultIfNull(reqVO.getHyaline(), SocialWxQrcodeReqDTO.HYALINE));
        } catch (WxErrorException e) {
            log.error("[getWxQrcode][reqVO({}) failed to obtain the miniapp code]", reqVO, e);
            throw exception(SOCIAL_CLIENT_WEIXIN_MINI_APP_QRCODE_ERROR);
        }
    }

    @Override
    @Cacheable(cacheNames = RedisKeyConstants.WXA_SUBSCRIBE_TEMPLATE, key = "#userType",
            unless = "#result == null")
    public List<TemplateInfo> getSubscribeTemplateList(Integer userType) {
        WxMaService service = getWxMaService(userType);
        try {
            WxMaSubscribeService subscribeService = service.getSubscribeService();
            return subscribeService.getTemplateList();
        } catch (WxErrorException e) {
            log.error("[getSubscribeTemplate][userType({}) Get the miniapp subscription message template]", userType, e);
            throw exception(SOCIAL_CLIENT_WEIXIN_MINI_APP_SUBSCRIBE_TEMPLATE_ERROR);
        }
    }

    @Override
    public void sendSubscribeMessage(SocialWxaSubscribeMessageSendReqDTO reqDTO, String templateId, String openId) {
        WxMaService service = getWxMaService(reqDTO.getUserType());
        try {
            WxMaSubscribeService subscribeService = service.getSubscribeService();
            subscribeService.sendSubscribeMsg(buildMessageSendReqDTO(reqDTO, templateId, openId));
        } catch (WxErrorException e) {
            log.error("[sendSubscribeMessage][reqVO({}) templateId({}) openId({}) Failed to send miniapp subscription message]", reqDTO, templateId, openId, e);
            throw exception(SOCIAL_CLIENT_WEIXIN_MINI_APP_SUBSCRIBE_MESSAGE_ERROR);
        }
    }

    /**
     * Construct send message request parameters
     *
     * @param reqDTO     Request
     * @param templateId Template ID
     * @param openId     Member openId
     * @return WeChat applet subscription message request parameters
     */
    private WxMaSubscribeMessage buildMessageSendReqDTO(SocialWxaSubscribeMessageSendReqDTO reqDTO,
                                                        String templateId, String openId) {
        // Set basic parameters for subscription messages
        WxMaSubscribeMessage subscribeMessage = new WxMaSubscribeMessage().setLang(WxMaConstants.MiniProgramLang.ZH_CN)
                .setMiniprogramState(miniprogramState).setTemplateId(templateId).setToUser(openId).setPage(reqDTO.getPage());
        // Set specific message parameters
        Map<String, String> messages = reqDTO.getMessages();
        if (CollUtil.isNotEmpty(messages)) {
            reqDTO.getMessages().keySet().forEach(key -> findAndThen(messages, key, value ->
                    subscribeMessage.addData(new WxMaSubscribeMessage.MsgData(key, value))));
        }
        return subscribeMessage;
    }

    @Override
    public void uploadWxaOrderShippingInfo(Integer userType, SocialWxaOrderUploadShippingInfoReqDTO reqDTO) {
        WxMaService service = getWxMaService(userType);
        List<ShippingListBean> shippingList;
        if (Objects.equals(reqDTO.getLogisticsType(), SocialWxaOrderUploadShippingInfoReqDTO.LOGISTICS_TYPE_EXPRESS)) {
            shippingList = singletonList(ShippingListBean.builder()
                    .trackingNo(reqDTO.getLogisticsNo())
                    .expressCompany(reqDTO.getExpressCompany())
                    .itemDesc(reqDTO.getItemDesc())
                    .contact(ContactBean.builder().receiverContact(DesensitizedUtil.mobilePhone(reqDTO.getReceiverContact())).build())
                    .build());
        } else {
            shippingList = singletonList(ShippingListBean.builder().itemDesc(reqDTO.getItemDesc()).build());
        }
        WxMaOrderShippingInfoUploadRequest request = WxMaOrderShippingInfoUploadRequest.builder()
                .orderKey(OrderKeyBean.builder()
                        .orderNumberType(2) // Use the WeChat order ID corresponding to the original payment transaction, that is, the channel order ID
                        .transactionId(reqDTO.getTransactionId())
                        .build())
                .logisticsType(reqDTO.getLogisticsType()) // Delivery method
                .deliveryMode(1) // Unified delivery
                .shippingList(shippingList)
                .payer(PayerBean.builder().openid(reqDTO.getOpenid()).build())
                .uploadTime(ZonedDateTime.now().format(UTC_MS_WITH_XXX_OFFSET_FORMATTER))
                .build();
        // Retry mechanism: Solve the 10060001 error caused by the time difference between payment callback and order information upload
        // Corresponding ISSUE: https://gitee.com/zhijiantianya/sar-cloud/pulls/230
        for (int attempt = 1; attempt <= UPLOAD_SHIPPING_INFO_MAX_RETRIES; attempt++) {
            try {
                WxMaOrderShippingInfoBaseResponse response = service.getWxMaOrderShippingService().upload(request);
                // Success, return directly
                if (response.getErrCode() == 0) {
                    log.info("[uploadWxaOrderShippingInfo][Successfully uploaded WeChat applet shipping information: request({}) response({})]", request, response);
                    return;
                }
                // If it is a 10060001 error (the payment order does not exist) and there are still retry times, wait and try again.
                if (response.getErrCode() == WX_ERR_CODE_PAY_ORDER_NOT_EXIST && attempt < UPLOAD_SHIPPING_INFO_MAX_RETRIES) {
                    log.warn("[uploadWxaOrderShippingInfo][The {}th attempt failed, the payment order does not exist, try again after {}: request({}) response({})]",
                            attempt, UPLOAD_SHIPPING_INFO_RETRY_INTERVAL, request, response);
                    Thread.sleep(UPLOAD_SHIPPING_INFO_RETRY_INTERVAL.toMillis());
                    continue;
                }
                // Other errors or the ID of retries is exhausted, an exception is thrown.
                log.error("[uploadWxaOrderShippingInfo][Failed to upload WeChat applet shipping information: request({}) response({})]", request, response);
                throw exception(SOCIAL_CLIENT_WEIXIN_MINI_APP_ORDER_UPLOAD_SHIPPING_INFO_ERROR, response.getErrMsg());
            } catch (WxErrorException ex) {
                log.error("[uploadWxaOrderShippingInfo][Failed to upload WeChat applet shipping information: request({})]", request, ex);
                throw exception(SOCIAL_CLIENT_WEIXIN_MINI_APP_ORDER_UPLOAD_SHIPPING_INFO_ERROR, ex.getError().getErrorMsg());
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                log.error("[uploadWxaOrderShippingInfo][Retry waiting interrupted: request({})]", request, ex);
                throw exception(SOCIAL_CLIENT_WEIXIN_MINI_APP_ORDER_UPLOAD_SHIPPING_INFO_ERROR, "Retry waiting was interrupted");
            }
        }
    }

    @Override
    public void notifyWxaOrderConfirmReceive(Integer userType, SocialWxaOrderNotifyConfirmReceiveReqDTO reqDTO) {
        WxMaService service = getWxMaService(userType);
        WxMaOrderShippingInfoNotifyConfirmRequest request = WxMaOrderShippingInfoNotifyConfirmRequest.builder()
                .transactionId(reqDTO.getTransactionId())
                .receivedTime(toEpochSecond(reqDTO.getReceivedTime()))
                .build();
        try {
            WxMaOrderShippingInfoBaseResponse response = service.getWxMaOrderShippingService().notifyConfirmReceive(request);
            if (response.getErrCode() != 0) {
                log.error("[notifyWxaOrderConfirmReceive][Failed to confirm receipt reminder to WeChat applet: request({}) response({})]", request, response);
                throw exception(SOCIAL_CLIENT_WEIXIN_MINI_APP_ORDER_NOTIFY_CONFIRM_RECEIVE_ERROR, response.getErrMsg());
            }
            log.info("[notifyWxaOrderConfirmReceive][Confirm receipt reminder to WeChat applet successfully: request({}) response({})]", request, response);
        } catch (WxErrorException ex) {
            log.error("[notifyWxaOrderConfirmReceive][Failed to confirm receipt reminder to WeChat applet: request({})]", request, ex);
            throw exception(SOCIAL_CLIENT_WEIXIN_MINI_APP_ORDER_NOTIFY_CONFIRM_RECEIVE_ERROR, ex.getError().getErrorMsg());
        }
    }

    /**
     * Get the WxMpService object corresponding to clientId + clientSecret
     *
     * @param userType User type
     * @return WxMpService object
     */
    @VisibleForTesting
    WxMaService getWxMaService(Integer userType) {
        // The first step is to query the DB configuration items and obtain the corresponding WxMaService object.
        SocialClientDO client = socialClientMapper.selectBySocialTypeAndUserType(
                SocialTypeEnum.WECHAT_MINI_PROGRAM.getType(), userType);
        if (client != null && Objects.equals(client.getStatus(), CommonStatusEnum.ENABLE.getStatus())) {
            return wxMaServiceCache.getUnchecked(client.getClientId() + ":" + client.getClientSecret());
        }
        // In the second step, if there is no DB configuration item, use the WxMaService object corresponding to application-*.yaml
        return wxMaService;
    }

    /**
     * Create a WxMaService object corresponding to clientId + clientSecret
     *
     * @param clientId     WeChat applet appId
     * @param clientSecret WeChat applet secret
     * @return WxMaService object
     */
    private WxMaService buildWxMaService(String clientId, String clientSecret) {
        // The first step is to create the WxMaRedisBetterConfigImpl object
        WxMaRedisBetterConfigImpl configStorage = new WxMaRedisBetterConfigImpl(
                new RedisTemplateWxRedisOps(stringRedisTemplate),
                wxMaProperties.getConfigStorage().getKeyPrefix());
        configStorage.setAppid(clientId);
        configStorage.setSecret(clientSecret);

        // The second step is to create the WxMpService object
        WxMaService service = new WxMaServiceImpl();
        service.setWxMaConfig(configStorage);
        return service;
    }

    // =================== Client Management ===================

    @Override
    public Long createSocialClient(SocialClientSaveReqVO createReqVO) {
        // Check for duplicates
        validateSocialClientUnique(null, createReqVO.getUserType(), createReqVO.getSocialType());

        // Insert
        SocialClientDO client = BeanUtils.toBean(createReqVO, SocialClientDO.class);
        socialClientMapper.insert(client);
        return client.getId();
    }

    @Override
    public void updateSocialClient(SocialClientSaveReqVO updateReqVO) {
        // Check existence
        validateSocialClientExists(updateReqVO.getId());
        // Check for duplicates
        validateSocialClientUnique(updateReqVO.getId(), updateReqVO.getUserType(), updateReqVO.getSocialType());

        // Update
        SocialClientDO updateObj = BeanUtils.toBean(updateReqVO, SocialClientDO.class);
        socialClientMapper.updateById(updateObj);
    }

    @Override
    public void deleteSocialClient(Long id) {
        // Check existence
        validateSocialClientExists(id);
        // Delete
        socialClientMapper.deleteById(id);
    }

    @Override
    public void deleteSocialClientList(List<Long> ids) {
        socialClientMapper.deleteByIds(ids);
    }

    private void validateSocialClientExists(Long id) {
        if (socialClientMapper.selectById(id) == null) {
            throw exception(SOCIAL_CLIENT_NOT_EXISTS);
        }
    }

    /**
     * To verify whether social applications are duplicated, userType + socialType must be unique
     * The reason is that when different ends (userType) select a certain social login (socialType), they need to build the corresponding request through {@link #buildAuthRequest(Integer, Integer)}
     *
     * @param id         ID
     * @param userType   User type
     * @param socialType social type
     */
    private void validateSocialClientUnique(Long id, Integer userType, Integer socialType) {
        SocialClientDO client = socialClientMapper.selectBySocialTypeAndUserType(
                socialType, userType);
        if (client == null) {
            return;
        }
        if (id == null // When adding, the description is repeated
                || ObjUtil.notEqual(id, client.getId())) { // When updating, if the ids are inconsistent, it means duplication
            throw exception(SOCIAL_CLIENT_UNIQUE);
        }
    }

    @Override
    public SocialClientDO getSocialClient(Long id) {
        return socialClientMapper.selectById(id);
    }

    @Override
    public PageResult<SocialClientDO> getSocialClientPage(SocialClientPageReqVO pageReqVO) {
        return socialClientMapper.selectPage(pageReqVO);
    }

}
