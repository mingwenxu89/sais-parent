package cn.iocoder.yudao.module.agri.controller.admin.sensorreporting.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "Sensor Reporting Status Response VO")
@Data
public class SensorReportingStatusRespVO {

    @Schema(description = "Whether periodic reporting is running")
    private Boolean running;

    @Schema(description = "Current interval in seconds")
    private Integer intervalSeconds;

    @Schema(description = "Task start time")
    private LocalDateTime startedAt;

    @Schema(description = "Last execution time")
    private LocalDateTime lastRunAt;

    @Schema(description = "Last successful publish count")
    private Integer lastSuccessCount;

    @Schema(description = "Last failed publish count")
    private Integer lastFailureCount;

}
