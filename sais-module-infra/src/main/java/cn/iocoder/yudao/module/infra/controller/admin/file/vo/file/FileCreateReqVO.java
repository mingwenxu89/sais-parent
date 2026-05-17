package cn.iocoder.yudao.module.infra.controller.admin.file.vo.file;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotNull;

@Schema(description = "Management backend - file creation Request VO")
@Data
public class FileCreateReqVO {

    @NotNull(message = "The file configuration ID cannot be empty")
    @Schema(description = "File configuration ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "11")
    private Long configId;

    @NotNull(message = "File path cannot be empty")
    @Schema(description = "file path", requiredMode = Schema.RequiredMode.REQUIRED, example = "yudao.jpg")
    private String path;

    @NotNull(message = "The original file name cannot be empty")
    @Schema(description = "Original file name", requiredMode = Schema.RequiredMode.REQUIRED, example = "yudao.jpg")
    private String name;

    @NotNull(message = "File URL cannot be empty")
    @Schema(description = "File URL", requiredMode = Schema.RequiredMode.REQUIRED, example = "https://www.iocoder.cn/yudao.jpg")
    private String url;

    @Schema(description = "File MIME type", example = "application/octet-stream")
    private String type;

    @Schema(description = "file size", example = "2048", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long size;

}
