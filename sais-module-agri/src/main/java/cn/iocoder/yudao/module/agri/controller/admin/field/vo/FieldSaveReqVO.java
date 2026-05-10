package cn.iocoder.yudao.module.agri.controller.admin.field.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Schema(description = "Admin - Save Field Request VO")
@Data
public class FieldSaveReqVO {

    @Schema(description = "Field ID", example = "1")
    private Long id;

    @Schema(description = "Field name", requiredMode = Schema.RequiredMode.REQUIRED, example = "East Field 1")
    @NotBlank(message = "Field name is required")
    private String fieldName;

    @Schema(description = "Area (mu)", example = "12.5")
    private BigDecimal area;

    @Schema(description = "Longitude", example = "120.123456")
    private BigDecimal longitude;

    @Schema(description = "Latitude", example = "30.123456")
    private BigDecimal latitude;

    @Schema(description = "Growth status UNSTARTED=Not started ONGOING=In progress FINISHED=Completed FALLOW=Fallow",
            requiredMode = Schema.RequiredMode.REQUIRED, example = "UNSTARTED")
    @NotBlank(message = "Growth status is required")
    private String growStatus;

    @Schema(description = "Boundary", example = "POLYGON((...))")
    private String boundary;

}
