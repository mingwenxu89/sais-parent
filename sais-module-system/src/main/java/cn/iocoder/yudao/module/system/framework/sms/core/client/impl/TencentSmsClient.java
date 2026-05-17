package cn.iocoder.yudao.module.system.framework.sms.core.client.impl;

import cn.hutool.core.date.format.FastDateFormat;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.crypto.digest.HmacAlgorithm;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.iocoder.yudao.framework.common.core.KeyValue;
import cn.iocoder.yudao.framework.common.util.collection.ArrayUtils;
import cn.iocoder.yudao.framework.common.util.http.HttpUtils;
import cn.iocoder.yudao.module.system.framework.sms.core.client.dto.SmsReceiveRespDTO;
import cn.iocoder.yudao.module.system.framework.sms.core.client.dto.SmsSendRespDTO;
import cn.iocoder.yudao.module.system.framework.sms.core.client.dto.SmsTemplateRespDTO;
import cn.iocoder.yudao.module.system.framework.sms.core.enums.SmsTemplateAuditStatusEnum;
import cn.iocoder.yudao.module.system.framework.sms.core.property.SmsChannelProperties;
import com.google.common.annotations.VisibleForTesting;

import java.nio.charset.StandardCharsets;
import java.util.*;

import static cn.hutool.crypto.digest.DigestUtil.sha256Hex;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;

/**
 * Implementation of Tencent Cloud SMS function
 *
 * See <a href="https://cloud.tencent.com/document/product/382/52077">Documentation</a>
 *
 * @author shiwp
 */
public class TencentSmsClient extends AbstractSmsClient {

    private static final String HOST = "sms.tencentcloudapi.com";
    private static final String VERSION = "2021-01-11";
    private static final String REGION = "ap-guangzhou";

    /**
     * Call success code
     */
    public static final String API_CODE_SUCCESS = "Ok";

    /**
     * Whether international/Hong Kong, Macao and Taiwan SMS:
     *
     * 0: Indicates domestic SMS.
     * 1: Indicates international/Hong Kong, Macao and Taiwan text messages.
     */
    private static final long INTERNATIONAL_CHINA = 0L;

    public TencentSmsClient(SmsChannelProperties properties) {
        super(properties);
        Assert.notEmpty(properties.getApiSecret(), "apiSecret cannot be empty");
        validateSdkAppId(properties);
    }

    /**
     * Parameter verification Tencent Cloud SDK AppId
     *
     * The reason is: When Tencent Cloud sends text messages, it requires additional parameters sdkAppId
     *
     * Solution: Considering not destroying the original apiKey + apiSecret structure, the secretId is spliced into the apiKey field in the format of "secretId sdkAppId".
     *
     * @param properties Configuration
     */
    private static void validateSdkAppId(SmsChannelProperties properties) {
        String combineKey = properties.getApiKey();
        Assert.notEmpty(combineKey, "apiKey cannot be empty");
        String[] keys = combineKey.trim().split(" ");
        Assert.isTrue(keys.length == 2, "Tencent Cloud SMS apiKey configuration format is wrong, please configure it as [secretId sdkAppId]");
    }

    private String getSdkAppId() {
        return StrUtil.subAfter(properties.getApiKey(), " ", true);
    }

    private String getApiKey() {
        return StrUtil.subBefore(properties.getApiKey(), " ", true);
    }

    @Override
    public SmsSendRespDTO sendSms(Long sendLogId, String mobile,
                                  String apiTemplateId, List<KeyValue<String, Object>> templateParams) throws Throwable {
        // 1. Execute the request
        // Reference link https://cloud.tencent.com/document/product/382/55981
        TreeMap<String, Object> body = new TreeMap<>();
        body.put("PhoneNumberSet", new String[]{mobile});
        body.put("SmsSdkAppId", getSdkAppId());
        body.put("SignName", properties.getSignature());
        body.put("TemplateId", apiTemplateId);
        body.put("TemplateParamSet", ArrayUtils.toArray(templateParams, param -> String.valueOf(param.getValue())));
        JSONObject response = request("SendSms", body);

        // 2. Parse the request
        JSONObject responseResult = response.getJSONObject("Response");
        JSONObject error = responseResult.getJSONObject("Error");
        if (error != null) {
            return new SmsSendRespDTO().setSuccess(false)
                    .setApiRequestId(responseResult.getStr("RequestId"))
                    .setApiCode(error.getStr("Code"))
                    .setApiMsg(error.getStr("Message"));
        }
        JSONObject sendResult = responseResult.getJSONArray("SendStatusSet").getJSONObject(0);
        return new SmsSendRespDTO().setSuccess(Objects.equals(API_CODE_SUCCESS, sendResult.getStr("Code")))
                .setApiRequestId(responseResult.getStr("RequestId"))
                .setSerialNo(sendResult.getStr("SerialNo"))
                .setApiMsg(sendResult.getStr("Message"));
    }

    @Override
    public List<SmsReceiveRespDTO> parseSmsReceiveStatus(String text) {
        JSONArray statuses = JSONUtil.parseArray(text);
        // Field reference
        return convertList(statuses, status -> {
            JSONObject statusObj = (JSONObject) status;
            return new SmsReceiveRespDTO()
                    .setSuccess("SUCCESS".equals(statusObj.getStr("report_status"))) // Whether the reception is successful
                    .setErrorCode(statusObj.getStr("errmsg")) // status report code
                    .setErrorMsg(statusObj.getStr("description")) // Status report description
                    .setMobile(statusObj.getStr("mobile")) // Mobile phone ID
                    .setReceiveTime(statusObj.getLocalDateTime("user_receive_time", null)) // status report time
                    .setSerialNo(statusObj.getStr("sid")); // Send serial ID
        });
    }

    @Override
    public SmsTemplateRespDTO getSmsTemplate(String apiTemplateId) throws Throwable {
        // 1. Build the request
        // Reference link https://cloud.tencent.com/document/product/382/52067
        TreeMap<String, Object> body = new TreeMap<>();
        body.put("International", INTERNATIONAL_CHINA);
        body.put("TemplateIdSet", new Integer[]{Integer.valueOf(apiTemplateId)});
        JSONObject response = request("DescribeSmsTemplateList", body);

        // 2. Parse the request
        JSONObject statusResult = response.getJSONObject("Response")
                .getJSONArray("DescribeTemplateStatusSet").getJSONObject(0);
        return new SmsTemplateRespDTO().setId(apiTemplateId)
                .setContent(statusResult.get("TemplateContent").toString())
                .setAuditStatus(convertSmsTemplateAuditStatus(statusResult.getInt("StatusCode")))
                .setAuditReason(statusResult.get("ReviewReply").toString());
    }

    @VisibleForTesting
    Integer convertSmsTemplateAuditStatus(int templateStatus) {
        switch (templateStatus) {
            case 1: return SmsTemplateAuditStatusEnum.CHECKING.getStatus();
            case 0: return SmsTemplateAuditStatusEnum.SUCCESS.getStatus();
            case -1: return SmsTemplateAuditStatusEnum.FAIL.getStatus();
            default: throw new IllegalArgumentException(String.format("Unknown review status (%d)", templateStatus));
        }
    }

    /**
     * Request Tencent Cloud SMS
     *
     * @see <a href="https://cloud.tencent.com/document/product/382/52072">Signature method v3</a>
     *
     * @param action Requested API name
     * @param body Request parameters
     * @return Request result
     */
    private JSONObject request(String action, TreeMap<String, Object> body) {
        // 1.1 Request Header
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json; charset=utf-8");
        headers.put("Host", HOST);
        headers.put("X-TC-Action", action);
        Date now = new Date();
        String nowStr = FastDateFormat.getInstance("yyyy-MM-dd", TimeZone.getTimeZone("UTC")).format(now);
        headers.put("X-TC-Timestamp", String.valueOf(now.getTime() / 1000));
        headers.put("X-TC-Version", VERSION);
        headers.put("X-TC-Region", REGION);

        // 1.2 Build signature header
        String canonicalQueryString = "";
        String canonicalHeaders = "content-type:application/json; charset=utf-8\n"
                + "host:" + HOST + "\n" + "x-tc-action:" + action.toLowerCase() + "\n";
        String signedHeaders = "content-type;host;x-tc-action";
        String canonicalRequest = "POST" + "\n" + "/" + "\n" + canonicalQueryString + "\n" + canonicalHeaders + "\n"
                + signedHeaders + "\n" + sha256Hex(JSONUtil.toJsonStr(body));
        String credentialScope = nowStr + "/" + "sms" + "/" + "tc3_request";
        String stringToSign = "TC3-HMAC-SHA256" + "\n" + now.getTime() / 1000 + "\n" + credentialScope + "\n" +
                sha256Hex(canonicalRequest);
        byte[] secretService = hmac256(hmac256(("TC3" + properties.getApiSecret()).getBytes(StandardCharsets.UTF_8), nowStr), "sms");
        String signature = HexUtil.encodeHexStr(hmac256(hmac256(secretService, "tc3_request"), stringToSign));
        headers.put("Authorization", "TC3-HMAC-SHA256" + " " + "Credential=" + getApiKey() + "/" + credentialScope + ", "
                + "SignedHeaders=" + signedHeaders + ", " + "Signature=" + signature);

        // 2. Initiate a request
        String responseBody = HttpUtils.post("https://" + HOST, headers, JSONUtil.toJsonStr(body));
        return JSONUtil.parseObj(responseBody);
    }

    private static byte[] hmac256(byte[] key, String msg) {
        return DigestUtil.hmac(HmacAlgorithm.HmacSHA256, key).digest(msg);
    }

}