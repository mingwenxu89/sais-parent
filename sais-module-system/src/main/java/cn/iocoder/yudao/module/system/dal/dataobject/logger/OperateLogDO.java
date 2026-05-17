package cn.iocoder.yudao.module.system.dal.dataobject.logger;

import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * Operation log table
 *
 * @author Yudao Source Code
 */
@TableName(value = "system_operate_log", autoResultMap = true)
@KeySequence("system_operate_log_seq") // Primary key auto-increment for Oracle, PostgreSQL, Kingbase, DB2, H2 databases. If it is a database such as MySQL, you DO not need to write it.
@Data
public class OperateLogDO extends BaseDO {

    /**
     * Log primary key
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
     *
     * Associate the id attribute of MemberUserDO, or the id attribute of AdminUserDO
     */
    private Long userId;
    /**
     * User type
     *
     * Association {@link UserTypeEnum}
     */
    private Integer userType;
    /**
     * Operation module type
     */
    private String type;
    /**
     * Operation name
     */
    private String subType;
    /**
     * Operation module business ID
     */
    private Long bizId;
    /**
     * Log content, recording details of the entire operation
     *
     * For example, modify the user information IDed 1, change the gender from male to female, and change the name from Yudao to Yuandao.
     */
    private String action;
    /**
     * Expand fields, some complex businesses need to record some fields (JSON format)
     *
     * For example, record the order ID, { orderId: "1"}
     */
    private String extra;

    /**
     * Request method name
     */
    private String requestMethod;
    /**
     * Request address
     */
    private String requestUrl;
    /**
     * User IP
     */
    private String userIp;
    /**
     * Browser UA
     */
    private String userAgent;

}
