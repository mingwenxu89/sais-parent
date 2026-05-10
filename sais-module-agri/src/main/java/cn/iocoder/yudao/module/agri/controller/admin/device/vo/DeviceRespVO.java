package cn.iocoder.yudao.module.agri.controller.admin.device.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "Admin - IoT Device Response VO")
@Data
@ExcelIgnoreUnannotated
public class DeviceRespVO {

    @Schema(description = "Device ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("Device ID")
    private Long id;

    @Schema(description = "Device name", requiredMode = Schema.RequiredMode.REQUIRED, example = "Sensor A01")
    @ExcelProperty("Device name")
    private String name;

    @Schema(description = "Device code", requiredMode = Schema.RequiredMode.REQUIRED, example = "DEV-001")
    @ExcelProperty("Device code")
    private String deviceCode;

    @Schema(description = "Device type", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("Device type")
    private Integer deviceType;

    @Schema(description = "Field ID", example = "1")
    @ExcelProperty("Field ID")
    private Long fieldId;

    @Schema(description = "IP address", example = "192.168.1.100")
    @ExcelProperty("IP address")
    private String ipAddress;

    @Schema(description = "Status", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("Status")
    private Integer status;

    @Schema(description = "Last online time", example = "2024-01-01 12:00:00")
    @ExcelProperty("Last online time")
    private LocalDateTime lastOnline;

    @Schema(description = "Description", example = "Installed in east section")
    @ExcelProperty("Description")
    private String description;

    @Schema(description = "Created at", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("Created at")
    private LocalDateTime createTime;

}
