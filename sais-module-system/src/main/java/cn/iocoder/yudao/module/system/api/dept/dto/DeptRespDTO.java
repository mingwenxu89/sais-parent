package cn.iocoder.yudao.module.system.api.dept.dto;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import lombok.Data;

/**
 * Department Response DTO
 *
 * @author Yudao Source Code
 */
@Data
public class DeptRespDTO {

    /**
     * Department ID
     */
    private Long id;
    /**
     * Department name
     */
    private String name;
    /**
     * Parent department ID
     */
    private Long parentId;
    /**
     * User ID of the person in charge
     */
    private Long leaderUserId;
    /**
     * Department status
     *
     * Enum {@link CommonStatusEnum}
     */
    private Integer status;

}
