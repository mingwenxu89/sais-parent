package cn.iocoder.yudao.module.infra.dal.dataobject.logger;

import cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum;
import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

/**
 * API access log
 *
 * @author Yudao Source Code
 */
@TableName("infra_api_access_log")
@KeySequence(value = "infra_api_access_log_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiAccessLogDO extends BaseDO {

    /**
     * The maximum length of {@link #requestParams}
     */
    public static final Integer REQUEST_PARAMS_MAX_LENGTH = 8000;

    /**
     * The maximum length of {@link #resultMsg}
     */
    public static final Integer RESULT_MSG_MAX_LENGTH = 512;

    /**
     * ID
     */
    @TableId
    private Long id;
    /**
     * link tracking ID
     *
     * Generally speaking, through the link tracking ID, access logs, error logs, link tracking logs, logger print logs, etc. can be combined together for troubleshooting.
     */
    private String traceId;
    /**
     * User ID
     */
    private Long userId;
    /**
     * User type
     *
     * Enumeration {@link UserTypeEnum}
     */
    private Integer userType;
    /**
     * Application name
     *
     * Currently reading the `Spring.application.name` configuration item
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
     * response result
     */
    private String responseBody;
    /**
     * User IP
     */
    private String userIp;
    /**
     * Browser UA
     */
    private String userAgent;

    // ========== Execution related fields ==========

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
     * Enum {@link OperateTypeEnum}
     */
    private Integer operateType;

    /**
     * Start request time
     */
    private LocalDateTime beginTime;
    /**
     * end request time
     */
    private LocalDateTime endTime;
    /**
     * Execution time, unit: milliseconds
     */
    private Integer duration;

    /**
     * result code
     *
     * Currently using the {@link CommonResult#getCode()} attribute
     */
    private Integer resultCode;
    /**
     * Result prompt
     *
     * Currently using the {@link CommonResult#getMsg()} attribute
     */
    private String resultMsg;

}
