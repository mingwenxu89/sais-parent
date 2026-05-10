package cn.iocoder.yudao.module.agri.controller.admin.irrigation.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Admin - Irrigation Plan Response VO")
@Data
@ExcelIgnoreUnannotated
public class IrrigationPlanRespVO {

    @ExcelProperty("ID")
    private Long id;

    @ExcelProperty("Farm ID")
    private Long farmId;

    @ExcelProperty("Field ID")
    private Long fieldId;

    @ExcelProperty("Field name")
    private String fieldName;

    @ExcelProperty("Device ID")
    private Long deviceId;

    @ExcelProperty("Crop plan ID")
    private Long cropPlanId;

    @ExcelProperty("Decision source")
    private String decisionSource;

    @ExcelProperty("Decision reason")
    private String decisionReason;

    @ExcelProperty("Planned start time")
    private LocalDateTime plannedStartTime;

    @ExcelProperty("Planned duration (min)")
    private Integer plannedDuration;

    @ExcelProperty("Status")
    private String status;

    @ExcelProperty("Actual start time")
    private LocalDateTime actualStartTime;

    @ExcelProperty("Actual end time")
    private LocalDateTime actualEndTime;

    @ExcelProperty("Water quantity (L)")
    private BigDecimal waterQuantity;

    @ExcelProperty("Created at")
    private LocalDateTime createTime;

}
