package cn.iocoder.yudao.module.agri.controller.admin.device.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Schema(description = "Admin - Save IoT Device Request VO")
@Data
public class DeviceSaveReqVO {

    @Schema(description = "Device ID", example = "1")
    private Long id;

    @Schema(description = "Device name", requiredMode = Schema.RequiredMode.REQUIRED, example = "Sensor A01")
    @NotBlank(message = "Device name is required")
    private String name;

    @Schema(description = "Device code", requiredMode = Schema.RequiredMode.REQUIRED, example = "DEV-001")
    @NotBlank(message = "Device code is required")
    private String deviceCode;

    @Schema(description = "Device type 1=Sensor 2=Irrigation Controller 3=Weather Station", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "Device type is required")
    private Integer deviceType;

    @Schema(description = "Field ID", example = "1")
    private Long fieldId;

    @Schema(description = "IP address", example = "192.168.1.100")
    private String ipAddress;

    @Schema(description = "Status 1=Online 2=Offline 3=Fault", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "Status is required")
    private Integer status;

    @Schema(description = "Last online time", example = "2024-01-01 12:00:00")
    private LocalDateTime lastOnline;

    @Schema(description = "Description", example = "Installed in east section")
    private String description;

}
