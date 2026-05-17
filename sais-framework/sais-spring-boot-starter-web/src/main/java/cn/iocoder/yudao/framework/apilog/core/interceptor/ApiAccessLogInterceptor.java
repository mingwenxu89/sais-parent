package cn.iocoder.yudao.framework.apilog.core.interceptor;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.util.servlet.ServletUtils;
import cn.iocoder.yudao.framework.common.util.spring.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

/**
 * API access log Interceptor
 *
 * Purpose: In a non-prod environment, print two logs, request and response, to the log file (console).
 *
 * @author Yudao Source Code
 */
@Slf4j
public class ApiAccessLogInterceptor implements HandlerInterceptor {

 public static final String ATTRIBUTE_HANDLER_METHOD = "HANDLER_METHOD";

 private static final String ATTRIBUTE_STOP_WATCH = "ApiAccessLogInterceptor.StopWatch";

 @Override
 public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // Record HandlerMethod and provide it to APIAccessLogFilter for use
 HandlerMethod handlerMethod = handler instanceof HandlerMethod ? (HandlerMethod) handler: null;
 if (handlerMethod != null) {
 request.setAttribute(ATTRIBUTE_HANDLER_METHOD, handlerMethod);
 }

        // Print request log
 if (!SpringUtils.isProd()) {
 Map<String, String> queryString = ServletUtils.getParamMap(request);
 String requestBody = ServletUtils.isJsonRequest(request) ? ServletUtils.getBody(request): null;
 if (CollUtil.isEmpty(queryString) && StrUtil.isEmpty(requestBody)) {
                log.info("[preHandle][Start requesting URL({}) no parameters]", request.getRequestURI());
 } else {
                log.info("[preHandle][Start request URL({}) parameters({})]", request.getRequestURI(),
 StrUtil.blankToDefault(requestBody, queryString.toString()));
 }
            // Timing
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 request.setAttribute(ATTRIBUTE_STOP_WATCH, stopWatch);
            // Print controller path
 printHandlerMethodPosition(handlerMethod);
 }
 return true;
 }

 @Override
 public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // Print response log
 if (!SpringUtils.isProd()) {
 StopWatch stopWatch = (StopWatch) request.getAttribute(ATTRIBUTE_STOP_WATCH);
 stopWatch.stop();
            log.info("[afterCompletion][Complete request URL({}) time consuming ({} ms)]",
 request.getRequestURI(), stopWatch.getTotalTimeMillis());
 }
 }

 /**
     * Print Controller method path
 */
 private void printHandlerMethodPosition(HandlerMethod handlerMethod) {
 if (handlerMethod == null) {
 return;
 }
 Method method = handlerMethod.getMethod();
 Class<?> clazz = method.getDeclaringClass();
 try {
            // Get the lineNumber of method
 List<String> clazzContents = FileUtil.readUtf8Lines(
 ResourceUtil.getResource(null, clazz).getPath().replace("/target/classes/", "/src/main/java/")
 + clazz.getSimpleName() + ".java");
 Optional<Integer> lineNumber = IntStream.range(0, clazzContents.size())
                    .filter(i -> clazzContents.get(i).contains(" " + method.getName() + "(")) // Simple matching, regardless of method duplicate names
                    .mapToObj(i -> i + 1) // Line numbers start from 1
.findFirst();
 if (!lineNumber.isPresent()) {
 return;
 }
            // Print results
            System.out.printf("\tController method path: %s(%s.java:%d)\n", clazz.getName(), clazz.getSimpleName(), lineNumber.get());
 } catch (Exception ignore) {
            // Ignore exceptions. Reason: Just printing, non-important logic
 }
 }

}
