package cn.iocoder.yudao.framework.common.pojo;

import cn.hutool.core.lang.Assert;
import cn.iocoder.yudao.framework.common.exception.ErrorCode;
import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.exception.enums.GlobalErrorCodeConstants;
import cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

/**
 * Generic return
 *
 * @param <T> Data generics
 */
@Data
public class CommonResult<T> implements Serializable {

 /**
     * error code
 *
 * @see ErrorCode#getCode()
 */
 private Integer code;
 /**
     * Error message, user can read
 *
 * @see ErrorCode#getMsg() ()
 */
 private String msg;
 /**
     * Return data
 */
 private T data;

 /**
     * Convert the incoming result object into another generic result object
 *
     * Because the CommonResult object returned by method A does not satisfy the return of method B that calls it, conversion needs to be performed.
 *
     * @param result The result object passed in
     * @param <T> Returned generic
     * @return new CommonResult object
 */
 public static <T> CommonResult<T> error(CommonResult<?> result) {
 return error(result.getCode(), result.getMsg());
 }

 public static <T> CommonResult<T> error(Integer code, String message) {
        Assert.notEquals(GlobalErrorCodeConstants.SUCCESS.getCode(), code, "code must be wrong!");
 CommonResult<T> result = new CommonResult<>();
 result.code = code;
 result.msg = message;
 return result;
 }

 public static <T> CommonResult<T> error(ErrorCode errorCode, Object... params) {
        Assert.notEquals(GlobalErrorCodeConstants.SUCCESS.getCode(), errorCode.getCode(), "code must be wrong!");
 CommonResult<T> result = new CommonResult<>();
 result.code = errorCode.getCode();
 result.msg = ServiceExceptionUtil.doFormat(errorCode.getCode(), errorCode.getMsg(), params);
 return result;
 }

 public static <T> CommonResult<T> error(ErrorCode errorCode) {
 return error(errorCode.getCode(), errorCode.getMsg());
 }

 public static <T> CommonResult<T> success(T data) {
 CommonResult<T> result = new CommonResult<>();
 result.code = GlobalErrorCodeConstants.SUCCESS.getCode();
 result.data = data;
 result.msg = "";
 return result;
 }

 public static boolean isSuccess(Integer code) {
 return Objects.equals(code, GlobalErrorCodeConstants.SUCCESS.getCode());
 }

    @JsonIgnore // AvoID jackson serialization
 public boolean isSuccess() {
 return isSuccess(code);
 }

    @JsonIgnore // AvoID jackson serialization
 public boolean isError() {
 return !isSuccess();
 }

    // ========= Integrated with Exception exception system =========

 /**
     * Determine whether there is any abnormality. If so, throw {@link ServiceException} exception
 */
 public void checkError() throws ServiceException {
 if (isSuccess()) {
 return;
 }
        // Business abnormality
 throw new ServiceException(code, msg);
 }

 /**
     * Determine whether there is any abnormality. If so, throw {@link ServiceException} exception
     * If not, return {@link #data} data
 */
    @JsonIgnore // AvoID jackson serialization
 public T getCheckedData() {
 checkError();
 return data;
 }

 public static <T> CommonResult<T> error(ServiceException serviceException) {
 return error(serviceException.getCode(), serviceException.getMessage());
 }

}
