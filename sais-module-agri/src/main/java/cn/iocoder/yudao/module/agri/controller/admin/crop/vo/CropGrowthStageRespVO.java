package cn.iocoder.yudao.module.agri.controller.admin.crop.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "Admin - Crop Growth Stage Response VO")
@Data
@ExcelIgnoreUnannotated
public class CropGrowthStageRespVO {

    @Schema(description = "Growth stage ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("Growth stage ID")
    private Long id;

    @Schema(description = "Crop ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("Crop ID")
    private Long cropId;

    @Schema(description = "Stage name", requiredMode = Schema.RequiredMode.REQUIRED, example = "Seedling Stage")
    @ExcelProperty("Stage name")
    private String stageName;

    @Schema(description = "Stage order", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("Stage order")
    private Integer stageOrder;

    @Schema(description = "Stage duration in days", requiredMode = Schema.RequiredMode.REQUIRED, example = "30")
    @ExcelProperty("Stage duration in days")
    private Integer durationDays;

    @Schema(description = "Soil moisture minimum threshold", example = "60")
    @ExcelProperty("Soil moisture minimum threshold")
    private java.math.BigDecimal soilMoistureMin;

    @Schema(description = "Soil moisture maximum threshold", example = "80")
    @ExcelProperty("Soil moisture maximum threshold")
    private java.math.BigDecimal soilMoistureMax;

    @Schema(description = "Soil moisture optimal threshold", example = "70")
    @ExcelProperty("Soil moisture optimal threshold")
    private java.math.BigDecimal soilMoistureOptimal;

    @Schema(description = "Created at", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("Created at")
    private LocalDateTime createTime;

}
