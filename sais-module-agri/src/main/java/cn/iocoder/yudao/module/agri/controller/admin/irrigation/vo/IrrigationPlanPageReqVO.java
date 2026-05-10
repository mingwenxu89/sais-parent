package cn.iocoder.yudao.module.agri.controller.admin.irrigation.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "Admin - Irrigation Plan Page Request VO")
@Data
public class IrrigationPlanPageReqVO extends PageParam {

    @Schema(description = "Device ID", example = "1")
    private Long deviceId;

    @Schema(description = "Field ID", example = "1")
    private Long fieldId;

    @Schema(description = "Decision source MANUAL / AI", example = "AI")
    private String decisionSource;

    @Schema(description = "Status PENDING / EXECUTING / COMPLETED / CANCELLED", example = "PENDING")
    private String status;

    @Schema(description = "Planned start time range")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] plannedStartTime;

}
