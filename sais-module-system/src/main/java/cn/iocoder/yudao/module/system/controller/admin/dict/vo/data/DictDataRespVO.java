package cn.iocoder.yudao.module.system.controller.admin.dict.vo.data;

import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;
import cn.iocoder.yudao.module.system.enums.DictTypeConstants;
import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "Management background - Dict data information Response VO")
@Data
@ExcelIgnoreUnannotated
public class DictDataRespVO {

    @Schema(description = "Dict data ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @ExcelProperty("dict encoding")
    private Long id;

    @Schema(description = "Display order", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @ExcelProperty("Dict sort")
    private Integer sort;

    @Schema(description = "dict tag", requiredMode = Schema.RequiredMode.REQUIRED, example = "taro road")
    @ExcelProperty("dict tag")
    private String label;

    @Schema(description = "Dict value", requiredMode = Schema.RequiredMode.REQUIRED, example = "iocoder")
    @ExcelProperty("Dict key")
    private String value;

    @Schema(description = "Dict Type", requiredMode = Schema.RequiredMode.REQUIRED, example = "sys_common_sex")
    @ExcelProperty("Dict Type")
    private String dictType;

    @Schema(description = "Status, see CommonStatusEnum enumeration", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty(value = "Status", converter = DictConvert.class)
    @DictFormat(DictTypeConstants.COMMON_STATUS)
    private Integer status;

    @Schema(description = "Color type, default, primary, success, info, warning, danger", example = "default")
    private String colorType;

    @Schema(description = "css style", example = "btn-visible")
    private String cssClass;

    @Schema(description = "Remark", example = "i am a character")
    private String remark;

    @Schema(description = "Create Time", requiredMode = Schema.RequiredMode.REQUIRED, example = "timestamp format")
    private LocalDateTime createTime;

}
