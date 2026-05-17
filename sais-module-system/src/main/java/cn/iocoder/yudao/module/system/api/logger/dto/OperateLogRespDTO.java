package cn.iocoder.yudao.module.system.api.logger.dto;

import com.fhs.core.trans.anno.Trans;
import com.fhs.core.trans.constant.TransType;
import com.fhs.core.trans.vo.VO;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * System operation log Resp DTO
 *
 * @author HUIHUI
 */
@Data
public class OperateLogRespDTO implements VO {

    /**
     * Log ID
     */
    private Long id;
    /**
     * link tracking ID
     */
    private String traceId;
    /**
     * User ID
     */
    @Trans(type = TransType.SIMPLE, targetClassName = "cn.iocoder.yudao.module.system.dal.dataobject.user.AdminUserDO",
            fields = "nickname", ref = "userName")
    private Long userId;
    /**
     * Username
     */
    private String userName;
    /**
     * User type
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
     * Operation content
     */
    private String action;
    /**
     * Extend fields
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

    /**
     * Create Time
     */
    private LocalDateTime createTime;

}
