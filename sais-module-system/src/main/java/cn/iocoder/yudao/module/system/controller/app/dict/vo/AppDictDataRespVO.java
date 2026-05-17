package cn.iocoder.yudao.module.system.controller.app.dict.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "User App - Dict data information Response VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppDictDataRespVO {

    @Schema(description = "Dict data ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long id;

    @Schema(description = "dict tag", requiredMode = Schema.RequiredMode.REQUIRED, example = "taro road")
    private String label;

    @Schema(description = "Dict value", requiredMode = Schema.RequiredMode.REQUIRED, example = "iocoder")
    private String value;

    @Schema(description = "Dict Type", requiredMode = Schema.RequiredMode.REQUIRED, example = "sys_common_sex")
    private String dictType;

}
