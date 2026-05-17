package cn.iocoder.yudao.module.system.controller.admin.user.vo.user;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.validation.InEnum;
import cn.iocoder.yudao.framework.dict.validation.InDict;
import cn.iocoder.yudao.module.system.enums.DictTypeConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotNull;

@Schema(description = "Management backend - User update status Request VO")
@Data
public class UserUpdateStatusReqVO {

    @Schema(description = "User ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotNull(message = "Role ID cannot be empty")
    private Long id;

    @Schema(description = "Status, see CommonStatusEnum enumeration", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "Status cannot be empty")
    @InEnum(value = CommonStatusEnum.class, message = "Modification status must be {value}")
    @InDict(type = DictTypeConstants.COMMON_STATUS)
    private Integer status;

}
