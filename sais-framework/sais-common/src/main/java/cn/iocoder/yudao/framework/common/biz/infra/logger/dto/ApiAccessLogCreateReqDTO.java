package cn.iocoder.yudao.framework.common.biz.infra.logger.dto;

import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * API access log
 *
 * @author Yudao Source Code
 */
@Data
public class ApiAccessLogCreateReqDTO {

 /**
     * link tracking number
 */
 private String traceId;
 /**
     * User ID
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
 private String requestParams;
 /**
     * response result
 */
 private String responseBody;
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
     * Operation module
 */
 private String operateModule;
 /**
     * Operation name
 */
 private String operateName;
 /**
     * Operation classification
 *
     * Enumeration, see OperateTypeEnum class
 */
 private Integer operateType;

 /**
     * Start request time
 */
    @NotNull(message = "start request time cannot be empty")
 private LocalDateTime beginTime;
 /**
     * end request time
 */
    @NotNull(message = "end request time cannot be empty")
 private LocalDateTime endTime;
 /**
     * Execution time, unit: milliseconds
 */
    @NotNull(message = "execution duration cannot be empty")
 private Integer duration;
 /**
     * result code
 */
    @NotNull(message = "error code cannot be empty")
 private Integer resultCode;
 /**
     * Result prompt
 */
 private String resultMsg;

}
