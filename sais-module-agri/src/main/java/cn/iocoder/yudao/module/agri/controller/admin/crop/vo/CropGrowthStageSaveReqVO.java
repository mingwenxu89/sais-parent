package cn.iocoder.yudao.module.agri.controller.admin.crop.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Admin - Save Crop Growth Stage Request VO")
@Data
public class CropGrowthStageSaveReqVO {

    @Schema(description = "Growth stage ID", example = "1")
    private Long id;

    @Schema(description = "Crop ID", example = "1")
    private Long cropId;

    @Schema(description = "Stage name", requiredMode = Schema.RequiredMode.REQUIRED, example = "Seedling Stage")
    @NotBlank(message = "Stage name is required")
    private String stageName;

    @Schema(description = "Stage order", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "Stage order is required")
    private Integer stageOrder;

    @Schema(description = "Stage duration in days", requiredMode = Schema.RequiredMode.REQUIRED, example = "30")
    @NotNull(message = "Stage duration in days is required")
    private Integer durationDays;

    @Schema(description = "Soil moisture minimum threshold", example = "60")
    private java.math.BigDecimal soilMoistureMin;

    @Schema(description = "Soil moisture maximum threshold", example = "80")
    private java.math.BigDecimal soilMoistureMax;

    @Schema(description = "Soil moisture optimal threshold", example = "70")
    private java.math.BigDecimal soilMoistureOptimal;

}
