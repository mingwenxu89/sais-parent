package cn.iocoder.yudao.framework.web.core.handler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.biz.infra.logger.ApiErrorLogCommonApi;
import cn.iocoder.yudao.framework.common.biz.infra.logger.dto.ApiErrorLogCreateReqDTO;
import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.collection.SetUtils;
import cn.iocoder.yudao.framework.common.util.json.JsonUtils;
import cn.iocoder.yudao.framework.common.util.monitor.TracerUtils;
import cn.iocoder.yudao.framework.common.util.servlet.ServletUtils;
import cn.iocoder.yudao.framework.web.core.util.WebFrameworkUtils;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.google.common.util.concurrent.UncheckedExecutionException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.util.Assert;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static cn.iocoder.yudao.framework.common.exception.enums.GlobalErrorCodeConstants.*;

/**
 * Global exception handler that translates Exceptions into CommonResult with the corresponding error code
 *
 * @author yudao source code
 */
@RestControllerAdvice
@AllArgsConstructor
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Ignored ServiceException error messages to avoid excessive logger output
     */
    public static final Set<String> IGNORE_ERROR_MESSAGES = SetUtils.asSet("Invalid refresh token");

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private final String applicationName;

    private final ApiErrorLogCommonApi apiErrorLogApi;

    /**
     * Handle all exceptions, mainly for use by Filters.
     * Since Filters do not go through the SpringMVC flow, but we still need a fallback for exception handling,
     * a comprehensive exception handling process is provided here to maintain consistent logic.
     *
     * @param request the request
     * @param ex the exception
     * @return common result
     */
    public CommonResult<?> allExceptionHandler(HttpServletRequest request, Throwable ex) {
        if (ex instanceof MissingServletRequestParameterException) {
            return missingServletRequestParameterExceptionHandler((MissingServletRequestParameterException) ex);
        }
        if (ex instanceof MethodArgumentTypeMismatchException) {
            return methodArgumentTypeMismatchExceptionHandler((MethodArgumentTypeMismatchException) ex);
        }
        if (ex instanceof MethodArgumentNotValidException) {
            return methodArgumentNotValidExceptionExceptionHandler((MethodArgumentNotValidException) ex);
        }
        if (ex instanceof BindException) {
            return bindExceptionHandler((BindException) ex);
        }
        if (ex instanceof ConstraintViolationException) {
            return constraintViolationExceptionHandler((ConstraintViolationException) ex);
        }
        if (ex instanceof ValidationException) {
            return validationException((ValidationException) ex);
        }
        if (ex instanceof MaxUploadSizeExceededException) {
            return maxUploadSizeExceededExceptionHandler((MaxUploadSizeExceededException) ex);
        }
        if (ex instanceof NoHandlerFoundException) {
            return noHandlerFoundExceptionHandler((NoHandlerFoundException) ex);
        }
        if (ex instanceof HttpRequestMethodNotSupportedException) {
            return httpRequestMethodNotSupportedExceptionHandler((HttpRequestMethodNotSupportedException) ex);
        }
        if (ex instanceof HttpMediaTypeNotSupportedException) {
            return httpMediaTypeNotSupportedExceptionHandler((HttpMediaTypeNotSupportedException) ex);
        }
        if (ex instanceof ServiceException) {
            return serviceExceptionHandler((ServiceException) ex);
        }
        if (ex instanceof AccessDeniedException) {
            return accessDeniedExceptionHandler(request, (AccessDeniedException) ex);
        }
        return defaultExceptionHandler(request, ex);
    }

    /**
     * Handle missing SpringMVC request parameters
     *
     * For example, the interface has a @RequestParam("xx") parameter defined, but the xx parameter is not passed
     */
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public CommonResult<?> missingServletRequestParameterExceptionHandler(MissingServletRequestParameterException ex) {
        log.warn("[missingServletRequestParameterExceptionHandler]", ex);
        return CommonResult.error(BAD_REQUEST.getCode(), String.format("Missing request parameter: %s", ex.getParameterName()));
    }

    /**
     * Handle SpringMVC request parameter type errors
     *
     * For example, the interface has a @RequestParam("xx") parameter defined as Integer, but the passed xx parameter type is String
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public CommonResult<?> methodArgumentTypeMismatchExceptionHandler(MethodArgumentTypeMismatchException ex) {
        log.warn("[methodArgumentTypeMismatchExceptionHandler]", ex);
        return CommonResult.error(BAD_REQUEST.getCode(), String.format("Request parameter type error: %s", ex.getMessage()));
    }

    /**
     * Handle SpringMVC parameter validation failures
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CommonResult<?> methodArgumentNotValidExceptionExceptionHandler(MethodArgumentNotValidException ex) {
        log.warn("[methodArgumentNotValidExceptionExceptionHandler]", ex);
        // get errorMessage
        String errorMessage = null;
        FieldError fieldError = ex.getBindingResult().getFieldError();
        if (fieldError == null) {
            // combined validation, see https://t.zsxq.com/3HVTx
            List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();
            if (CollUtil.isNotEmpty(allErrors)) {
                errorMessage = allErrors.get(0).getDefaultMessage();
            }
        } else {
            errorMessage = fieldError.getDefaultMessage();
        }
        // convert to CommonResult
        if (StrUtil.isEmpty(errorMessage)) {
            return CommonResult.error(BAD_REQUEST);
        }
        return CommonResult.error(BAD_REQUEST.getCode(), String.format("Invalid request parameter: %s", errorMessage));
    }

    /**
     * Handle SpringMVC parameter binding failures, which are also validated through Validator
     */
    @ExceptionHandler(BindException.class)
    public CommonResult<?> bindExceptionHandler(BindException ex) {
        log.warn("[handleBindException]", ex);
        FieldError fieldError = ex.getFieldError();
        assert fieldError != null; // assertion to avoid warnings
        return CommonResult.error(BAD_REQUEST.getCode(), String.format("Invalid request parameter: %s", fieldError.getDefaultMessage()));
    }

    /**
     * Handle SpringMVC request parameter type errors
     *
     * For example, the @RequestBody entity has an xx field of type Integer, but the passed xx parameter type is String
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @SuppressWarnings("PatternVariableCanBeUsed")
    public CommonResult<?> methodArgumentTypeInvalidFormatExceptionHandler(HttpMessageNotReadableException ex) {
        log.warn("[methodArgumentTypeInvalidFormatExceptionHandler]", ex);
        if (ex.getCause() instanceof InvalidFormatException) {
            InvalidFormatException invalidFormatException = (InvalidFormatException) ex.getCause();
            return CommonResult.error(BAD_REQUEST.getCode(), String.format("Request parameter type error: %s", invalidFormatException.getValue()));
        }
        if (StrUtil.startWith(ex.getMessage(), "Required request body is missing")) {
            return CommonResult.error(BAD_REQUEST.getCode(), "Request parameter type error: request body is missing");
        }
        return defaultExceptionHandler(ServletUtils.getRequest(), ex);
    }

    /**
     * Handle exceptions caused by Validator validation failures
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    public CommonResult<?> constraintViolationExceptionHandler(ConstraintViolationException ex) {
        log.warn("[constraintViolationExceptionHandler]", ex);
        ConstraintViolation<?> constraintViolation = ex.getConstraintViolations().iterator().next();
        return CommonResult.error(BAD_REQUEST.getCode(), String.format("Invalid request parameter: %s", constraintViolation.getMessage()));
    }

    /**
     * Handle ValidationException thrown during local parameter validation by Dubbo Consumer
     */
    @ExceptionHandler(value = ValidationException.class)
    public CommonResult<?> validationException(ValidationException ex) {
        log.warn("[constraintViolationExceptionHandler]", ex);
        // Cannot concatenate detailed error information because Dubbo Consumer throws ValidationException with a plain string that is not human-readable
        return CommonResult.error(BAD_REQUEST);
    }

    /**
     * Handle file upload size exceeded exception
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public CommonResult<?> maxUploadSizeExceededExceptionHandler(MaxUploadSizeExceededException ex) {
        return CommonResult.error(BAD_REQUEST.getCode(), "Uploaded file is too large, please adjust and try again");
    }

    /**
     * Handle SpringMVC request path not found
     *
     * Note: the following two configuration items must be set:
     * 1. spring.mvc.throw-exception-if-no-handler-found set to true
     * 2. spring.mvc.static-path-pattern set to /statics/**
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public CommonResult<?> noHandlerFoundExceptionHandler(NoHandlerFoundException ex) {
        log.warn("[noHandlerFoundExceptionHandler]", ex);
        return CommonResult.error(NOT_FOUND.getCode(), String.format("Request path not found: %s", ex.getRequestURL()));
    }

    /**
     * Handle incorrect SpringMVC request method
     *
     * For example, interface A is a GET method, but the request is made with POST, resulting in a mismatch
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public CommonResult<?> httpRequestMethodNotSupportedExceptionHandler(HttpRequestMethodNotSupportedException ex) {
        log.warn("[httpRequestMethodNotSupportedExceptionHandler]", ex);
        return CommonResult.error(METHOD_NOT_ALLOWED.getCode(), String.format("Incorrect request method: %s", ex.getMessage()));
    }

    /**
     * Handle incorrect SpringMVC request Content-Type
     *
     * For example, interface A expects Content-Type of application/json, but the request uses application/octet-stream, causing a mismatch
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public CommonResult<?> httpMediaTypeNotSupportedExceptionHandler(HttpMediaTypeNotSupportedException ex) {
        log.warn("[httpMediaTypeNotSupportedExceptionHandler]", ex);
        return CommonResult.error(BAD_REQUEST.getCode(), String.format("Incorrect request type: %s", ex.getMessage()));
    }

    /**
     * Handle Spring Security insufficient privileges exception
     *
     * Originating from the @PreAuthorize annotation, where AOP performs permission interception
     */
    @ExceptionHandler(value = AccessDeniedException.class)
    public CommonResult<?> accessDeniedExceptionHandler(HttpServletRequest req, AccessDeniedException ex) {
        log.warn("[accessDeniedExceptionHandler][userId({}) cannot access url({})]", WebFrameworkUtils.getLoginUserId(req),
                req.getRequestURL(), ex);
        return CommonResult.error(FORBIDDEN);
    }

    /**
     * Handle Guava UncheckedExecutionException
     *
     * For example, cache loading errors, see <a href="https://t.zsxq.com/UszdH">https://t.zsxq.com/UszdH</a>
     */
    @ExceptionHandler(value = UncheckedExecutionException.class)
    public CommonResult<?> uncheckedExecutionExceptionHandler(HttpServletRequest req, UncheckedExecutionException ex) {
        return allExceptionHandler(req, ex.getCause());
    }

    /**
     * Handle business exception ServiceException
     *
     * For example, insufficient product inventory, user phone number already exists.
     */
    @ExceptionHandler(value = ServiceException.class)
    public CommonResult<?> serviceExceptionHandler(ServiceException ex) {
        // only log when not in the ignore list, to avoid excessive ex stack traces
        if (!IGNORE_ERROR_MESSAGES.contains(ex.getMessage())) {
            // even when logging, only log the first StackTraceElement, and use warn level in console output for better visibility
            try {
                StackTraceElement[] stackTraces = ex.getStackTrace();
                for (StackTraceElement stackTrace : stackTraces) {
                    if (ObjUtil.notEqual(stackTrace.getClassName(), ServiceExceptionUtil.class.getName())) {
                        log.warn("[serviceExceptionHandler]\n\t{}", stackTrace);
                        break;
                    }
                }
            } catch (Exception ignored) {
                // ignore logging to avoid affecting the main flow
            }
        }
        return CommonResult.error(ex.getCode(), ex.getMessage());
    }

    /**
     * Handle system exceptions, as the last fallback for all exceptions
     */
    @ExceptionHandler(value = Exception.class)
    public CommonResult<?> defaultExceptionHandler(HttpServletRequest req, Throwable ex) {
        // Special case: if the exception is a ServiceException, return directly
        // e.g.: https://gitee.com/zhijiantianya/sar-cloud/issues/ICSSRM, https://gitee.com/zhijiantianya/sar-cloud/issues/ICT6FM
        if (ex.getCause() != null && ex.getCause() instanceof ServiceException) {
            return serviceExceptionHandler((ServiceException) ex.getCause());
        }

        // Case 1: handle table-does-not-exist exception
        CommonResult<?> tableNotExistsResult = handleTableNotExists(ex);
        if (tableNotExistsResult != null) {
            return tableNotExistsResult;
        }

        // Case 2: handle exception
        log.error("[defaultExceptionHandler]", ex);
        // insert exception log
        createExceptionLog(req, ex);
        // return ERROR CommonResult
        return CommonResult.error(INTERNAL_SERVER_ERROR.getCode(), INTERNAL_SERVER_ERROR.getMsg());
    }

    private void createExceptionLog(HttpServletRequest req, Throwable e) {
        // insert error log
        ApiErrorLogCreateReqDTO errorLog = new ApiErrorLogCreateReqDTO();
        try {
            // initialize errorLog
            buildExceptionLog(errorLog, req, e);
            // execute insert errorLog
            apiErrorLogApi.createApiErrorLogAsync(errorLog);
        } catch (Throwable th) {
            log.error("[createExceptionLog][url({}) log({}) an exception occurred]", req.getRequestURI(),  JsonUtils.toJsonString(errorLog), th);
        }
    }

    private void buildExceptionLog(ApiErrorLogCreateReqDTO errorLog, HttpServletRequest request, Throwable e) {
        // process user information
        errorLog.setUserId(WebFrameworkUtils.getLoginUserId(request));
        errorLog.setUserType(WebFrameworkUtils.getLoginUserType(request));
        // set exception fields
        errorLog.setExceptionName(e.getClass().getName());
        errorLog.setExceptionMessage(ExceptionUtil.getMessage(e));
        errorLog.setExceptionRootCauseMessage(ExceptionUtil.getRootCauseMessage(e));
        errorLog.setExceptionStackTrace(ExceptionUtil.stacktraceToString(e));
        StackTraceElement[] stackTraceElements = e.getStackTrace();
        Assert.notEmpty(stackTraceElements, "Exception stackTraceElements cannot be empty");
        StackTraceElement stackTraceElement = stackTraceElements[0];
        errorLog.setExceptionClassName(stackTraceElement.getClassName());
        errorLog.setExceptionFileName(stackTraceElement.getFileName());
        errorLog.setExceptionMethodName(stackTraceElement.getMethodName());
        errorLog.setExceptionLineNumber(stackTraceElement.getLineNumber());
        // set other fields
        errorLog.setTraceId(TracerUtils.getTraceId());
        errorLog.setApplicationName(applicationName);
        errorLog.setRequestUrl(request.getRequestURI());
        Map<String, Object> requestParams = MapUtil.<String, Object>builder()
                .put("query", ServletUtils.getParamMap(request))
                .put("body", ServletUtils.getBody(request)).build();
        errorLog.setRequestParams(JsonUtils.toJsonString(requestParams));
        errorLog.setRequestMethod(request.getMethod());
        errorLog.setUserAgent(ServletUtils.getUserAgent(request));
        errorLog.setUserIp(ServletUtils.getClientIP(request));
        errorLog.setExceptionTime(LocalDateTime.now());
    }

    /**
     * Handle the case where a Table does not exist
     *
     * @param ex the exception
     * @return if the exception is about a Table not existing, return the corresponding CommonResult
     */
    private CommonResult<?> handleTableNotExists(Throwable ex) {
        String message = ExceptionUtil.getRootCauseMessage(ex);
        if (!message.contains("doesn't exist")) {
            return null;
        }
        // 1. Report module
        if (message.contains("report_")) {
            log.error("[Report module sar-module-report - table structure not imported][See https://cloud.iocoder.cn/report/ to enable]");
            return CommonResult.error(NOT_IMPLEMENTED.getCode(),
                    "[Report module sar-module-report - table structure not imported][See https://cloud.iocoder.cn/report/ to enable]");
        }
        // 2. Workflow
        if (message.contains("bpm_")) {
            log.error("[Workflow module sar-module-bpm - table structure not imported][See https://cloud.iocoder.cn/bpm/ to enable]");
            return CommonResult.error(NOT_IMPLEMENTED.getCode(),
                    "[Workflow module sar-module-bpm - table structure not imported][See https://cloud.iocoder.cn/bpm/ to enable]");
        }
        // 3. WeChat Official Account
        if (message.contains("mp_")) {
            log.error("[WeChat Official Account sar-module-mp - table structure not imported][See https://cloud.iocoder.cn/mp/build/ to enable]");
            return CommonResult.error(NOT_IMPLEMENTED.getCode(),
                    "[WeChat Official Account sar-module-mp - table structure not imported][See https://cloud.iocoder.cn/mp/build/ to enable]");
        }
        // 4. Mall system
        if (StrUtil.containsAny(message, "product_", "promotion_", "trade_")) {
            log.error("[Mall system sar-module-mall - disabled][See https://cloud.iocoder.cn/mall/build/ to enable]");
            return CommonResult.error(NOT_IMPLEMENTED.getCode(),
                    "[Mall system sar-module-mall - disabled][See https://cloud.iocoder.cn/mall/build/ to enable]");
        }
        // 5. ERP system
        if (message.contains("erp_")) {
            log.error("[ERP system sar-module-erp - table structure not imported][See https://cloud.iocoder.cn/erp/build/ to enable]");
            return CommonResult.error(NOT_IMPLEMENTED.getCode(),
                    "[ERP system sar-module-erp - table structure not imported][See https://cloud.iocoder.cn/erp/build/ to enable]");
        }
        // 6. CRM system
        if (message.contains("crm_")) {
            log.error("[CRM system sar-module-crm - table structure not imported][See https://cloud.iocoder.cn/crm/build/ to enable]");
            return CommonResult.error(NOT_IMPLEMENTED.getCode(),
                    "[CRM system sar-module-crm - table structure not imported][See https://cloud.iocoder.cn/crm/build/ to enable]");
        }
        // 7. Payment platform
        if (message.contains("pay_")) {
            log.error("[Payment module sar-module-pay - table structure not imported][See https://cloud.iocoder.cn/pay/build/ to enable]");
            return CommonResult.error(NOT_IMPLEMENTED.getCode(),
                    "[Payment module sar-module-pay - table structure not imported][See https://cloud.iocoder.cn/pay/build/ to enable]");
        }
        // 8. AI large model
        if (message.contains("ai_")) {
            log.error("[AI large model sar-module-ai - table structure not imported][See https://cloud.iocoder.cn/ai/build/ to enable]");
            return CommonResult.error(NOT_IMPLEMENTED.getCode(),
                    "[AI large model sar-module-ai - table structure not imported][See https://cloud.iocoder.cn/ai/build/ to enable]");
        }
        // 9. IoT (Internet of Things)
        if (message.contains("iot_")) {
            log.error("[IoT sar-module-iot - table structure not imported][See https://doc.iocoder.cn/iot/build/ to enable]");
            return CommonResult.error(NOT_IMPLEMENTED.getCode(),
                    "[IoT sar-module-iot - table structure not imported][See https://doc.iocoder.cn/iot/build/ to enable]");
        }
        return null;
    }

}
