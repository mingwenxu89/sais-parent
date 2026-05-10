package cn.iocoder.yudao.module.agri.controller.admin.alert.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "Admin - Alert Response VO")
@Data
@ExcelIgnoreUnannotated
public class AlertRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("ID")
    private Long id;

    @Schema(description = "Farm ID", example = "1")
    @ExcelProperty("Farm ID")
    private Long farmId;

    @Schema(description = "Field ID", example = "1")
    @ExcelProperty("Field ID")
    private Long fieldId;

    @Schema(description = "Field name")
    private String fieldName;

    @Schema(description = "Related irrigation plan ID", example = "5")
    private Long irrigationPlanId;

    @Schema(description = "Alert type (1=SENSOR_ABNORMAL 2=EXTREME_WEATHER 3=IRRIGATION_ABNORMAL)",
            requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("Alert type")
    private Integer alertType;

    @Schema(description = "Alert level (1=INFO 2=WARN 3=CRITICAL)",
            requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty("Alert level")
    private Integer level;

    @Schema(description = "Alert description", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("Alert description")
    private String context;

    @Schema(description = "Status (0=UNHANDLED 1=HANDLING 2=RESOLVED 3=IGNORED)",
            requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    @ExcelProperty("Status")
    private Integer status;

    @Schema(description = "Triggered at", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("Triggered at")
    private LocalDateTime triggeredAt;

    @Schema(description = "Handled at")
    @ExcelProperty("Handled at")
    private LocalDateTime handledAt;

    @Schema(description = "Created at", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("Created at")
    private LocalDateTime createTime;

}
