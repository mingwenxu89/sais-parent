package cn.iocoder.yudao.module.agri.controller.admin.crop.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "Admin - Crop Response VO")
@Data
@ExcelIgnoreUnannotated
public class CropRespVO {

    @Schema(description = "Crop ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("Crop ID")
    private Long id;

    @Schema(description = "Crop name", requiredMode = Schema.RequiredMode.REQUIRED, example = "Rice")
    @ExcelProperty("Crop name")
    private String cropName;

    @Schema(description = "Crop type", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty("Crop type")
    private Integer cropType;

    @Schema(description = "Variety", example = "Japonica rice")
    @ExcelProperty("Variety")
    private String variety;

    @Schema(description = "Growth period (days)", example = "120")
    @ExcelProperty("Growth period (days)")
    private Integer growthPeriod;

    @Schema(description = "Soil pH minimum", example = "5.5")
    @ExcelProperty("Soil pH minimum")
    private java.math.BigDecimal soilPhMin;

    @Schema(description = "Soil pH maximum", example = "7.5")
    @ExcelProperty("Soil pH maximum")
    private java.math.BigDecimal soilPhMax;

    @Schema(description = "Irrigation method", example = "1")
    @ExcelProperty("Irrigation method")
    private Integer irrigationMethod;

    @Schema(description = "Drought resistance", example = "2")
    @ExcelProperty("Drought resistance")
    private Integer droughtResistance;

    @Schema(description = "Waterlogging tolerance", example = "1")
    @ExcelProperty("Waterlogging tolerance")
    private Integer waterloggingTolerance;

    @Schema(description = "Crop image URL", example = "https://example.com/images/tomato.jpg")
    @ExcelProperty("Crop image URL")
    private String imageUrl;

    @Schema(description = "Created at", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("Created at")
    private LocalDateTime createTime;

}
