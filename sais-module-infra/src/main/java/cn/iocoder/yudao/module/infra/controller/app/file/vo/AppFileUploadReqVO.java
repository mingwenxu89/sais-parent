package cn.iocoder.yudao.module.infra.controller.app.file.vo;

import cn.iocoder.yudao.module.infra.controller.admin.file.vo.file.FileUploadReqVO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;

@Schema(description = "User App - Upload File Request VO")
@Data
public class AppFileUploadReqVO {

    @Schema(description = "file attachment", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "file attachment cannot be empty")
    private MultipartFile file;

    @Schema(description = "File directory", example = "XXX/YYY")
    private String directory;

    @AssertTrue(message = "file directory is incorrect")
    @JsonIgnore
    public boolean isDirectoryValid() {
        return FileUploadReqVO.isDirectoryValid(directory);
    }

}
