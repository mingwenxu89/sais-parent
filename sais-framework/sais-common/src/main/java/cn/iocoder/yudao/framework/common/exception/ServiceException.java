package cn.iocoder.yudao.framework.common.exception;

import cn.iocoder.yudao.framework.common.exception.enums.ServiceErrorCodeRange;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Business logic exception Exception
 */
@Data
@EqualsAndHashCode(callSuper = true)
public final class ServiceException extends RuntimeException {

 /**
     * Business error code
 *
 * @see ServiceErrorCodeRange
 */
 private Integer code;
 /**
     * Error message
 */
 private String message;

 /**
     * Empty constructor to avoID deserialization problems
 */
 public ServiceException() {
 }

 public ServiceException(ErrorCode errorCode) {
 this.code = errorCode.getCode();
 this.message = errorCode.getMsg();
 }

 public ServiceException(Integer code, String message) {
 this.code = code;
 this.message = message;
 }

 public Integer getCode() {
 return code;
 }

 public ServiceException setCode(Integer code) {
 this.code = code;
 return this;
 }

 @Override
 public String getMessage() {
 return message;
 }

 public ServiceException setMessage(String message) {
 this.message = message;
 return this;
 }

}
