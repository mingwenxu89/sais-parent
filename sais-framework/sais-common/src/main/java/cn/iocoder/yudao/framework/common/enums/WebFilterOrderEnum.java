package cn.iocoder.yudao.framework.common.enums;

/**
 * Enumeration class of Web filter order to ensure that the filter is in line with our expectations
 *
 * Considering that every starter needs to use this tool class, it is placed under the enums package under the common module.
 *
 * @author Yudao Source Code
 */
public interface WebFilterOrderEnum {

 int CORS_FILTER = Integer.MIN_VALUE;

 int TRACE_FILTER = CORS_FILTER + 1;

 int REQUEST_BODY_CACHE_FILTER = Integer.MIN_VALUE + 500;

 int API_ENCRYPT_FILTER = REQUEST_BODY_CACHE_FILTER + 1;

    // OrderedRequestContextFilter defaults to -105, used for internationalization context, etc.

    int TENANT_CONTEXT_FILTER = - 104; // Need to ensure that it is in front of APIAccessLogFilter

    int API_ACCESS_LOG_FILTER = -103; // Need to ensure that it is behind RequestBodyCacheFilter

    int XSS_FILTER = -102;  // Need to ensure that it is behind RequestBodyCacheFilter

    // Spring Security Filter defaults to -100, visible org.springframework.boot.autoconfigure.security.SecurityProperties configuration property class

    int TENANT_SECURITY_FILTER = -99; // Need to ensure that it is behind the Spring Security filter

    int FLOWABLE_FILTER = -98; // Need to ensure that behind Spring Security filtering

 int DEMO_FILTER = Integer.MAX_VALUE;

}
