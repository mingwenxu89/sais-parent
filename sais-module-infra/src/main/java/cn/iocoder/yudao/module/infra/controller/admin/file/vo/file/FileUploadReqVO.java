package cn.iocoder.yudao.module.infra.controller.admin.file.vo.file;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Management backend - Upload files Request VO")
@Data
public class FileUploadReqVO {

    @Schema(description = "file attachment", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "file attachment cannot be empty")
    private MultipartFile file;

    @Schema(description = "File directory", example = "XXX/YYY")
    private String directory;

    @AssertTrue(message = "file directory is incorrect")
    @JsonIgnore
    public boolean isDirectoryValid() {
        return isDirectoryValid(directory);
    }

    public static boolean isDirectoryValid(String directory) {
        // 1. Cannot contain .. to prevent directory traversal
        // 2. Cannot start with / or \ to prevent uploading to the root directory
        return !StrUtil.contains(directory, "..")
                && !StrUtil.startWithAny(directory, "/", "\\");
    }

}
