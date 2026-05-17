package cn.iocoder.yudao.module.system.api.user.dto;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import lombok.Data;

import java.util.Set;

/**
 * Admin User Response DTO
 *
 * @author Yudao Source Code
 */
@Data
public class AdminUserRespDTO {

    /**
     * User ID
     */
    private Long id;
    /**
     * User nickname
     */
    private String nickname;
    /**
     * Account status
     *
     * Enum {@link CommonStatusEnum}
     */
    private Integer status;

    /**
     * Department ID
     */
    private Long deptId;
    /**
     * Position ID array
     */
    private Set<Long> postIds;
    /**
     * Mobile phone ID
     */
    private String mobile;
    /**
     * User avatar
     */
    private String avatar;

}
