package cn.iocoder.yudao.module.system.controller.admin.permission.vo.menu;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;

@Schema(description = "Management backend - menu streamlined information Response VO")
@Data
public class MenuSimpleRespVO {

    @Schema(description = "menu ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long id;

    @Schema(description = "Menu name", requiredMode = Schema.RequiredMode.REQUIRED, example = "taro road")
    private String name;

    @Schema(description = "Parent menu ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long parentId;

    @Schema(description = "Type, see MenuTypeEnum enumeration class", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer type;

}
