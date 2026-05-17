package cn.iocoder.yudao.module.infra.controller.admin.logger.vo.apiaccesslog;

import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;
import cn.iocoder.yudao.module.infra.enums.DictTypeConstants;
import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "Management backend - API access log Response VO")
@Data
@ExcelIgnoreUnannotated
public class ApiAccessLogRespVO {

    @Schema(description = "Log primary key", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @ExcelProperty("Log primary key")
    private Long id;

    @Schema(description = "link tracking ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "66600cb6-7852-11eb-9439-0242ac130002")
    @ExcelProperty("link tracking ID")
    private String traceId;

    @Schema(description = "User ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "666")
    @ExcelProperty("User ID")
    private Long userId;

    @Schema(description = "User type, see UserTypeEnum enumeration", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty(value = "User type", converter = DictConvert.class)
    @DictFormat(DictTypeConstants.USER_TYPE)
    private Integer userType;

    @Schema(description = "Application name", requiredMode = Schema.RequiredMode.REQUIRED, example = "dashboard")
    @ExcelProperty("Application name")
    private String applicationName;

    @Schema(description = "Request method name", requiredMode = Schema.RequiredMode.REQUIRED, example = "GET")
    @ExcelProperty("Request method name")
    private String requestMethod;

    @Schema(description = "Request address", requiredMode = Schema.RequiredMode.REQUIRED, example = "/xxx/yyy")
    @ExcelProperty("Request address")
    private String requestUrl;

    @Schema(description = "Request parameters")
    @ExcelProperty("Request parameters")
    private String requestParams;

    @Schema(description = "response result")
    @ExcelProperty("response result")
    private String responseBody;

    @Schema(description = "User IP", requiredMode = Schema.RequiredMode.REQUIRED, example = "127.0.0.1")
    @ExcelProperty("User IP")
    private String userIp;

    @Schema(description = "Browser UA", requiredMode = Schema.RequiredMode.REQUIRED, example = "Mozilla/5.0")
    @ExcelProperty("Browser UA")
    private String userAgent;

    @Schema(description = "Operation module", requiredMode = Schema.RequiredMode.REQUIRED, example = "Product module")
    @ExcelProperty("Operation module")
    private String operateModule;

    @Schema(description = "Operation name", requiredMode = Schema.RequiredMode.REQUIRED, example = "Create product")
    @ExcelProperty("Operation name")
    private String operateName;

    @Schema(description = "Operation classification", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty(value = "Operation classification", converter = DictConvert.class)
    @DictFormat(DictTypeConstants.OPERATE_TYPE)
    private Integer operateType;

    @Schema(description = "Start request time", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("Start request time")
    private LocalDateTime beginTime;

    @Schema(description = "end request time", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("end request time")
    private LocalDateTime endTime;

    @Schema(description = "Execution time", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
    @ExcelProperty("Execution time")
    private Integer duration;

    @Schema(description = "result code", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    @ExcelProperty("result code")
    private Integer resultCode;

    @Schema(description = "Result prompt", example = "Taro source code, awesome!")
    @ExcelProperty("Result prompt")
    private String resultMsg;

    @Schema(description = "Create Time", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}
