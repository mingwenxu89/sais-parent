package cn.iocoder.yudao.module.system.framework.sms.core.client.impl;

import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.HmacAlgorithm;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.iocoder.yudao.framework.common.core.KeyValue;
import cn.iocoder.yudao.framework.common.util.http.HttpUtils;
import cn.iocoder.yudao.module.system.framework.sms.core.client.dto.SmsReceiveRespDTO;
import cn.iocoder.yudao.module.system.framework.sms.core.client.dto.SmsSendRespDTO;
import cn.iocoder.yudao.module.system.framework.sms.core.client.dto.SmsTemplateRespDTO;
import cn.iocoder.yudao.module.system.framework.sms.core.enums.SmsTemplateAuditStatusEnum;
import cn.iocoder.yudao.module.system.framework.sms.core.property.SmsChannelProperties;
import com.google.common.annotations.VisibleForTesting;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.function.Function;

import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;

/**
 * Implementation class of Qiniu Cloud SMS client
 *
 * @author scholar
 * @since 2024/08/26 15:35
 */
@Slf4j
public class QiniuSmsClient extends AbstractSmsClient {

    private static final String HOST = "sms.qiniuapi.com";

    public QiniuSmsClient(SmsChannelProperties properties) {
        super(properties);
        Assert.notEmpty(properties.getApiKey(), "apiKey cannot be empty");
        Assert.notEmpty(properties.getApiSecret(), "apiSecret cannot be empty");
    }

    public SmsSendRespDTO sendSms(Long sendLogId, String mobile, String apiTemplateId,
                                  List<KeyValue<String, Object>> templateParams) throws Throwable {
        // 1. Execute the request
        // Reference link https://developer.qiniu.com/SMS/5824/through-the-API-send-text-messages
        LinkedHashMap<String, Object> body = new LinkedHashMap<>();
        body.put("template_id", apiTemplateId);
        body.put("mobile", mobile);
        body.put("parameters", CollStreamUtil.toMap(templateParams, KeyValue::getKey, KeyValue::getValue));
        body.put("seq", Long.toString(sendLogId));
        JSONObject response = request("POST", body, "/v1/message/single");

        // 2. Parse the request
        if (ObjectUtil.isNotEmpty(response.getStr("error"))) {
            // SMS request failed
            return new SmsSendRespDTO().setSuccess(false)
                    .setApiCode(response.getStr("error"))
                    .setApiRequestId(response.getStr("request_id"))
                    .setApiMsg(response.getStr("message"));
        }
        return new SmsSendRespDTO().setSuccess(response.containsKey("message_id"))
                .setSerialNo(response.getStr("message_id"));
    }

    /**
     * Request Qiniu Cloud SMS
     *
     * @see <a href="https://developer.qiniu.com/sms/5842/sms-api-authentication"</>
     * @param httpMethod http request method
     * @param body http request message body
     * @param path URL path
     * @return Request result
     */
    private JSONObject request(String httpMethod, LinkedHashMap<String, Object> body, String path) {
        String signDate = DateUtil.date().setTimeZone(TimeZone.getTimeZone("UTC")).toString("yyyyMMdd'T'HHmmss'Z'");
        // 1. Request header
        Map<String, String> header = new HashMap<>(4);
        header.put("HOST", HOST);
        header.put("Authorization", getSignature(httpMethod, path, body != null ? JSONUtil.toJsonStr(body) : "", signDate));
        header.put("Content-Type", "application/json");
        header.put("X-Qiniu-Date", signDate);

        // 2. Initiate a request
        String responseBody;
        if (Objects.equals(httpMethod, "POST")){
            responseBody = HttpUtils.post("https://" + HOST + path, header, JSONUtil.toJsonStr(body));
        } else {
            responseBody = HttpUtils.get("https://" + HOST + path, header);
        }
        return JSONUtil.parseObj(responseBody);
    }

    private String getSignature(String method, String path, String body, String signDate) {
        StringBuilder dataToSign = new StringBuilder();
        dataToSign.append(method.toUpperCase()).append(" ").append(path)
                .append("\nHost: ").append(HOST)
                .append("\n").append("Content-Type").append(": ").append("application/json")
                .append("\n").append("X-Qiniu-Date").append(": ").append(signDate)
                .append("\n\n");
        if (ObjectUtil.isNotEmpty(body)) {
            dataToSign.append(body);
        }
        String signature = SecureUtil.hmac(HmacAlgorithm.HmacSHA1, properties.getApiSecret())
                .digestBase64(dataToSign.toString(), true);
        return "Qiniu " + properties.getApiKey() + ":" + signature;
    }

    @Override
    public List<SmsReceiveRespDTO> parseSmsReceiveStatus(String text) {
        JSONObject status = JSONUtil.parseObj(text);
        // Field reference https://developer.qiniu.com/SMS/5910/message-push
        return convertList(status.getJSONArray("items"), new Function<Object, SmsReceiveRespDTO>() {

            @Override
            public SmsReceiveRespDTO apply(Object item) {
                JSONObject statusObj = (JSONObject) item;
                return new SmsReceiveRespDTO()
                        .setSuccess("DELIVRD".equals(statusObj.getStr("status"))) // Whether the reception is successful
                        .setErrorMsg(statusObj.getStr("status")) // status report code
                        .setMobile(statusObj.getStr("mobile")) // Mobile phone ID
                        .setReceiveTime(LocalDateTimeUtil.of(statusObj.getLong("delivrd_at") * 1000L)) // status report time
                        .setSerialNo(statusObj.getStr("message_id")) // Send serial ID
                        .setLogId(statusObj.getLong("seq")); // User serial ID
            }

        });
    }

    @Override
    public SmsTemplateRespDTO getSmsTemplate(String apiTemplateId) throws Throwable {
        // 1. Execute the request
        // Reference link https://developer.qiniu.com/SMS/5969/query-a-single-template
        JSONObject response = request("GET", null, "/v1/template/" + apiTemplateId);

        // 2.2 Parse the request
        return new SmsTemplateRespDTO()
                .setId(response.getStr("id"))
                .setContent(response.getStr("template"))
                .setAuditStatus(convertSmsTemplateAuditStatus(response.getStr("audit_status")))
                .setAuditReason(response.getStr("reject_reason"));
    }

    @VisibleForTesting
    Integer convertSmsTemplateAuditStatus(String templateStatus) {
        switch (templateStatus) {
            case "passed": return SmsTemplateAuditStatusEnum.SUCCESS.getStatus();
            case "reviewing": return SmsTemplateAuditStatusEnum.CHECKING.getStatus();
            case "rejected": return SmsTemplateAuditStatusEnum.FAIL.getStatus();
            default:
                throw new IllegalArgumentException(String.format("Unknown review status (%str)", templateStatus));
        }
    }
}
