package cn.iocoder.yudao.module.infra.controller.admin.logger.vo.apierrorlog;

import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;
import cn.iocoder.yudao.module.infra.enums.DictTypeConstants;
import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "Management backend - API error log Response VO")
@Data
@ExcelIgnoreUnannotated
public class ApiErrorLogRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @ExcelProperty("ID")
    private Long id;

    @Schema(description = "link tracking ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "66600cb6-7852-11eb-9439-0242ac130002")
    @ExcelProperty("link tracking ID")
    private String traceId;

    @Schema(description = "User ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "666")
    @ExcelProperty("User ID")
    private Long userId;

    @Schema(description = "User type", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty(value = "User type", converter = DictConvert.class)
    @DictFormat(DictTypeConstants.USER_TYPE)
    private Integer userType;

    @Schema(description = "Application name", requiredMode = Schema.RequiredMode.REQUIRED, example = "dashboard")
    @ExcelProperty("Application name")
    private String applicationName;

    @Schema(description = "Request method name", requiredMode = Schema.RequiredMode.REQUIRED, example = "GET")
    @ExcelProperty("Request method name")
    private String requestMethod;

    @Schema(description = "Request address", requiredMode = Schema.RequiredMode.REQUIRED, example = "/xx/yy")
    @ExcelProperty("Request address")
    private String requestUrl;

    @Schema(description = "Request parameters", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("Request parameters")
    private String requestParams;

    @Schema(description = "User IP", requiredMode = Schema.RequiredMode.REQUIRED, example = "127.0.0.1")
    @ExcelProperty("User IP")
    private String userIp;

    @Schema(description = "Browser UA", requiredMode = Schema.RequiredMode.REQUIRED, example = "Mozilla/5.0")
    @ExcelProperty("Browser UA")
    private String userAgent;

    @Schema(description = "Exception occurrence time", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("Exception occurrence time")
    private LocalDateTime exceptionTime;

    @Schema(description = "Exception name", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("Exception name")
    private String exceptionName;

    @Schema(description = "Messages caused by exceptions", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("Messages caused by exceptions")
    private String exceptionMessage;

    @Schema(description = "Root message caused by exception", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("Root message caused by exception")
    private String exceptionRootCauseMessage;

    @Schema(description = "Exception stack trace", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("Exception stack trace")
    private String exceptionStackTrace;

    @Schema(description = "The full name of the class where the exception occurred", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("The full name of the class where the exception occurred")
    private String exceptionClassName;

    @Schema(description = "The class file where the exception occurred", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("The class file where the exception occurred")
    private String exceptionFileName;

    @Schema(description = "The method name where the exception occurred", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("The method name where the exception occurred")
    private String exceptionMethodName;

    @Schema(description = "The line of the method where the exception occurred", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("The line of the method where the exception occurred")
    private Integer exceptionLineNumber;

    @Schema(description = "Processing status", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    @ExcelProperty(value = "Processing status", converter = DictConvert.class)
    @DictFormat(DictTypeConstants.API_ERROR_LOG_PROCESS_STATUS)
    private Integer processStatus;

    @Schema(description = "processing time", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("processing time")
    private LocalDateTime processTime;

    @Schema(description = "Handle user ID", example = "233")
    @ExcelProperty("Handle user ID")
    private Integer processUserId;

    @Schema(description = "Create Time", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("Create Time")
    private LocalDateTime createTime;

}
