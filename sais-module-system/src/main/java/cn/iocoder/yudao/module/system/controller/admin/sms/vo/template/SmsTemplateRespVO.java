package cn.iocoder.yudao.module.system.controller.admin.sms.vo.template;

import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;
import cn.iocoder.yudao.module.system.enums.DictTypeConstants;
import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Management backend - SMS template Response VO")
@Data
@ExcelIgnoreUnannotated
public class SmsTemplateRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @ExcelProperty("ID")
    private Long id;

    @Schema(description = "SMS type, see SmsTemplateTypeEnum enumeration class", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty(value = "SMS signature", converter = DictConvert.class)
    @DictFormat(DictTypeConstants.SMS_TEMPLATE_TYPE)
    private Integer type;

    @Schema(description = "Open status, see CommonStatusEnum enumeration class", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty(value = "On state", converter = DictConvert.class)
    @DictFormat(DictTypeConstants.COMMON_STATUS)
    private Integer status;

    @Schema(description = "template encoding", requiredMode = Schema.RequiredMode.REQUIRED, example = "test_01")
    @ExcelProperty("template encoding")
    private String code;

    @Schema(description = "Template name", requiredMode = Schema.RequiredMode.REQUIRED, example = "yudao")
    @ExcelProperty("Template name")
    private String name;

    @Schema(description = "Template content", requiredMode = Schema.RequiredMode.REQUIRED, example = "Hello, {name}. You look so {like}!")
    @ExcelProperty("Template content")
    private String content;

    @Schema(description = "parameter array", example = "name,code")
    private List<String> params;

    @Schema(description = "Remark", example = "Hahaha")
    @ExcelProperty("Remark")
    private String remark;

    @Schema(description = "SMS API template ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "4383920")
    @ExcelProperty("SMS API template ID")
    private String apiTemplateId;

    @Schema(description = "SMS channel ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "10")
    @ExcelProperty("SMS channel ID")
    private Long channelId;

    @Schema(description = "SMS channel code", requiredMode = Schema.RequiredMode.REQUIRED, example = "ALIYUN")
    @ExcelProperty(value = "SMS channel code", converter = DictConvert.class)
    @DictFormat(DictTypeConstants.SMS_CHANNEL_CODE)
    private String channelCode;

    @Schema(description = "Create Time", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("Create Time")
    private LocalDateTime createTime;

}
