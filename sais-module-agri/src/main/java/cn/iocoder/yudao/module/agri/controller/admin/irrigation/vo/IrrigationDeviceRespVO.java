package cn.iocoder.yudao.module.agri.controller.admin.irrigation.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Irrigation Device Response VO")
@Data
@ExcelIgnoreUnannotated
public class IrrigationDeviceRespVO {

    private Long id;

    private String deviceCode;

    private Long farmId;

    private String farmName;

    private Long fieldId;

    private String fieldName;

    private BigDecimal flowRate;

    private Boolean isWatering;

    private Integer status;

    private Long sensorId;

    private String sensorCode;

    /** Start time of the current watering session (null when not watering) */
    private LocalDateTime wateringStartedAt;

    private Boolean simulateFault;

}
