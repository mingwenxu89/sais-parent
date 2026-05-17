package cn.iocoder.yudao.module.infra.controller.admin.codegen.vo.table;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "Management background - database table definition Response VO")
@Data
public class DatabaseTableRespVO {

    @Schema(description = "table name", requiredMode = Schema.RequiredMode.REQUIRED, example = "yuanma")
    private String name;

    @Schema(description = "Table description", requiredMode = Schema.RequiredMode.REQUIRED, example = "Yudao Source Code")
    private String comment;

}
