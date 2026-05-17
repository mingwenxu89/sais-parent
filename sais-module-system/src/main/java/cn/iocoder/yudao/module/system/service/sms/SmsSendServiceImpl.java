package cn.iocoder.yudao.module.system.service.sms;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.core.KeyValue;
import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.framework.datapermission.core.annotation.DataPermission;
import cn.iocoder.yudao.module.system.framework.sms.core.client.SmsClient;
import cn.iocoder.yudao.module.system.framework.sms.core.client.dto.SmsReceiveRespDTO;
import cn.iocoder.yudao.module.system.framework.sms.core.client.dto.SmsSendRespDTO;
import cn.iocoder.yudao.module.system.dal.dataobject.sms.SmsChannelDO;
import cn.iocoder.yudao.module.system.dal.dataobject.sms.SmsTemplateDO;
import cn.iocoder.yudao.module.system.dal.dataobject.user.AdminUserDO;
import cn.iocoder.yudao.module.system.mq.message.sms.SmsSendMessage;
import cn.iocoder.yudao.module.system.mq.producer.sms.SmsProducer;
import cn.iocoder.yudao.module.system.service.member.MemberService;
import cn.iocoder.yudao.module.system.service.user.AdminUserService;
import com.google.common.annotations.VisibleForTesting;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.system.enums.ErrorCodeConstants.*;

/**
 * Implementation of SMS sending Service sending
 *
 * @author Yudao Source Code
 */
@Service
@Slf4j
public class SmsSendServiceImpl implements SmsSendService {

    @Resource
    private AdminUserService adminUserService;
    @Resource
    private MemberService memberService;
    @Resource
    private SmsChannelService smsChannelService;
    @Resource
    private SmsTemplateService smsTemplateService;
    @Resource
    private SmsLogService smsLogService;

    @Resource
    private SmsProducer smsProducer;

    @Override
    @DataPermission(enable = false) // No need to worry about data permissions when sending text messages
    public Long sendSingleSmsToAdmin(String mobile, Long userId, String templateCode, Map<String, Object> templateParams) {
        // If mobile is empty, load the mobile phone ID corresponding to the user ID.
        if (StrUtil.isEmpty(mobile)) {
            AdminUserDO user = adminUserService.getUser(userId);
            if (user != null) {
                mobile = user.getMobile();
            }
        }
        // Execute send
        return sendSingleSms(mobile, userId, UserTypeEnum.ADMIN.getValue(), templateCode, templateParams);
    }

    @Override
    public Long sendSingleSmsToMember(String mobile, Long userId, String templateCode, Map<String, Object> templateParams) {
        // If mobile is empty, load the mobile phone ID corresponding to the user ID.
        if (StrUtil.isEmpty(mobile)) {
            mobile = memberService.getMemberUserMobile(userId);
        }
        // Execute send
        return sendSingleSms(mobile, userId, UserTypeEnum.MEMBER.getValue(), templateCode, templateParams);
    }

    @Override
    public Long sendSingleSms(String mobile, Long userId, Integer userType,
                              String templateCode, Map<String, Object> templateParams) {
        // Verify whether the SMS template is legal
        SmsTemplateDO template = validateSmsTemplate(templateCode);
        // Verify whether the SMS channel is legal
        SmsChannelDO smsChannel = validateSmsChannel(template.getChannelId());

        // Verify whether the mobile phone ID exists
        mobile = validateMobile(mobile);
        // Build ordered template parameters. Why is it placed in this position? It is to ensure the correctness of the template parameters in advance, rather than inserting the sending log.
        List<KeyValue<String, Object>> newTemplateParams = buildTemplateParams(template, templateParams);

        // Create a delivery log. If the template is disabled, no SMS will be sent, only logs will be recorded
        Boolean isSend = CommonStatusEnum.ENABLE.getStatus().equals(template.getStatus())
                && CommonStatusEnum.ENABLE.getStatus().equals(smsChannel.getStatus());
        String content = smsTemplateService.formatSmsTemplateContent(template.getContent(), templateParams);
        Long sendLogId = smsLogService.createSmsLog(mobile, userId, userType, isSend, template, content, templateParams);

        // Send MQ messages and send SMS messages asynchronously
        if (isSend) {
            smsProducer.sendSmsSendMessage(sendLogId, mobile, template.getChannelId(),
                    template.getApiTemplateId(), newTemplateParams);
        }
        return sendLogId;
    }

    @VisibleForTesting
    SmsChannelDO validateSmsChannel(Long channelId) {
        // Get text message templates. Taking into account efficiency, obtain from cache
        SmsChannelDO channelDO = smsChannelService.getSmsChannel(channelId);
        // SMS template does not exist
        if (channelDO == null) {
            throw exception(SMS_CHANNEL_NOT_EXISTS);
        }
        return channelDO;
    }

    @VisibleForTesting
    SmsTemplateDO validateSmsTemplate(String templateCode) {
        // Get text message templates. Taking into account efficiency, obtain from cache
        SmsTemplateDO template = smsTemplateService.getSmsTemplateByCodeFromCache(templateCode);
        // SMS template does not exist
        if (template == null) {
            throw exception(SMS_SEND_TEMPLATE_NOT_EXISTS);
        }
        return template;
    }

    /**
     * Process the parameter template into an ordered KeyValue array
     * <p>
     * The reason is that some SMS platforms DO not use key as a parameter, but an array subscript, for example <a href="https://cloud.tencent.com/document/product/382/39023">Tencent Cloud</a>
     *
     * @param template       SMS template
     * @param templateParams original parameters
     * @return processed parameters
     */
    @VisibleForTesting
    List<KeyValue<String, Object>> buildTemplateParams(SmsTemplateDO template, Map<String, Object> templateParams) {
        return template.getParams().stream().map(key -> {
            Object value = templateParams.get(key);
            if (value == null) {
                throw exception(SMS_SEND_MOBILE_TEMPLATE_PARAM_MISS, key);
            }
            return new KeyValue<>(key, value);
        }).collect(Collectors.toList());
    }

    @VisibleForTesting
    public String validateMobile(String mobile) {
        if (StrUtil.isEmpty(mobile)) {
            throw exception(SMS_SEND_MOBILE_NOT_EXISTS);
        }
        return mobile;
    }

    @Override
    public void doSendSms(SmsSendMessage message) {
        // Get the SmsClient client corresponding to the channel
        SmsClient smsClient = smsChannelService.getSmsClient(message.getChannelId());
        Assert.notNull(smsClient, "SMS client({}) does not exist", message.getChannelId());
        // Send SMS
        try {
            SmsSendRespDTO sendResponse = smsClient.sendSms(message.getLogId(), message.getMobile(),
                    message.getApiTemplateId(), message.getTemplateParams());
            smsLogService.updateSmsSendResult(message.getLogId(), sendResponse.getSuccess(),
                    sendResponse.getApiCode(), sendResponse.getApiMsg(),
                    sendResponse.getApiRequestId(), sendResponse.getSerialNo());
        } catch (Throwable ex) {
            log.error("[doSendSms][Exception in sending SMS, log ID ({})]", message.getLogId(), ex);
            smsLogService.updateSmsSendResult(message.getLogId(), false,
                    "EXCEPTION", ExceptionUtil.getRootCauseMessage(ex), null, null);
        }
    }

    @Override
    public void receiveSmsStatus(String channelCode, String text) throws Throwable {
        // Get the SmsClient client corresponding to the channel
        SmsClient smsClient = smsChannelService.getSmsClient(channelCode);
        Assert.notNull(smsClient, "SMS client({}) does not exist", channelCode);
        // parse content
        List<SmsReceiveRespDTO> receiveResults = smsClient.parseSmsReceiveStatus(text);
        if (CollUtil.isEmpty(receiveResults)) {
            return;
        }
        // Update the reception results of the SMS log. Because the volume is generally not large, use a for loop to update it first.
        receiveResults.forEach(result -> smsLogService.updateSmsReceiveResult(result.getLogId(), result.getSerialNo(),
                result.getSuccess(), result.getReceiveTime(), result.getErrorCode(), result.getErrorMsg()));
    }

}
