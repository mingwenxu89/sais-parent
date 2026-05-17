package cn.iocoder.yudao.framework.tracer.core.filter;

import cn.iocoder.yudao.framework.common.util.monitor.TracerUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Trace filter, print traceID to header and return
 *
 * @author Yudao Source Code
 */
public class TraceFilter extends OncePerRequestFilter {

 /**
     * Header name - link tracking number
 */
 private static final String HEADER_NAME_TRACE_ID = "trace-id";

 @Override
 protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
 throws IOException, ServletException {
        // Set response traceId
 response.addHeader(HEADER_NAME_TRACE_ID, TracerUtils.getTraceId());
        // continue filtering
 chain.doFilter(request, response);
 }

}
