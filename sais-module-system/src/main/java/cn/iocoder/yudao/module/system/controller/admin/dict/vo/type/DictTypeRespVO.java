package cn.iocoder.yudao.module.system.controller.admin.dict.vo.type;

import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;
import cn.iocoder.yudao.module.system.enums.DictTypeConstants;
import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "Management background - dict type information Response VO")
@Data
@ExcelIgnoreUnannotated
public class DictTypeRespVO {

    @Schema(description = "Dict type ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @ExcelProperty("dict primary key")
    private Long id;

    @Schema(description = "Dict name", requiredMode = Schema.RequiredMode.REQUIRED, example = "gender")
    @ExcelProperty("Dict name")
    private String name;

    @Schema(description = "Dict Type", requiredMode = Schema.RequiredMode.REQUIRED, example = "sys_common_sex")
    @ExcelProperty("Dict Type")
    private String type;

    @Schema(description = "Status, see CommonStatusEnum enumeration class", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty(value = "Status", converter = DictConvert.class)
    @DictFormat(DictTypeConstants.COMMON_STATUS)
    private Integer status;

    @Schema(description = "Remark", example = "happy note")
    private String remark;

    @Schema(description = "Create Time", requiredMode = Schema.RequiredMode.REQUIRED, example = "timestamp format")
    private LocalDateTime createTime;

}
