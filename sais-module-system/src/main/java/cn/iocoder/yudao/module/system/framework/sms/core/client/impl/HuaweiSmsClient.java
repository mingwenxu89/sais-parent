package cn.iocoder.yudao.module.system.framework.sms.core.client.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.format.FastDateFormat;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.iocoder.yudao.framework.common.core.KeyValue;
import cn.iocoder.yudao.framework.common.util.http.HttpUtils;
import cn.iocoder.yudao.framework.common.util.json.JsonUtils;
import cn.iocoder.yudao.module.system.framework.sms.core.client.dto.SmsReceiveRespDTO;
import cn.iocoder.yudao.module.system.framework.sms.core.client.dto.SmsSendRespDTO;
import cn.iocoder.yudao.module.system.framework.sms.core.client.dto.SmsTemplateRespDTO;
import cn.iocoder.yudao.module.system.framework.sms.core.enums.SmsTemplateAuditStatusEnum;
import cn.iocoder.yudao.module.system.framework.sms.core.property.SmsChannelProperties;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static cn.hutool.crypto.digest.DigestUtil.sha256Hex;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;

/**
 * Implementation class of Huawei SMS client
 *
 * @author scholar
 * @since 2024/6/02 11:55
 */
@Slf4j
public class HuaweiSmsClient extends AbstractSmsClient {

    private static final String URL = "https://smsapi.cn-north-4.myhuaweicloud.com:443/sms/batchSendSms/v1";//APPAccess address+API access URI
    private static final String HOST = "smsapi.cn-north-4.myhuaweicloud.com:443";
    private static final String SIGNEDHEADERS = "content-type;host;x-sdk-date";

    private static final String RESPONSE_CODE_SUCCESS = "000000";

    public HuaweiSmsClient(SmsChannelProperties properties) {
        super(properties);
        Assert.notEmpty(properties.getApiKey(), "apiKey cannot be empty");
        Assert.notEmpty(properties.getApiSecret(), "apiSecret cannot be empty");
        validateSender(properties);
    }

    /**
     * Parameter verification Huawei Cloud’s sender channel ID
     *
     * The reason is: When verifying that Huawei Cloud sends text messages, additional parameters sender are required.
     *
     * Solution: Considering not destroying the original apiKey + apiSecret structure, the secretId is spliced into the apiKey field in the format of "secretId sdkAppId".
     *
     * @param properties Configuration
     */
    private static void validateSender(SmsChannelProperties properties) {
        String combineKey = properties.getApiKey();
        Assert.notEmpty(combineKey, "apiKey cannot be empty");
        String[] keys = combineKey.trim().split(" ");
        Assert.isTrue(keys.length == 2, "Huawei Cloud SMS apiKey configuration format is wrong, please configure it as [accessKeyId sender]");
    }

    private String getAccessKey() {
        return StrUtil.subBefore(properties.getApiKey(), " ", true);
    }

    private String getSender() {
        return StrUtil.subAfter(properties.getApiKey(), " ", true);
    }

    @Override
    public SmsSendRespDTO sendSms(Long sendLogId, String mobile, String apiTemplateId,
                                  List<KeyValue<String, Object>> templateParams) throws Throwable {
        StringBuilder requestBody = new StringBuilder();
        appendToBody(requestBody, "from=", getSender());
        appendToBody(requestBody, "&to=", mobile);
        appendToBody(requestBody, "&templateId=", apiTemplateId);
        appendToBody(requestBody, "&templateParas=", JsonUtils.toJsonString(
                convertList(templateParams, kv -> String.valueOf(kv.getValue()))));
        appendToBody(requestBody, "&statusCallback=", properties.getCallbackUrl());
        appendToBody(requestBody, "&extend=", String.valueOf(sendLogId));
        JSONObject response = request("/sms/batchSendSms/v1/", "POST", requestBody.toString());

        // 2. Parse the request
        if (!response.containsKey("result")) { // For example: The key is incorrect
            return new SmsSendRespDTO().setSuccess(false)
                    .setApiCode(response.getStr("code"))
                    .setApiMsg(response.getStr("description"));
        }
        JSONObject sendResult = response.getJSONArray("result").getJSONObject(0);
        return new SmsSendRespDTO().setSuccess(RESPONSE_CODE_SUCCESS.equals(response.getStr("code")))
                .setSerialNo(sendResult.getStr("smsMsgId")).setApiCode(sendResult.getStr("status"));
    }

    /**
     * Request Huawei Cloud SMS
     *
     * @see <a href="Authentication">https://support.huaweicloud.com/API-msgsms/sms_05_0046.html</a>
     * @param uri Request URI
     * @param method Request Method
     * @param requestBody Request Body
     * @return Request result
     */
    private JSONObject request(String uri, String method, String requestBody) {
        // 1.1 Request Header
        TreeMap<String, String> headers = new TreeMap<>();
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        String sdkDate = FastDateFormat.getInstance("yyyyMMdd'T'HHmmss'Z'", TimeZone.getTimeZone("UTC")).format(new Date());
        headers.put("X-Sdk-Date", sdkDate);
        headers.put("host", HOST);

        // 1.2 Build signature header
        String canonicalQueryString = ""; // Query parameters are empty
        String canonicalHeaders = "content-type:application/x-www-form-urlencoded\n"
                + "host:"+ HOST +"\n" + "x-sdk-date:" + sdkDate + "\n";
        String canonicalRequest = method + "\n" + uri + "\n" + canonicalQueryString + "\n"
                + canonicalHeaders + "\n" + SIGNEDHEADERS + "\n" + sha256Hex(requestBody);
        String stringToSign = "SDK-HMAC-SHA256" + "\n" + sdkDate + "\n" + sha256Hex(canonicalRequest);
        String signature = SecureUtil.hmacSha256(properties.getApiSecret()).digestHex(stringToSign);  // Compute signature
        headers.put("Authorization", "SDK-HMAC-SHA256" + " " + "Access=" + getAccessKey()
                + ", " + "SignedHeaders=" + SIGNEDHEADERS + ", " + "Signature=" + signature);

        // 2. Initiate a request
        String responseBody = HttpUtils.post(URL, headers, requestBody);
        return JSONUtil.parseObj(responseBody);
    }

    @Override
    public List<SmsReceiveRespDTO> parseSmsReceiveStatus(String requestBody) {
        Map<String, String> params = HttpUtil.decodeParamMap(requestBody, StandardCharsets.UTF_8);
        // Field reference https://support.huaweicloud.com/API-msgsms/sms_05_0003.html
        return ListUtil.of(new SmsReceiveRespDTO()
                .setSuccess("DELIVRD".equals(params.get("status"))) // Whether the reception is successful
                .setErrorCode(params.get("status")) // status report code
                .setErrorMsg(params.get("statusDesc"))
                .setMobile(params.get("to")) // Mobile phone ID
                .setReceiveTime(LocalDateTime.ofInstant(Instant.parse(params.get("updateTime")), ZoneId.of("UTC"))) // status report time
                .setSerialNo(params.get("smsMsgId")) // Send serial ID
                .setLogId(Long.valueOf(params.get("extend")))); // User serial ID
    }

    @Override
    public SmsTemplateRespDTO getSmsTemplate(String apiTemplateId) throws Throwable {
        // Huawei SMS template query and SMS sending are two different sets of keys and secrets, which are quite different from Alibaba and Tencent. Template query verification is not implemented here yet.
        String[] strs = apiTemplateId.split(" ");
        Assert.isTrue(strs.length == 2, "The format is incorrect and needs to meet: apiTemplateId sender");
        return new SmsTemplateRespDTO().setId(apiTemplateId).setContent(null)
                .setAuditStatus(SmsTemplateAuditStatusEnum.SUCCESS.getStatus()).setAuditReason(null);
    }

    private static void appendToBody(StringBuilder body, String key, String value) {
        if (StrUtil.isNotEmpty(value)) {
            body.append(key).append(HttpUtils.encodeUtf8(value));
        }
    }

}