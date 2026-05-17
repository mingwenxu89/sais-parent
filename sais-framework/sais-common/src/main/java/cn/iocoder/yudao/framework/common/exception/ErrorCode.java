package cn.iocoder.yudao.framework.common.exception;

import cn.iocoder.yudao.framework.common.exception.enums.GlobalErrorCodeConstants;
import cn.iocoder.yudao.framework.common.exception.enums.ServiceErrorCodeRange;
import lombok.Data;

/**
 * error code object
 *
 * Global error code, occupies [0, 999], see {@link GlobalErrorCodeConstants}
 * Business exception error code, occupies [1 000 000 000, +∞), see {@link ServiceErrorCodeRange}
 *
 * The reason why TODO error codes are designed as objects is to prepare for future i18 internationalization.
 */
@Data
public class ErrorCode {

 /**
     * error code
 */
 private final Integer code;
 /**
     * Error message
 */
 private final String msg;

 public ErrorCode(Integer code, String message) {
 this.code = code;
 this.msg = message;
 }

}
