package cn.iocoder.yudao.module.system.api.dept.dto;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import lombok.Data;

/**
 * Position Response DTO
 *
 * @author Yudao Source Code
 */
@Data
public class PostRespDTO {

    /**
     * Position serial ID
     */
    private Long id;
    /**
     * Job title
     */
    private String name;
    /**
     * Position code
     */
    private String code;
    /**
     * Position sorting
     */
    private Integer sort;
    /**
     * Status
     *
     * Enum {@link CommonStatusEnum}
     */
    private Integer status;

}
