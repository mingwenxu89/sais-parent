package cn.iocoder.yudao.framework.common.exception.util;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;
import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.exception.enums.GlobalErrorCodeConstants;
import com.google.common.annotations.VisibleForTesting;
import lombok.extern.slf4j.Slf4j;

/**
 * {@link ServiceException} Utility class
 *
 * The purpose is to format exception information prompts.
 * Considering that String.format will report an error when the parameters are incorrect, use {} as a placeholder and use the {@link #doFormat(int, String, Object...)} method to format
 *
 */
@Slf4j
public class ServiceExceptionUtil {

    // ========== Integration with ServiceException ==========

 public static ServiceException exception(ErrorCode errorCode) {
 return exception0(errorCode.getCode(), errorCode.getMsg());
 }

 public static ServiceException exception(ErrorCode errorCode, Object... params) {
 return exception0(errorCode.getCode(), errorCode.getMsg(), params);
 }

 public static ServiceException exception0(Integer code, String messagePattern, Object... params) {
 String message = doFormat(code, messagePattern, params);
 return new ServiceException(code, message);
 }

 public static ServiceException invalidParamException(String messagePattern, Object... params) {
 return exception0(GlobalErrorCodeConstants.BAD_REQUEST.getCode(), messagePattern, params);
 }

    // ========== Formatting methods ==========

 /**
     * Format the message corresponding to the error number using params.
 *
     * @param code error number
     * @param messagePattern Message template
     * @param params parameter
     * @return Prompt after formatting
 */
 @VisibleForTesting
 public static String doFormat(int code, String messagePattern, Object... params) {
 StringBuilder sbuf = new StringBuilder(messagePattern.length() + 50);
 int i = 0;
 int j;
 int l;
 for (l = 0; l < params.length; l++) {
 j = messagePattern.indexOf("{}", i);
 if (j == -1) {
                log.error("[doFormat][Too many parameters: error code ({})|error content ({})|parameters ({})", code, messagePattern, params);
 if (i == 0) {
 return messagePattern;
 } else {
 sbuf.append(messagePattern.substring(i));
 return sbuf.toString();
 }
 } else {
 sbuf.append(messagePattern, i, j);
 sbuf.append(params[l]);
 i = j + 2;
 }
 }
 if (messagePattern.indexOf("{}", i) != -1) {
            log.error("[doFormat][Too few parameters: error code ({})|error content ({})|parameters ({})", code, messagePattern, params);
 }
 sbuf.append(messagePattern.substring(i));
 return sbuf.toString();
 }

}
