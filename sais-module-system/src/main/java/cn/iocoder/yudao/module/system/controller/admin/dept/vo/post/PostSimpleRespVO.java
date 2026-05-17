package cn.iocoder.yudao.module.system.controller.admin.dept.vo.post;

import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "Management backend - simplification of job information Response VO")
@Data
public class PostSimpleRespVO {

    @Schema(description = "Position serial ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @ExcelProperty("Position serial ID")
    private Long id;

    @Schema(description = "Job title", requiredMode = Schema.RequiredMode.REQUIRED, example = "small potatoes")
    @ExcelProperty("Job title")
    private String name;

}
