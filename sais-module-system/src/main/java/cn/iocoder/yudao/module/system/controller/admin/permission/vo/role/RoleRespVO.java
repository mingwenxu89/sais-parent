package cn.iocoder.yudao.module.system.controller.admin.permission.vo.role;

import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;
import cn.iocoder.yudao.module.system.enums.DictTypeConstants;
import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Set;

@Schema(description = "Management backend - role information Response VO")
@Data
@ExcelIgnoreUnannotated
public class RoleRespVO {

    @Schema(description = "role ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("Role serial ID")
    private Long id;

    @Schema(description = "Character name", requiredMode = Schema.RequiredMode.REQUIRED, example = "Administrator")
    @ExcelProperty("Character name")
    private String name;

    @Schema(description = "character mark", requiredMode = Schema.RequiredMode.REQUIRED, example = "admin")
    @NotBlank(message = "Role flag cannot be empty")
    @ExcelProperty("character mark")
    private String code;

    @Schema(description = "Display order", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @ExcelProperty("Role sorting")
    private Integer sort;

    @Schema(description = "Status, see CommonStatusEnum enumeration class", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty(value = "character status", converter = DictConvert.class)
    @DictFormat(DictTypeConstants.COMMON_STATUS)
    private Integer status;

    @Schema(description = "Role type, see RoleTypeEnum enumeration class", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer type;

    @Schema(description = "Remark", example = "i am a character")
    private String remark;

    @Schema(description = "Data range, see DataScopeEnum enumeration class", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty(value = "data range", converter = DictConvert.class)
    @DictFormat(DictTypeConstants.DATA_SCOPE)
    private Integer dataScope;

    @Schema(description = "Data range (specified department array)", example = "1")
    private Set<Long> dataScopeDeptIds;

    @Schema(description = "Create Time", requiredMode = Schema.RequiredMode.REQUIRED, example = "timestamp format")
    private LocalDateTime createTime;

}
