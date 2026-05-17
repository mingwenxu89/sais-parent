package cn.iocoder.yudao.module.system.controller.admin.dict.vo.type;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "Management backend - Dict type simplified information Response VO")
@Data
public class DictTypeSimpleRespVO {

    @Schema(description = "Dict type ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long id;

    @Schema(description = "Dict type name", requiredMode = Schema.RequiredMode.REQUIRED, example = "taro road")
    private String name;

    @Schema(description = "Dict Type", requiredMode = Schema.RequiredMode.REQUIRED, example = "sys_common_sex")
    private String type;

}
