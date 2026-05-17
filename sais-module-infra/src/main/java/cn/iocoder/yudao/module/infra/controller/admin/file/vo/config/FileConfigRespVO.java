package cn.iocoder.yudao.module.infra.controller.admin.file.vo.config;

import cn.iocoder.yudao.module.infra.framework.file.core.client.FileClientConfig;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "Management background - file configuration Response VO")
@Data
public class FileConfigRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "Configuration name", requiredMode = Schema.RequiredMode.REQUIRED, example = "S3 - Alibaba Cloud")
    private String name;

    @Schema(description = "Storage, see FileStorageEnum enumeration class", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer storage;

    @Schema(description = "Is it the main configuration?", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
    private Boolean master;

    @Schema(description = "Storage configuration", requiredMode = Schema.RequiredMode.REQUIRED)
    private FileClientConfig config;

    @Schema(description = "Remark", example = "I am a note")
    private String remark;

    @Schema(description = "Create Time", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}
