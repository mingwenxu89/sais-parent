package cn.iocoder.yudao.framework.common.util.http;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.map.TableMap;
import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * HTTP tools
 *
 * @author Yudao Source Code
 */
public class HttpUtils {

    /**
     * Encode URL parameters
     *
     * @param value parameters
     * @return encoded parameters
     */
    public static String encodeUtf8(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    /**
     * Decode URL parameters
     *
     * @param value parameters
     * @return Decoded parameters
     */
    public static String decodeUtf8(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }

    @SuppressWarnings("unchecked")
    public static String replaceUrlQuery(String url, String key, String value) {
        UrlBuilder builder = UrlBuilder.of(url, Charset.defaultCharset());
        // Remove first
        TableMap<CharSequence, CharSequence> query = (TableMap<CharSequence, CharSequence>)
                ReflectUtil.getFieldValue(builder.getQuery(), "query");
        query.remove(key);
        // added after
        builder.addQuery(key, value);
        return builder.build();
    }

    public static String removeUrlQuery(String url) {
        if (!StrUtil.contains(url, '?')) {
            return url;
        }
        UrlBuilder builder = UrlBuilder.of(url, Charset.defaultCharset());
        // Remove query, fragment
        builder.setQuery(null);
        builder.setFragment(null);
        return builder.build();
    }

    /**
     * Splicing URLs
     *
     * copy from the append method of the AuthorizationEndpoint class of Spring Security OAuth2
     *
     * @param base Base URL
     * @param query query parameters
     * @param keys The key of query corresponds to the mapping of the original key. For example, if there is a key in the query that is xx, but its actual key is extra_xx, then add this mapping through keys.
     * @param fragment URL fragment, that is, spliced into #
     * @return Concatenated URL
     */
    public static String append(String base, Map<String, ?> query, Map<String, String> keys, boolean fragment) {
        UriComponentsBuilder template = UriComponentsBuilder.newInstance();
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(base);
        URI redirectUri;
        try {
            // assume it's encoded to start with (if it came in over the wire)
            redirectUri = builder.build(true).toUri();
        } catch (Exception e) {
            // ... but allow client registrations to contain hard-coded non-encoded values
            redirectUri = builder.build().toUri();
            builder = UriComponentsBuilder.fromUri(redirectUri);
        }
        template.scheme(redirectUri.getScheme()).port(redirectUri.getPort()).host(redirectUri.getHost())
                .userInfo(redirectUri.getUserInfo()).path(redirectUri.getPath());

        if (fragment) {
            StringBuilder values = new StringBuilder();
            if (redirectUri.getFragment() != null) {
                String append = redirectUri.getFragment();
                values.append(append);
            }
            for (String key : query.keySet()) {
                if (values.length() > 0) {
                    values.append("&");
                }
                String name = key;
                if (keys != null && keys.containsKey(key)) {
                    name = keys.get(key);
                }
                values.append(name).append("={").append(key).append("}");
            }
            if (values.length() > 0) {
                template.fragment(values.toString());
            }
            UriComponents encoded = template.build().expand(query).encode();
            builder.fragment(encoded.getFragment());
        } else {
            for (String key : query.keySet()) {
                String name = key;
                if (keys != null && keys.containsKey(key)) {
                    name = keys.get(key);
                }
                template.queryParam(name, "{" + key + "}");
            }
            template.fragment(redirectUri.getFragment());
            UriComponents encoded = template.build().expand(query).encode();
            builder.query(encoded.getQuery());
        }
        return builder.build().toUriString();
    }

    public static String[] obtainBasicAuthorization(HttpServletRequest request) {
        String clientId;
        String clientSecret;
        // Get it from Header first
        String authorization = request.getHeader("Authorization");
        authorization = StrUtil.subAfter(authorization, "Basic ", true);
        if (StringUtils.hasText(authorization)) {
            authorization = Base64.decodeStr(authorization);
            clientId = StrUtil.subBefore(authorization, ":", false);
            clientSecret = StrUtil.subAfter(authorization, ":", false);
            // Then get it from Param
        } else {
            clientId = request.getParameter("client_id");
            clientSecret = request.getParameter("client_secret");
        }

        // If both are non-empty, return
        if (StrUtil.isNotEmpty(clientId) && StrUtil.isNotEmpty(clientSecret)) {
            return new String[]{clientId, clientSecret};
        }
        return null;
    }

    /**
     * HTTP post request, implemented based on {@link cn.hutool.http.HttpUtil}
     *
     * Why should this method be encapsulated? Because the method encapsulated by HttpUtil by default does not allow passing headers parameters.
     *
     * @param url URL
     * @param headers Request Header
     * @param requestBody Request body
     * @return Request result
     */
    public static String post(String url, Map<String, String> headers, String requestBody) {
        try (HttpResponse response = HttpRequest.post(url)
                .addHeaders(headers)
                .body(requestBody)
                .execute()) {
            return response.body();
        }
    }

    /**
     * HTTP get request, implemented based on {@link cn.hutool.http.HttpUtil}
     *
     * Why should this method be encapsulated? Because the method encapsulated by HttpUtil by default does not allow passing headers parameters.
     *
     * @param url URL
     * @param headers Request Header
     * @return Request result
     */
    public static String get(String url, Map<String, String> headers) {
        try (HttpResponse response = HttpRequest.get(url)
                .addHeaders(headers)
                .execute()) {
            return response.body();
        }
    }

}
