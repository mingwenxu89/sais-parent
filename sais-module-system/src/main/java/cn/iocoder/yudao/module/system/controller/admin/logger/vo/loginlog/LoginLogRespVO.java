package cn.iocoder.yudao.module.system.controller.admin.logger.vo.loginlog;

import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;
import cn.iocoder.yudao.module.system.enums.DictTypeConstants;
import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "Management background - Login log Response VO")
@Data
@ExcelIgnoreUnannotated
public class LoginLogRespVO {

    @Schema(description = "Log ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @ExcelProperty("Log primary key")
    private Long id;

    @Schema(description = "Log type, see LoginLogTypeEnum enumeration class", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty(value = "Log type", converter = DictConvert.class)
    @DictFormat(DictTypeConstants.LOGIN_TYPE)
    private Integer logType;

    @Schema(description = "User ID", example = "666")
    private Long userId;

    @Schema(description = "User type, see UserTypeEnum enumeration", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private Integer userType;

    @Schema(description = "link tracking ID", example = "89aca178-a370-411c-ae02-3f0d672be4ab")
    private String traceId;

    @Schema(description = "User account", requiredMode = Schema.RequiredMode.REQUIRED, example = "yudao")
    @ExcelProperty("User account")
    private String username;

    @Schema(description = "Login results, see LoginResultEnum enumeration class", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty(value = "Login results", converter = DictConvert.class)
    @DictFormat(DictTypeConstants.LOGIN_RESULT)
    private Integer result;

    @Schema(description = "User IP", requiredMode = Schema.RequiredMode.REQUIRED, example = "127.0.0.1")
    @ExcelProperty("Login IP")
    private String userIp;

    @Schema(description = "Browser UserAgent", example = "Mozilla/5.0")
    @ExcelProperty("Browser UA")
    private String userAgent;

    @Schema(description = "Login time", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("Login time")
    private LocalDateTime createTime;

}
