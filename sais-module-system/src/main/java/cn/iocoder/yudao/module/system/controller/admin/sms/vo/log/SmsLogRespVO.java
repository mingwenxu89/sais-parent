package cn.iocoder.yudao.module.system.controller.admin.sms.vo.log;

import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;
import cn.iocoder.yudao.framework.excel.core.convert.JsonConvert;
import cn.iocoder.yudao.module.system.enums.DictTypeConstants;
import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Schema(description = "Management background - SMS log Response VO")
@Data
@ExcelIgnoreUnannotated
public class SmsLogRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @ExcelProperty("ID")
    private Long id;

    @Schema(description = "SMS channel ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "10")
    @ExcelProperty("SMS channel ID")
    private Long channelId;

    @Schema(description = "SMS channel code", requiredMode = Schema.RequiredMode.REQUIRED, example = "ALIYUN")
    @ExcelProperty("SMS channel code")
    private String channelCode;

    @Schema(description = "Template ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "20")
    @ExcelProperty("Template ID")
    private Long templateId;

    @Schema(description = "template encoding", requiredMode = Schema.RequiredMode.REQUIRED, example = "test-01")
    @ExcelProperty("template encoding")
    private String templateCode;

    @Schema(description = "SMS type", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty(value = "SMS type", converter = DictConvert.class)
    @DictFormat(DictTypeConstants.SMS_TEMPLATE_TYPE)
    private Integer templateType;

    @Schema(description = "SMS content", requiredMode = Schema.RequiredMode.REQUIRED, example = "Hello, your captcha is 1024")
    @ExcelProperty("SMS content")
    private String templateContent;

    @Schema(description = "SMS parameters", requiredMode = Schema.RequiredMode.REQUIRED, example = "name,code")
    @ExcelProperty(value = "SMS parameters", converter = JsonConvert.class)
    private Map<String, Object> templateParams;

    @Schema(description = "SMS API template ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "SMS_207945135")
    @ExcelProperty("SMS API template ID")
    private String apiTemplateId;

    @Schema(description = "Mobile phone ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "15601691300")
    @ExcelProperty("Mobile phone ID")
    private String mobile;

    @Schema(description = "User ID", example = "10")
    @ExcelProperty("User ID")
    private Long userId;

    @Schema(description = "User type", example = "1")
    @ExcelProperty(value = "User type", converter = DictConvert.class)
    @DictFormat(DictTypeConstants.USER_TYPE)
    private Integer userType;

    @Schema(description = "Send status", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty(value = "Send status", converter = DictConvert.class)
    @DictFormat(DictTypeConstants.SMS_SEND_STATUS)
    private Integer sendStatus;

    @Schema(description = "Send time")
    @ExcelProperty("Send time")
    private LocalDateTime sendTime;

    @Schema(description = "Encoding of results sent by SMS API", example = "SUCCESS")
    @ExcelProperty("Encoding of results sent by SMS API")
    private String apiSendCode;

    @Schema(description = "SMS API sending failure prompt", example = "Success")
    @ExcelProperty("SMS API sending failure prompt")
    private String apiSendMsg;

    @Schema(description = "Unique request ID returned by SMS API send", example = "3837C6D3-B96F-428C-BBB2-86135D4B5B99")
    @ExcelProperty("Unique request ID returned by SMS API send")
    private String apiRequestId;

    @Schema(description = "Serial ID returned by SMS API sending", example = "62923244790")
    @ExcelProperty("Serial ID returned by SMS API sending")
    private String apiSerialNo;

    @Schema(description = "receiving status", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    @ExcelProperty(value = "receiving status", converter = DictConvert.class)
    @DictFormat(DictTypeConstants.SMS_RECEIVE_STATUS)
    private Integer receiveStatus;

    @Schema(description = "Receiving time")
    @ExcelProperty("Receiving time")
    private LocalDateTime receiveTime;

    @Schema(description = "Encoding of API received results", example = "DELIVRD")
    @ExcelProperty("Encoding of API received results")
    private String apiReceiveCode;

    @Schema(description = "Description of API receiving results", example = "User received successfully")
    @ExcelProperty("Description of API receiving results")
    private String apiReceiveMsg;

    @Schema(description = "Create Time", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("Create Time")
    private LocalDateTime createTime;

}
