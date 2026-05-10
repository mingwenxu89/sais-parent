package cn.iocoder.yudao.module.agri.controller.admin.crop.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Admin - Save Crop Request VO")
@Data
public class CropSaveReqVO {

    @Schema(description = "Crop ID", example = "1")
    private Long id;

    @Schema(description = "Crop name", requiredMode = Schema.RequiredMode.REQUIRED, example = "Rice")
    @NotBlank(message = "Crop name is required")
    private String cropName;

    @Schema(description = "Crop type 1=Vegetable 2=Grain 3=Fruit 4=Other", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "Crop type is required")
    private Integer cropType;

    @Schema(description = "Variety", example = "Japonica rice")
    private String variety;

    @Schema(description = "Growth period (days)", example = "120")
    private Integer growthPeriod;

    @Schema(description = "Soil pH minimum", example = "5.5")
    private java.math.BigDecimal soilPhMin;

    @Schema(description = "Soil pH maximum", example = "7.5")
    private java.math.BigDecimal soilPhMax;

    @Schema(description = "Irrigation method 1=Drip 2=Sprinkler 3=Flood 4=Micro-spray", example = "1")
    private Integer irrigationMethod;

    @Schema(description = "Drought resistance 1=Weak 2=Medium 3=Strong", example = "2")
    private Integer droughtResistance;

    @Schema(description = "Waterlogging tolerance 1=Weak 2=Medium 3=Strong", example = "1")
    private Integer waterloggingTolerance;

    @Schema(description = "Crop image URL", example = "https://example.com/images/tomato.jpg")
    private String imageUrl;

    @Schema(description = "Growth stage list")
    private java.util.List<CropGrowthStageSaveReqVO> growthStages;

}
