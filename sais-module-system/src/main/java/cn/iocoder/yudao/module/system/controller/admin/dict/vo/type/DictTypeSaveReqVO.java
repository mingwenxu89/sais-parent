package cn.iocoder.yudao.module.system.controller.admin.dict.vo.type;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Management background - Dict type creation/modification Request VO")
@Data
public class DictTypeSaveReqVO {

    @Schema(description = "Dict type ID", example = "1024")
    private Long id;

    @Schema(description = "Dict name", requiredMode = Schema.RequiredMode.REQUIRED, example = "gender")
    @NotBlank(message = "Dict name cannot be empty")
    @Size(max = 100, message = "Dict type name cannot exceed 100 characters in length")
    private String name;

    @Schema(description = "Dict Type", requiredMode = Schema.RequiredMode.REQUIRED, example = "sys_common_sex")
    @NotNull(message = "Dict type cannot be empty")
    @Size(max = 100, message = "Dict type type length cannot exceed 100 characters")
    private String type;

    @Schema(description = "Status, see CommonStatusEnum enumeration class", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "Status cannot be empty")
    private Integer status;

    @Schema(description = "Remark", example = "happy note")
    private String remark;

}
