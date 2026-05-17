package cn.iocoder.yudao.module.system.controller.admin.dict.vo.data;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.validation.InEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Management backend - Dict data creation/modification Request VO")
@Data
public class DictDataSaveReqVO {

    @Schema(description = "Dict data ID", example = "1024")
    private Long id;

    @Schema(description = "Display order", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotNull(message = "Display order cannot be empty")
    private Integer sort;

    @Schema(description = "dict tag", requiredMode = Schema.RequiredMode.REQUIRED, example = "taro road")
    @NotBlank(message = "Dict tags cannot be empty")
    @Size(max = 100, message = "Dict tag length cannot exceed 100 characters")
    private String label;

    @Schema(description = "Dict value", requiredMode = Schema.RequiredMode.REQUIRED, example = "iocoder")
    @NotBlank(message = "Dict key value cannot be empty")
    @Size(max = 100, message = "Dict key length cannot exceed 100 characters")
    private String value;

    @Schema(description = "Dict Type", requiredMode = Schema.RequiredMode.REQUIRED, example = "sys_common_sex")
    @NotBlank(message = "Dict type cannot be empty")
    @Size(max = 100, message = "Dict type length cannot exceed 100 characters")
    private String dictType;

    @Schema(description = "Status, see CommonStatusEnum enumeration", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "Status cannot be empty")
    @InEnum(value = CommonStatusEnum.class, message = "Modification status must be {value}")
    private Integer status;

    @Schema(description = "Color type, default, primary, success, info, warning, danger", example = "default")
    private String colorType;

    @Schema(description = "css style", example = "btn-visible")
    private String cssClass;

    @Schema(description = "Remark", example = "i am a character")
    private String remark;

}
