package cn.iocoder.yudao.framework.apilog.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Operation type of operation log
 *
 * @author ruoyi
 */
@Getter
@AllArgsConstructor
public enum OperateTypeEnum {

 /**
     * Query
 */
 GET(1),
 /**
     * New
 */
 CREATE(2),
 /**
     * Revise
 */
 UPDATE(3),
 /**
     * delete
 */
 DELETE(4),
 /**
     * Export
 */
 EXPORT(5),
 /**
     * import
 */
 IMPORT(6),
 /**
     * other
 *
     * When it cannot be classified, you can choose to use others. Because there are also operation names that can be further identified
 */
 OTHER(0);

 /**
     * type
 */
 private final Integer type;

}
