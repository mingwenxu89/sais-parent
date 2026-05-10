package cn.iocoder.yudao.framework.common.util.servlet;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.util.json.JsonUtils;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 客户端工具类
 *
 * @author 芋道源码
 */
public class ServletUtils {

    /**
     * 返回 JSON 字符串
     *
     * @param response 响应
     * @param object   对象，会序列化成 JSON 字符串
     */
    @SuppressWarnings("deprecation") // 必须使用 APPLICATION_JSON_UTF8_VALUE，否则会乱码
    public static void writeJSON(HttpServletResponse response, Object object) {
        String content = JsonUtils.toJsonString(object);
        try {
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 返回附件
     *
     * @param response 响应
     * @param filename 文件名
     * @param content  附件内容
     */
    public static void writeAttachment(HttpServletResponse response, String filename, byte[] content) throws IOException {
        // 设置 header 和 contentType
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        // 输出附件
        IoUtil.write(response.getOutputStream(), false, content);
    }

    /**
     * @param request 请求
     * @return ua
     */
    public static String getUserAgent(HttpServletRequest request) {
        String ua = request.getHeader("User-Agent");
        return ua != null ? ua : "";
    }

    /**
     * 获得请求
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
        return getClientIP(request);
    }

    public static boolean isJsonRequest(ServletRequest request) {
        return StrUtil.startWithIgnoreCase(request.getContentType(), MediaType.APPLICATION_JSON_VALUE);
    }

    public static String getBody(HttpServletRequest request) {
        // 只有在 json 请求在读取，因为只有 CacheRequestBodyFilter 才会进行缓存，支持重复读取
        if (isJsonRequest(request)) {
            try {
                return IoUtil.read(request.getReader());
            } catch (IOException e) {
                return null;
            }
        }
        return null;
    }

    public static byte[] getBodyBytes(HttpServletRequest request) {
        // 只有在 json 请求在读取，因为只有 CacheRequestBodyFilter 才会进行缓存，支持重复读取
        if (isJsonRequest(request)) {
            try {
                return IoUtil.readBytes(request.getInputStream());
            } catch (IOException e) {
                return null;
            }
        }
        return null;
    }

    private static final String[] CLIENT_IP_HEADERS = {
        "X-Forwarded-For", "X-Real-IP", "Proxy-Client-IP",
        "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR"
    };

    public static String getClientIP(HttpServletRequest request) {
        for (String header : CLIENT_IP_HEADERS) {
            String ip = request.getHeader(header);
            if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
                return ip.contains(",") ? ip.split(",")[0].trim() : ip.trim();
            }
        }
        return request.getRemoteAddr();
    }

    public static Map<String, String> getParamMap(HttpServletRequest request) {
        Map<String, String> result = new LinkedHashMap<>();
        request.getParameterMap().forEach((name, values) ->
            result.put(name, values.length > 0 ? StrUtil.join(",", (Object[]) values) : ""));
        return result;
    }

    public static Map<String, String> getHeaderMap(HttpServletRequest request) {
        Map<String, String> result = new LinkedHashMap<>();
        Enumeration<String> names = request.getHeaderNames();
        while (names != null && names.hasMoreElements()) {
            String name = names.nextElement();
            result.put(name, request.getHeader(name));
        }
        return result;
    }

}
