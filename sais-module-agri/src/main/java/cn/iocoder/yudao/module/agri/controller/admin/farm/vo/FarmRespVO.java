package cn.iocoder.yudao.module.agri.controller.admin.farm.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Admin - Farm Response VO")
@Data
public class FarmRespVO {

    @Schema(description = "Farm ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "Farm name", example = "Demo Farm")
    private String farmName;

    @Schema(description = "Longitude", example = "175.2793000")
    private BigDecimal longitude;

    @Schema(description = "Latitude", example = "-37.7870000")
    private BigDecimal latitude;

    @Schema(description = "Address", example = "Hamilton, New Zealand")
    private String address;

    @Schema(description = "Created at", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}
