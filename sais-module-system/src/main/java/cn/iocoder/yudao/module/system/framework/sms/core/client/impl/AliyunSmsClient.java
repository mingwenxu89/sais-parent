package cn.iocoder.yudao.module.system.framework.sms.core.client.impl;

import cn.hutool.core.date.format.FastDateFormat;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.iocoder.yudao.framework.common.core.KeyValue;
import cn.iocoder.yudao.framework.common.util.collection.MapUtils;
import cn.iocoder.yudao.framework.common.util.http.HttpUtils;
import cn.iocoder.yudao.framework.common.util.json.JsonUtils;
import cn.iocoder.yudao.module.system.framework.sms.core.client.dto.SmsReceiveRespDTO;
import cn.iocoder.yudao.module.system.framework.sms.core.client.dto.SmsSendRespDTO;
import cn.iocoder.yudao.module.system.framework.sms.core.client.dto.SmsTemplateRespDTO;
import cn.iocoder.yudao.module.system.framework.sms.core.enums.SmsTemplateAuditStatusEnum;
import cn.iocoder.yudao.module.system.framework.sms.core.property.SmsChannelProperties;
import com.google.common.annotations.VisibleForTesting;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;

/**
 * Implementation class of Alibaba SMS client
 *
 * @author zzf
 * @since 2021/1/25 14:17
 */
@Slf4j
public class AliyunSmsClient extends AbstractSmsClient {

    private static final String URL = "https://dysmsapi.aliyuncs.com";
    private static final String HOST = "dysmsapi.aliyuncs.com";
    private static final String VERSION = "2017-05-25";

    private static final String RESPONSE_CODE_SUCCESS = "OK";

    public AliyunSmsClient(SmsChannelProperties properties) {
        super(properties);
        Assert.notEmpty(properties.getApiKey(), "apiKey cannot be empty");
        Assert.notEmpty(properties.getApiSecret(), "apiSecret cannot be empty");
    }

    @Override
    public SmsSendRespDTO sendSms(Long sendLogId, String mobile, String apiTemplateId,
                                  List<KeyValue<String, Object>> templateParams) throws Throwable {
        Assert.notBlank(properties.getSignature(), "SMS signature cannot be empty");
        // 1. Execute the request
        // Reference link https://API.aliyun.com/document/Dysmsapi/2017-05-25/SendSms
        TreeMap<String, Object> queryParam = new TreeMap<>();
        queryParam.put("PhoneNumbers", mobile);
        queryParam.put("SignName", properties.getSignature());
        queryParam.put("TemplateCode", apiTemplateId);
        queryParam.put("TemplateParam", JsonUtils.toJsonString(MapUtils.convertMap(templateParams)));
        queryParam.put("OutId", sendLogId);
        JSONObject response = request("SendSms", queryParam);

        // 2. Parse the request
        return new SmsSendRespDTO()
                .setSuccess(Objects.equals(response.getStr("Code"), RESPONSE_CODE_SUCCESS))
                .setSerialNo(response.getStr("BizId"))
                .setApiRequestId(response.getStr("RequestId"))
                .setApiCode(response.getStr("Code"))
                .setApiMsg(response.getStr("Message"));
    }

    @Override
    public List<SmsReceiveRespDTO> parseSmsReceiveStatus(String text) {
        JSONArray statuses = JSONUtil.parseArray(text);
        // Field reference https://help.aliyun.com/zh/SMS/developer-reference/smsreport-2
        return convertList(statuses, status -> {
            JSONObject statusObj = (JSONObject) status;
            return new SmsReceiveRespDTO()
                    .setSuccess(statusObj.getBool("success")) // Whether the reception is successful
                    .setErrorCode(statusObj.getStr("err_code")) // status report code
                    .setErrorMsg(statusObj.getStr("err_msg")) // Status report description
                    .setMobile(statusObj.getStr("phone_number")) // Mobile phone ID
                    .setReceiveTime(statusObj.getLocalDateTime("report_time", null)) // status report time
                    .setSerialNo(statusObj.getStr("biz_id")) // Send serial ID
                    .setLogId(statusObj.getLong("out_id")); // User serial ID
        });
    }

    @Override
    public SmsTemplateRespDTO getSmsTemplate(String apiTemplateId) throws Throwable {
        // 1. Execute the request
        // Reference link https://API.aliyun.com/document/Dysmsapi/2017-05-25/GetSmsTemplate
        TreeMap<String, Object> queryParam = new TreeMap<>();
        queryParam.put("TemplateCode", apiTemplateId);
        JSONObject response = request("GetSmsTemplate", queryParam);

        // 2.1 Request failed
        String code = response.getStr("Code");
        if (ObjectUtil.notEqual(code, RESPONSE_CODE_SUCCESS)) {
            log.error("[getSmsTemplate][Template ID ({}) response is incorrect ({})]", apiTemplateId, response);
            return null;
        }
        // 2.2 Request successful
        return new SmsTemplateRespDTO()
                .setId(response.getStr("TemplateCode"))
                .setContent(response.getStr("TemplateContent"))
                .setAuditStatus(convertSmsTemplateAuditStatus(response.getInt("TemplateStatus")))
                .setAuditReason(response.getStr("Reason"));
    }

    @VisibleForTesting
    @SuppressWarnings("EnhancedSwitchMigration")
    Integer convertSmsTemplateAuditStatus(Integer templateStatus) {
        switch (templateStatus) {
            case 0: return SmsTemplateAuditStatusEnum.CHECKING.getStatus();
            case 1: return SmsTemplateAuditStatusEnum.SUCCESS.getStatus();
            case 2: return SmsTemplateAuditStatusEnum.FAIL.getStatus();
            default: throw new IllegalArgumentException(String.format("Unknown review status (%d)", templateStatus));
        }
    }

    /**
     * Request Alibaba Cloud SMS
     *
     * @see <a href="https://help.aliyun.com/zh/sdk/product-overview/v3-request-structure-and-signature">V3 version request body & signature mechanism</>
     * @param apiName Requested API name
     * @param queryParams Request parameters
     * @return Request result
     */
    private JSONObject request(String apiName, TreeMap<String, Object> queryParams) {
        // 1. Request parameters
        String queryString = queryParams.entrySet().stream()
                .map(entry -> percentCode(entry.getKey()) + "=" + percentCode(String.valueOf(entry.getValue())))
                .collect(Collectors.joining("&"));

        // 2. Request Body
        String requestBody = ""; // The SMS API is an RPC API, and the query parameters are spliced in the uri, so the request body is set to empty if there are no special requirements.
        String hashedRequestPayload = DigestUtil.sha256Hex(requestBody);

        // 3.1 Request Header
        TreeMap<String, String> headers = new TreeMap<>();
        headers.put("host", HOST);
        headers.put("x-acs-version", VERSION);
        headers.put("x-acs-action", apiName);
        headers.put("x-acs-date", FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ss'Z'", TimeZone.getTimeZone("GMT")).format(new Date()));
        headers.put("x-acs-signature-nonce", IdUtil.randomUUID());
        headers.put("x-acs-content-sha256", hashedRequestPayload);

        // 3.2 Build signature header
        StringBuilder canonicalHeaders = new StringBuilder(); // Construct a request header, multiple standardized message headers, arrange them in ascending order according to the character code sequence of the message header name (lowercase) and then splice them together.
        StringBuilder signedHeadersBuilder = new StringBuilder(); // A list of signed message headers. Multiple request header names (lowercase) are arranged in ascending alphabetical order and separated by English semicolons (;）separate
        headers.entrySet().stream().filter(entry -> entry.getKey().toLowerCase().startsWith("x-acs-")
                        || "host".equalsIgnoreCase(entry.getKey())
                        || "content-type".equalsIgnoreCase(entry.getKey()))
                .sorted(Map.Entry.comparingByKey()).forEach(entry -> {
                    String lowerKey = entry.getKey().toLowerCase();
                    canonicalHeaders.append(lowerKey).append(":").append(String.valueOf(entry.getValue()).trim()).append("\n");
                    signedHeadersBuilder.append(lowerKey).append(";");
                });
        String signedHeaders = signedHeadersBuilder.substring(0, signedHeadersBuilder.length() - 1);

        // 4. Build Authorization signature
        String canonicalRequest = "POST" + "\n" +
                "/" + "\n" +
                queryString + "\n" +
                canonicalHeaders + "\n" +
                signedHeaders + "\n" +
                hashedRequestPayload;
        String hashedCanonicalRequest = DigestUtil.sha256Hex(canonicalRequest);
        String stringToSign = "ACS3-HMAC-SHA256" + "\n" + hashedCanonicalRequest;
        String signature = SecureUtil.hmacSha256(properties.getApiSecret()).digestHex(stringToSign); // Compute signature
        headers.put("Authorization", "ACS3-HMAC-SHA256" + " " + "Credential=" + properties.getApiKey()
                + ", " + "SignedHeaders=" + signedHeaders + ", " + "Signature=" + signature);

        // 5. Initiate a request
        String responseBody = HttpUtils.post(URL + "?" + queryString, headers, requestBody);
        return JSONUtil.parseObj(responseBody);
    }

    /**
     * URL-encode the specified string and replace specific characters to comply with URL encoding specifications
     *
     * @param str String that needs to be URL encoded
     * @return encoded string
     */
    @SneakyThrows
    private static String percentCode(String str) {
        Assert.notNull(str, "str cannot be empty");
        return HttpUtils.encodeUtf8(str)
                .replace("+", "%20") // plus sign "+" is replaced by "%20"
                .replace("*", "%2A") // asterisk "*" is replaced by "%2A"
                .replace("%7E", "~"); // tilde "%7E" is replaced by "~"
    }

}
