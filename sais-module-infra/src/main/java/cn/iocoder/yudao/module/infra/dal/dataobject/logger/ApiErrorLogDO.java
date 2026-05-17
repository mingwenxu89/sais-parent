package cn.iocoder.yudao.module.infra.dal.dataobject.logger;

import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.module.infra.enums.logger.ApiErrorLogProcessStatusEnum;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

/**
 * API exception data
 *
 * @author Yudao Source Code
 */
@TableName("infra_api_error_log")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@KeySequence(value = "infra_api_error_log_seq")
public class ApiErrorLogDO extends BaseDO {

    /**
     * The maximum length of {@link #requestParams}
     */
    public static final Integer REQUEST_PARAMS_MAX_LENGTH = 8000;

    /**
     * ID
     */
    @TableId
    private Long id;
    /**
     * User ID
     */
    private Long userId;
    /**
     * link tracking ID
     *
     * Generally speaking, through the link tracking ID, access logs, error logs, link tracking logs, logger print logs, etc. can be combined together for troubleshooting.
     */
    private String traceId;
    /**
     * User type
     *
     * Enumeration {@link UserTypeEnum}
     */
    private Integer userType;
    /**
     * Application name
     *
     * Currently reading Spring.application.name
     */
    private String applicationName;

    // ========== Request related fields ==========

    /**
     * Request method name
     */
    private String requestMethod;
    /**
     * Access address
     */
    private String requestUrl;
    /**
     * Request parameters
     *
     * query: Query String
     * body: Quest Body
     */
    private String requestParams;
    /**
     * User IP
     */
    private String userIp;
    /**
     * Browser UA
     */
    private String userAgent;

    // ========== Exception related fields ==========

    /**
     * Exception occurrence time
     */
    private LocalDateTime exceptionTime;
    /**
     * Exception name
     *
     * Full class name of {@link Throwable#getClass()}
     */
    private String exceptionName;
    /**
     * Messages caused by exceptions
     *
     * {@link cn.hutool.core.exceptions.ExceptionUtil#getMessage(Throwable)}
     */
    private String exceptionMessage;
    /**
     * Root message caused by exception
     *
     * {@link cn.hutool.core.exceptions.ExceptionUtil#getRootCauseMessage(Throwable)}
     */
    private String exceptionRootCauseMessage;
    /**
     * Exception stack trace
     *
     * {@link org.apache.commons.lang3.exception.ExceptionUtils#getStackTrace(Throwable)}
     */
    private String exceptionStackTrace;
    /**
     * The full name of the class where the exception occurred
     *
     * {@link StackTraceElement#getClassName()}
     */
    private String exceptionClassName;
    /**
     * The class file where the exception occurred
     *
     * {@link StackTraceElement#getFileName()}
     */
    private String exceptionFileName;
    /**
     * The method name where the exception occurred
     *
     * {@link StackTraceElement#getMethodName()}
     */
    private String exceptionMethodName;
    /**
     * The line of the method where the exception occurred
     *
     * {@link StackTraceElement#getLineNumber()}
     */
    private Integer exceptionLineNumber;

    // ========== Process related fields ==========

    /**
     * Processing status
     *
     * Enum {@link ApiErrorLogProcessStatusEnum}
     */
    private Integer processStatus;
    /**
     * processing time
     */
    private LocalDateTime processTime;
    /**
     * Handle user ID
     *
     * Association cn.iocoder.yudao.adminserver.modules.system.dal.dataobject.user.SysUserDO.SysUserDO#getId()
     */
    private Long processUserId;

}
