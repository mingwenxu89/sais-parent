package cn.iocoder.yudao.module.infra.controller.admin.file.vo.config;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.util.Map;

@Schema(description = "Management background - file configuration creation/modification Request VO")
@Data
public class FileConfigSaveReqVO {

    @Schema(description = "ID", example = "1")
    private Long id;

    @Schema(description = "Configuration name", requiredMode = Schema.RequiredMode.REQUIRED, example = "S3 - Alibaba Cloud")
    @NotNull(message = "Configuration name cannot be empty")
    private String name;

    @Schema(description = "Storage, see FileStorageEnum enumeration class", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "Memory cannot be empty")
    private Integer storage;

    @Schema(description = "Store configuration. Configuration is a dynamic parameter, so use Map to receive it.", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Storage configuration cannot be empty")
    private Map<String, Object> config;

    @Schema(description = "Remark", example = "I am a note")
    private String remark;

}
