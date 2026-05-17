package cn.iocoder.yudao.module.system.api.logger.dto;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import lombok.Data;

/**
 * Operation log paging Request DTO
 *
 * @author HUIHUI
 */
@Data
public class OperateLogPageReqDTO extends PageParam {

    /**
     * module type
     */
    private String type;
    /**
     * Module data ID
     */
    private Long bizId;

    /**
     * User ID
     */
    private Long userId;

}
