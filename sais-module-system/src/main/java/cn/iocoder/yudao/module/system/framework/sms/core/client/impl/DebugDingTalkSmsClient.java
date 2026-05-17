package cn.iocoder.yudao.module.system.framework.sms.core.client.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.crypto.digest.HmacAlgorithm;
import cn.hutool.http.HttpUtil;
import cn.iocoder.yudao.framework.common.core.KeyValue;
import cn.iocoder.yudao.framework.common.util.collection.MapUtils;
import cn.iocoder.yudao.framework.common.util.json.JsonUtils;
import cn.iocoder.yudao.module.system.framework.sms.core.client.dto.SmsReceiveRespDTO;
import cn.iocoder.yudao.module.system.framework.sms.core.client.dto.SmsSendRespDTO;
import cn.iocoder.yudao.module.system.framework.sms.core.client.dto.SmsTemplateRespDTO;
import cn.iocoder.yudao.module.system.framework.sms.core.enums.SmsTemplateAuditStatusEnum;
import cn.iocoder.yudao.module.system.framework.sms.core.property.SmsChannelProperties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * SMS client implementation class for debugging based on DingTalk WebHook
 *
 * Considering saving money, we use DingTalk WebHook to simulate sending text messages to facilitate debugging.
 *
 * @author Yudao Source Code
 */
public class DebugDingTalkSmsClient extends AbstractSmsClient {

    public DebugDingTalkSmsClient(SmsChannelProperties properties) {
        super(properties);
        Assert.notEmpty(properties.getApiKey(), "apiKey cannot be empty");
        Assert.notEmpty(properties.getApiSecret(), "apiSecret cannot be empty");
    }

    @Override
    public SmsSendRespDTO sendSms(Long sendLogId, String mobile,
                                  String apiTemplateId, List<KeyValue<String, Object>> templateParams) throws Throwable {
        // Build request
        String url = buildUrl("robot/send");
        Map<String, Object> params = new HashMap<>();
        params.put("msgtype", "text");
        String content = String.format("[Simulated SMS]\nMobile phone ID: %s\nSMS log ID: %d\nTemplate parameters: %s",
                mobile, sendLogId, MapUtils.convertMap(templateParams));
        params.put("text", MapUtil.builder().put("content", content).build());
        // Execute request
        String responseText = HttpUtil.post(url, JsonUtils.toJsonString(params));
        // Parse results
        Map<?, ?> responseObj = JsonUtils.parseObject(responseText, Map.class);
        String errorCode = MapUtil.getStr(responseObj, "errcode");
        return new SmsSendRespDTO().setSuccess(Objects.equals(errorCode, "0")).setSerialNo(StrUtil.uuid())
                .setApiCode(errorCode).setApiMsg(MapUtil.getStr(responseObj, "errorMsg"));
    }

    /**
     * Build request address
     *
     * See <a href="https://developers.dingtalk.com/document/app/custom-robot-access/title-nfv-794-g71">Documentation</a>
     *
     * @param path Request path
     * @return Request address
     */
    @SuppressWarnings("SameParameterValue")
    private String buildUrl(String path) {
        // Generate timestamp
        long timestamp = System.currentTimeMillis();
        // Generate sign
        String secret = properties.getApiSecret();
        String stringToSign = timestamp + "\n" + secret;
        byte[] signData = DigestUtil.hmac(HmacAlgorithm.HmacSHA256, StrUtil.bytes(secret)).digest(stringToSign);
        String sign = Base64.encode(signData);
        // Build the final URL
        return String.format("https://oapi.dingtalk.com/%s?access_token=%s&timestamp=%d&sign=%s",
                path, properties.getApiKey(), timestamp, sign);
    }

    @Override
    public List<SmsReceiveRespDTO> parseSmsReceiveStatus(String text) {
        throw new UnsupportedOperationException("Simulate SMS client, no need to parse callback for now");
    }

    @Override
    public SmsTemplateRespDTO getSmsTemplate(String apiTemplateId) {
        return new SmsTemplateRespDTO().setId(apiTemplateId).setContent("")
                .setAuditStatus(SmsTemplateAuditStatusEnum.SUCCESS.getStatus()).setAuditReason("");
    }

}
