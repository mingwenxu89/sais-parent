package cn.iocoder.yudao.module.system.controller.admin.dict.vo.data;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.validation.InEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.Size;

@Schema(description = "Management backend - Dict type paging list Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class DictDataPageReqVO extends PageParam {

    @Schema(description = "dict tag", example = "taro road")
    @Size(max = 100, message = "Dict tag length cannot exceed 100 characters")
    private String label;

    @Schema(description = "Dict type, fuzzy matching", example = "sys_common_sex")
    @Size(max = 100, message = "Dict type type length cannot exceed 100 characters")
    private String dictType;

    @Schema(description = "Display status, see CommonStatusEnum enumeration class", example = "1")
    @InEnum(value = CommonStatusEnum.class, message = "Modification status must be {value}")
    private Integer status;

}
