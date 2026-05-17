package cn.iocoder.yudao.framework.common.biz.system.logger.dto;

import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * System operation log Create Request DTO
 *
 * @author HUIHUI
 */
@Data
public class OperateLogCreateReqDTO {

    /**
     * link tracking number
     *
     * Generally speaking, through the link tracking number, access logs, error logs, link tracking logs, logger print logs, etc. can be combined together for troubleshooting.
     */
    private String traceId;
    /**
     * User ID
     *
     * Associate the ID attribute of MemberUserDO, or the ID attribute of AdminUserDO
     */
    @NotNull(message = "user ID cannot be empty")
    private Long userId;
    /**
     * User type
     *
     * Association {@link UserTypeEnum}
     */
    @NotNull(message = "user type cannot be empty")
    private Integer userType;
    /**
     * Operation module type
     */
    @NotEmpty(message = "operation module type cannot be empty")
    private String type;
    /**
     * Operation name
     */
    @NotEmpty(message = "operation name cannot be empty")
    private String subType;
    /**
     * Operation module business number
     */
    @NotNull(message = "the operation module business number cannot be empty.")
    private Long bizId;
    /**
     * Operation content, record the details of the entire operation
     * For example, modify the user information numbered 1, change the gender from male to female, and change the name from Yudao to Yuandao.
     */
    @NotEmpty(message = "the operation content cannot be empty")
    private String action;
    /**
     * Expand fields, some complex businesses need to record some fields (JSON format)
     * For example, record the order number, { orderId: "1"}
     */
    private String extra;

    /**
     * Request method name
     */
    @NotEmpty(message = "the request method name cannot be empty")
    private String requestMethod;
    /**
     * Request address
     */
    @NotEmpty(message = "the request address cannot be empty")
    private String requestUrl;
    /**
     * User IP
     */
    @NotEmpty(message = "user IP cannot be empty")
    private String userIp;
    /**
     * Browser UA
     */
    @NotEmpty(message = "browser UA cannot be empty")
    private String userAgent;

}
