package cn.iocoder.yudao.module.system.controller.admin.user.vo.user;

import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;
import cn.iocoder.yudao.module.system.enums.DictTypeConstants;
import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Schema(description = "Management backend - User information Response VO")
@Data
@ExcelIgnoreUnannotated
public class UserRespVO{

    @Schema(description = "User ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("User ID")
    private Long id;

    @Schema(description = "User account", requiredMode = Schema.RequiredMode.REQUIRED, example = "yudao")
    @ExcelProperty("Username")
    private String username;

    @Schema(description = "User nickname", requiredMode = Schema.RequiredMode.REQUIRED, example = "Yunai")
    @ExcelProperty("User nickname")
    private String nickname;

    @Schema(description = "Remark", example = "I am a user")
    private String remark;

    @Schema(description = "Department ID", example = "I am a user")
    private Long deptId;
    @Schema(description = "Department name", example = "IT Department")
    @ExcelProperty("Department name")
    private String deptName;

    @Schema(description = "Position ID array", example = "1")
    private Set<Long> postIds;

    @Schema(description = "User email", example = "yudao@iocoder.cn")
    @ExcelProperty("User email")
    private String email;

    @Schema(description = "Mobile phone ID", example = "15601691300")
    @ExcelProperty("Mobile phone ID")
    private String mobile;

    @Schema(description = "User gender, see SexEnum enumeration class", example = "1")
    @ExcelProperty(value = "User gender", converter = DictConvert.class)
    @DictFormat(DictTypeConstants.USER_SEX)
    private Integer sex;

    @Schema(description = "User avatar", example = "https://www.iocoder.cn/xxx.png")
    private String avatar;

    @Schema(description = "Status, see CommonStatusEnum enumeration class", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty(value = "Account status", converter = DictConvert.class)
    @DictFormat(DictTypeConstants.COMMON_STATUS)
    private Integer status;

    @Schema(description = "Last login IP", requiredMode = Schema.RequiredMode.REQUIRED, example = "192.168.1.1")
    @ExcelProperty("Last login IP")
    private String loginIp;

    @Schema(description = "Last login time", requiredMode = Schema.RequiredMode.REQUIRED, example = "timestamp format")
    @ExcelProperty("Last login time")
    private LocalDateTime loginDate;

    @Schema(description = "Create Time", requiredMode = Schema.RequiredMode.REQUIRED, example = "timestamp format")
    private LocalDateTime createTime;

}
