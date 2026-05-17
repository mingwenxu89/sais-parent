package cn.iocoder.yudao.module.system.controller.admin.dict.vo.data;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "Management backend - data dict simplification Response VO")
@Data
public class DictDataSimpleRespVO {

    @Schema(description = "Dict Type", requiredMode = Schema.RequiredMode.REQUIRED, example = "gender")
    private String dictType;

    @Schema(description = "Dict key", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private String value;

    @Schema(description = "dict tag", requiredMode = Schema.RequiredMode.REQUIRED, example = "Male")
    private String label;

    @Schema(description = "Color type, default, primary, success, info, warning, danger", example = "default")
    private String colorType;

    @Schema(description = "css style", example = "btn-visible")
    private String cssClass;

}
