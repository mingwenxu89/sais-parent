package cn.iocoder.yudao.framework.common.biz.infra.logger.dto;

import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * API error log
 *
 * @author Yudao Source Code
 */
@Data
public class ApiErrorLogCreateReqDTO {

 /**
     * link number
 */
 private String traceId;
 /**
     * Account number
 */
 private Long userId;
 /**
     * User type
 */
 private Integer userType;
 /**
     * Application name
 */
    @NotNull(message = "application name cannot be empty")
 private String applicationName;

 /**
     * Request method name
 */
    @NotNull(message = "http request method cannot be empty")
 private String requestMethod;
 /**
     * Access address
 */
    @NotNull(message = "access address cannot be empty")
 private String requestUrl;
 /**
     * Request parameters
 */
    @NotNull(message = "request parameters cannot be empty")
 private String requestParams;
 /**
     * User IP
 */
    @NotNull(message = "ip cannot be empty")
 private String userIp;
 /**
     * Browser UA
 */
    @NotNull(message = "user-Agent cannot be empty")
 private String userAgent;

 /**
     * abnormal time
 */
    @NotNull(message = "exception time cannot be empty")
 private LocalDateTime exceptionTime;
 /**
     * Exception name
 */
    @NotNull(message = "exception name cannot be empty")
 private String exceptionName;
 /**
     * The full name of the class where the exception occurred
 */
    @NotNull(message = "the full name of the class where the exception occurred cannot be empty.")
 private String exceptionClassName;
 /**
     * The class file where the exception occurred
 */
    @NotNull(message = "the class file where the exception occurred cannot be empty")
 private String exceptionFileName;
 /**
     * The method name where the exception occurred
 */
    @NotNull(message = "the method name where the exception occurred cannot be empty")
 private String exceptionMethodName;
 /**
     * The line of the method where the exception occurred
 */
    @NotNull(message = "the line where the exception occurs cannot be empty")
 private Integer exceptionLineNumber;
 /**
     * Exception stack traceException stack trace
 */
    @NotNull(message = "exception stack trace cannot be empty")
 private String exceptionStackTrace;
 /**
     * Root message caused by exception
 */
    @NotNull(message = "the root message caused by the exception cannot be empty")
 private String exceptionRootCauseMessage;
 /**
     * Messages caused by exceptions
 */
    @NotNull(message = "the message caused by the exception cannot be empty")
 private String exceptionMessage;


}
