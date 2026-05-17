package cn.iocoder.yudao.module.system.controller.admin.dept.vo.post;

import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;
import cn.iocoder.yudao.module.system.enums.DictTypeConstants;
import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "Management backend - job information Response VO")
@Data
@ExcelIgnoreUnannotated
public class PostRespVO {

    @Schema(description = "Position serial ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @ExcelProperty("Position serial ID")
    private Long id;

    @Schema(description = "Job title", requiredMode = Schema.RequiredMode.REQUIRED, example = "small potatoes")
    @ExcelProperty("Job title")
    private String name;

    @Schema(description = "Position code", requiredMode = Schema.RequiredMode.REQUIRED, example = "yudao")
    @ExcelProperty("Position code")
    private String code;

    @Schema(description = "Display order", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @ExcelProperty("Position sorting")
    private Integer sort;

    @Schema(description = "Status, see CommonStatusEnum enumeration class", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty(value = "Status", converter = DictConvert.class)
    @DictFormat(DictTypeConstants.COMMON_STATUS)
    private Integer status;

    @Schema(description = "Remark", example = "happy note")
    private String remark;

    @Schema(description = "Create Time", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}
