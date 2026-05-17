package cn.iocoder.yudao.framework.common.biz.system.dict.dto;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import lombok.Data;

/**
 * Dictionary data Response DTO
 *
 * @author Yudao Source Code
 */
@Data
public class DictDataRespDTO {

 /**
     * dictionary tag
 */
 private String label;
 /**
     * Dictionary value
 */
 private String value;
 /**
     * dictionary type
 */
 private String dictType;
 /**
     * state
 *
     * Enum {@link CommonStatusEnum}
 */
 private Integer status;

}
