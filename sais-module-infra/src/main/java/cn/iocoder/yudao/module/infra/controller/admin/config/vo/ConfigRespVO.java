package cn.iocoder.yudao.module.infra.controller.admin.config.vo;

import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;
import cn.iocoder.yudao.module.infra.enums.DictTypeConstants;
import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "Management background - parameter configuration information Response VO")
@Data
@ExcelIgnoreUnannotated
public class ConfigRespVO {

    @Schema(description = "Parameter configuration serial ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @ExcelProperty("Parameter configuration serial ID")
    private Long id;

    @Schema(description = "Parameter classification", requiredMode = Schema.RequiredMode.REQUIRED, example = "biz")
    @ExcelProperty("Parameter classification")
    private String category;

    @Schema(description = "Parameter name", requiredMode = Schema.RequiredMode.REQUIRED, example = "Database name")
    @ExcelProperty("Parameter name")
    private String name;

    @Schema(description = "Parameter key name", requiredMode = Schema.RequiredMode.REQUIRED, example = "yunai.db.username")
    @ExcelProperty("Parameter key name")
    private String key;

    @Schema(description = "Parameter key value", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @ExcelProperty("Parameter key value")
    private String value;

    @Schema(description = "Parameter type, see SysConfigTypeEnum enumeration", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty(value = "Parameter type", converter = DictConvert.class)
    @DictFormat(DictTypeConstants.CONFIG_TYPE)
    private Integer type;

    @Schema(description = "visible or not", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
    @ExcelProperty(value = "visible or not", converter = DictConvert.class)
    @DictFormat(DictTypeConstants.BOOLEAN_STRING)
    private Boolean visible;

    @Schema(description = "Remark", example = "Note that he is very handsome!")
    @ExcelProperty("Remark")
    private String remark;

    @Schema(description = "Create Time", requiredMode = Schema.RequiredMode.REQUIRED, example = "timestamp format")
    @ExcelProperty("Create Time")
    private LocalDateTime createTime;

}
