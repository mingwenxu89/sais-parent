package cn.iocoder.yudao.framework.apilog.core.filter;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum;
import cn.iocoder.yudao.framework.common.biz.infra.logger.ApiAccessLogCommonApi;
import cn.iocoder.yudao.framework.common.biz.infra.logger.dto.ApiAccessLogCreateReqDTO;
import cn.iocoder.yudao.framework.common.exception.enums.GlobalErrorCodeConstants;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.json.JsonUtils;
import cn.iocoder.yudao.framework.common.util.monitor.TracerUtils;
import cn.iocoder.yudao.framework.common.util.servlet.ServletUtils;
import cn.iocoder.yudao.framework.web.config.WebProperties;
import cn.iocoder.yudao.framework.web.core.filter.ApiRequestFilter;
import cn.iocoder.yudao.framework.web.core.util.WebFrameworkUtils;
import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;
import java.util.Map;

import static cn.iocoder.yudao.framework.apilog.core.interceptor.ApiAccessLogInterceptor.ATTRIBUTE_HANDLER_METHOD;
import static cn.iocoder.yudao.framework.common.util.json.JsonUtils.toJsonString;

/**
 * API access log Filter
 *
 * Purpose: Record API access logs to the database
 *
 * @author Yudao Source Code
 */
@Slf4j
public class ApiAccessLogFilter extends ApiRequestFilter {

    private static final String[] SANITIZE_KEYS = new String[]{"password", "token", "accessToken", "refreshToken"};

    private final String applicationName;

    private final ApiAccessLogCommonApi apiAccessLogApi;

    public ApiAccessLogFilter(WebProperties webProperties, String applicationName, ApiAccessLogCommonApi apiAccessLogApi) {
        super(webProperties);
        this.applicationName = applicationName;
        this.apiAccessLogApi = apiAccessLogApi;
    }

    @Override
    @SuppressWarnings("NullableProblems")
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // Get start time
        LocalDateTime beginTime = LocalDateTime.now();
        // Obtain parameters in advance to avoid XssFilter filtering processing
        Map<String, String> queryString = ServletUtils.getParamMap(request);
        String requestBody = ServletUtils.isJsonRequest(request) ? ServletUtils.getBody(request) : null;

        try {
            // continue filter
            filterChain.doFilter(request, response);
            // Execute normally and record logs
            createApiAccessLog(request, beginTime, queryString, requestBody, null);
        } catch (Exception ex) {
            // Abnormal execution and logging
            createApiAccessLog(request, beginTime, queryString, requestBody, ex);
            throw ex;
        }
    }

    private void createApiAccessLog(HttpServletRequest request, LocalDateTime beginTime,
                                    Map<String, String> queryString, String requestBody, Exception ex) {
        ApiAccessLogCreateReqDTO accessLog = new ApiAccessLogCreateReqDTO();
        try {
            boolean enable = buildApiAccessLog(accessLog, request, beginTime, queryString, requestBody, ex);
            if (!enable) {
                return;
            }
            apiAccessLogApi.createApiAccessLogAsync(accessLog);
        } catch (Throwable th) {
            log.error("[createApiAccessLog][URL({}) log({}) exception occurred]", request.getRequestURI(), toJsonString(accessLog), th);
        }
    }

    private boolean buildApiAccessLog(ApiAccessLogCreateReqDTO accessLog, HttpServletRequest request, LocalDateTime beginTime,
                                      Map<String, String> queryString, String requestBody, Exception ex) {
        // Judgment: Whether to record operation logs
        HandlerMethod handlerMethod = (HandlerMethod) request.getAttribute(ATTRIBUTE_HANDLER_METHOD);
        ApiAccessLog accessLogAnnotation = null;
        if (handlerMethod != null) {
            accessLogAnnotation = handlerMethod.getMethodAnnotation(ApiAccessLog.class);
            if (accessLogAnnotation != null && BooleanUtil.isFalse(accessLogAnnotation.enable())) {
                return false;
            }
        }

        // Process user information
        accessLog.setUserId(WebFrameworkUtils.getLoginUserId(request))
                .setUserType(WebFrameworkUtils.getLoginUserType(request));
        // Set access results
        CommonResult<?> result = WebFrameworkUtils.getCommonResult(request);
        if (result != null) {
            accessLog.setResultCode(result.getCode()).setResultMsg(result.getMsg());
        } else if (ex != null) {
            accessLog.setResultCode(GlobalErrorCodeConstants.INTERNAL_SERVER_ERROR.getCode())
                    .setResultMsg(ExceptionUtil.getRootCauseMessage(ex));
        } else {
            accessLog.setResultCode(GlobalErrorCodeConstants.SUCCESS.getCode()).setResultMsg("");
        }
        // Set request fields
        accessLog.setTraceId(TracerUtils.getTraceId()).setApplicationName(applicationName)
                .setRequestUrl(request.getRequestURI()).setRequestMethod(request.getMethod())
                .setUserAgent(ServletUtils.getUserAgent(request)).setUserIp(ServletUtils.getClientIP(request));
        String[] sanitizeKeys = accessLogAnnotation != null ? accessLogAnnotation.sanitizeKeys() : null;
        Boolean requestEnable = accessLogAnnotation != null ? accessLogAnnotation.requestEnable() : Boolean.TRUE;
        if (!BooleanUtil.isFalse(requestEnable)) { // Recorded by default, so judge !false
            Map<String, Object> requestParams = MapUtil.<String, Object>builder()
                    .put("query", sanitizeMap(queryString, sanitizeKeys))
                    .put("body", sanitizeJson(requestBody, sanitizeKeys)).build();
            accessLog.setRequestParams(toJsonString(requestParams));
        }
        Boolean responseEnable = accessLogAnnotation != null ? accessLogAnnotation.responseEnable() : Boolean.FALSE;
        if (BooleanUtil.isTrue(responseEnable)) { // Not logged by default, forced to true by default
            accessLog.setResponseBody(sanitizeJson(result, sanitizeKeys));
        }
        // duration
        accessLog.setBeginTime(beginTime).setEndTime(LocalDateTime.now())
                .setDuration((int) LocalDateTimeUtil.between(accessLog.getBeginTime(), accessLog.getEndTime(), ChronoUnit.MILLIS));

        // Operation module
        if (handlerMethod != null) {
            Tag tagAnnotation = handlerMethod.getBeanType().getAnnotation(Tag.class);
            Operation operationAnnotation = handlerMethod.getMethodAnnotation(Operation.class);
            String operateModule = accessLogAnnotation != null && StrUtil.isNotBlank(accessLogAnnotation.operateModule()) ?
                    accessLogAnnotation.operateModule() :
                    tagAnnotation != null ? StrUtil.nullToDefault(tagAnnotation.name(), tagAnnotation.description()) : null;
            String operateName = accessLogAnnotation != null && StrUtil.isNotBlank(accessLogAnnotation.operateName()) ?
                    accessLogAnnotation.operateName() :
                    operationAnnotation != null ? operationAnnotation.summary() : null;
            OperateTypeEnum operateType = accessLogAnnotation != null && accessLogAnnotation.operateType().length > 0 ?
                    accessLogAnnotation.operateType()[0] : parseOperateLogType(request);
            accessLog.setOperateModule(operateModule).setOperateName(operateName).setOperateType(operateType.getType());
        }
        return true;
    }

    // ========== Parsing @ApiAccessLog, @Swagger annotations ==========

    private static OperateTypeEnum parseOperateLogType(HttpServletRequest request) {
        RequestMethod requestMethod = RequestMethod.resolve(request.getMethod());
        if (requestMethod == null) {
            return OperateTypeEnum.OTHER;
        }
        switch (requestMethod) {
            case GET:
                return OperateTypeEnum.GET;
            case POST:
                return OperateTypeEnum.CREATE;
            case PUT:
                return OperateTypeEnum.UPDATE;
            case DELETE:
                return OperateTypeEnum.DELETE;
            default:
                return OperateTypeEnum.OTHER;
        }
    }

    // ========== Desensitization logic for requests and responses, removing sensitive fields such as password and token ==========

    private static String sanitizeMap(Map<String, ?> map, String[] sanitizeKeys) {
        if (CollUtil.isEmpty(map)) {
            return null;
        }
        if (sanitizeKeys != null) {
            MapUtil.removeAny(map, sanitizeKeys);
        }
        MapUtil.removeAny(map, SANITIZE_KEYS);
        return JsonUtils.toJsonString(map);
    }

    private static String sanitizeJson(String jsonString, String[] sanitizeKeys) {
        if (StrUtil.isEmpty(jsonString)) {
            return null;
        }
        try {
            JsonNode rootNode = JsonUtils.parseTree(jsonString);
            sanitizeJson(rootNode, sanitizeKeys);
            return JsonUtils.toJsonString(rootNode);
        } catch (Exception e) {
            // If desensitization fails, ignore the exception directly to avoid affecting user requests.
            log.error("[sanitizeJson][An exception occurred in desensitization ({})]", jsonString, e);
            return jsonString;
        }
    }

    private static String sanitizeJson(CommonResult<?> commonResult, String[] sanitizeKeys) {
        if (commonResult == null) {
            return null;
        }
        String jsonString = toJsonString(commonResult);
        try {
            JsonNode rootNode = JsonUtils.parseTree(jsonString);
            sanitizeJson(rootNode.get("data"), sanitizeKeys); // Only the data field is processed, and the code and msg fields are not processed to avoid errors being desensitized.
            return JsonUtils.toJsonString(rootNode);
        } catch (Exception e) {
            // If desensitization fails, ignore the exception directly to avoid affecting user requests.
            log.error("[sanitizeJson][An exception occurred in desensitization ({})]", jsonString, e);
            return jsonString;
        }
    }

    private static void sanitizeJson(JsonNode node, String[] sanitizeKeys) {
        // Case 1: Array, traversal processing
        if (node.isArray()) {
            for (JsonNode childNode : node) {
                sanitizeJson(childNode, sanitizeKeys);
            }
            return;
        }
        // Case 2: Non-Object, just a certain value, returned directly
        if (!node.isObject()) {
            return;
        }
        // Case 3: Object, traversal processing
        Iterator<Map.Entry<String, JsonNode>> iterator = node.properties().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, JsonNode> entry = iterator.next();
            if (ArrayUtil.contains(sanitizeKeys, entry.getKey())
                || ArrayUtil.contains(SANITIZE_KEYS, entry.getKey())) {
                iterator.remove();
                continue;
            }
            sanitizeJson(entry.getValue(), sanitizeKeys);
        }
    }

}
