package cn.iocoder.yudao.module.agri.controller.admin.field.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Admin - Field Response VO")
@Data
@ExcelIgnoreUnannotated
public class FieldRespVO {

    @Schema(description = "Field ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("Field ID")
    private Long id;

    @Schema(description = "Field name", requiredMode = Schema.RequiredMode.REQUIRED, example = "East Field 1")
    @ExcelProperty("Field name")
    private String fieldName;

    @Schema(description = "Farm ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("Farm ID")
    private Long farmId;

    @Schema(description = "Area (mu)", example = "12.5")
    @ExcelProperty("Area (mu)")
    private BigDecimal area;

    @Schema(description = "Longitude", example = "120.123456")
    @ExcelProperty("Longitude")
    private BigDecimal longitude;

    @Schema(description = "Latitude", example = "30.123456")
    @ExcelProperty("Latitude")
    private BigDecimal latitude;

    @Schema(description = "Growth status", requiredMode = Schema.RequiredMode.REQUIRED, example = "UNSTARTED")
    @ExcelProperty("Growth status")
    private String growStatus;

    @Schema(description = "Boundary", example = "POLYGON((...))")
    private String boundary;

    @Schema(description = "Created at", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("Created at")
    private LocalDateTime createTime;

}
