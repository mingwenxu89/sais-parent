package cn.iocoder.yudao.framework.common.util.servlet;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import cn.iocoder.yudao.framework.common.util.json.JsonUtils;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Map;

/**
 * Client tools
 *
 * @author Yudao Source Code
 */
public class ServletUtils {

    /**
     * Return JSON string
     *
     * @param response response
     * @param object Object, which will be serialized into a JSON string
     */
    @SuppressWarnings("deprecation") // APPLICATION_JSON_UTF8_VALUE must be used, otherwise it will be garbled
    public static void writeJSON(HttpServletResponse response, Object object) {
        String content = JsonUtils.toJsonString(object);
        JakartaServletUtil.write(response, content, MediaType.APPLICATION_JSON_UTF8_VALUE);
    }

    /**
     * @param request Request
     * @return ua
     */
    public static String getUserAgent(HttpServletRequest request) {
        String ua = request.getHeader("User-Agent");
        return ua != null ? ua : "";
    }

    /**
     * get request
     *
     * @return HttpServletRequest
     */
    public static HttpServletRequest getRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (!(requestAttributes instanceof ServletRequestAttributes)) {
            return null;
        }
        return ((ServletRequestAttributes) requestAttributes).getRequest();
    }

    public static String getUserAgent() {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return null;
        }
        return getUserAgent(request);
    }

    public static String getClientIP() {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return null;
        }
        return JakartaServletUtil.getClientIP(request);
    }

    public static boolean isJsonRequest(ServletRequest request) {
        return StrUtil.startWithIgnoreCase(request.getContentType(), MediaType.APPLICATION_JSON_VALUE);
    }

    public static String getBody(HttpServletRequest request) {
        // Only the JSON request is being read, because only CacheRequestBodyFilter will cache and support repeated reading.
        if (isJsonRequest(request)) {
            return JakartaServletUtil.getBody(request);
        }
        return null;
    }

    public static byte[] getBodyBytes(HttpServletRequest request) {
        // Only the JSON request is being read, because only CacheRequestBodyFilter will cache and support repeated reading.
        if (isJsonRequest(request)) {
            return JakartaServletUtil.getBodyBytes(request);
        }
        return null;
    }

    public static String getClientIP(HttpServletRequest request) {
        return JakartaServletUtil.getClientIP(request);
    }

    public static Map<String, String> getParamMap(HttpServletRequest request) {
        return JakartaServletUtil.getParamMap(request);
    }

    public static Map<String, String> getHeaderMap(HttpServletRequest request) {
        return JakartaServletUtil.getHeaderMap(request);
    }

}
