package cn.iocoder.yudao.module.infra.controller.admin.config.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Management background - Parameter configuration creation/modification Request VO")
@Data
public class ConfigSaveReqVO {

    @Schema(description = "Parameter configuration serial ID", example = "1024")
    private Long id;

    @Schema(description = "Parameter grouping", requiredMode = Schema.RequiredMode.REQUIRED, example = "biz")
    @NotEmpty(message = "Parameter group cannot be empty")
    @Size(max = 50, message = "Parameter names cannot exceed 50 characters")
    private String category;

    @Schema(description = "Parameter name", requiredMode = Schema.RequiredMode.REQUIRED, example = "Database name")
    @NotBlank(message = "Parameter name cannot be empty")
    @Size(max = 100, message = "Parameter names cannot exceed 100 characters")
    private String name;

    @Schema(description = "Parameter key name", requiredMode = Schema.RequiredMode.REQUIRED, example = "yunai.db.username")
    @NotBlank(message = "Parameter key name length cannot be empty")
    @Size(max = 100, message = "Parameter key name length cannot exceed 100 characters")
    private String key;

    @Schema(description = "Parameter key value", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotBlank(message = "Parameter key value cannot be empty")
    @Size(max = 500, message = "Parameter key length cannot exceed 500 characters")
    private String value;

    @Schema(description = "visible or not", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
    @NotNull(message = "Whether visible cannot be empty")
    private Boolean visible;

    @Schema(description = "Remark", example = "Note that he is very handsome!")
    private String remark;

}
