package cn.iocoder.yudao.module.agri.controller.admin.crop.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Schema(description = "Crop Plan Create/Update Request VO")
@Data
public class CropPlanSaveReqVO {

    @Schema(description = "Crop plan ID")
    private Long id;

    @Schema(description = "Crop ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Crop ID cannot be empty")
    private Long cropId;

    @Schema(description = "Field ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Field ID cannot be empty")
    private Long fieldId;

    @Schema(description = "Start date", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Start date cannot be empty")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

}
