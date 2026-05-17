package cn.iocoder.yudao.module.infra.controller.admin.codegen.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "Management background - code generation preview Response VO, note that each file is an object")
@Data
public class CodegenPreviewRespVO {

    @Schema(description = "file path", requiredMode = Schema.RequiredMode.REQUIRED, example = "java/cn/iocoder/yudao/adminserver/modules/system/controller/test/SysTestDemoController.java")
    private String filePath;

    @Schema(description = "code", requiredMode = Schema.RequiredMode.REQUIRED, example = "Hello World")
    private String code;

}
