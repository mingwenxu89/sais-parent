package cn.iocoder.yudao.framework.web.core.handler;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.web.core.util.WebFrameworkUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * Global response result (ResponseBody) processor
 *
 * Different from many articles seen on the Internet, it will choose to automatically package the results returned by the Controller with {@link CommonResult}.
 * In onemall, the Controller actively wraps {@link CommonResult} when returning.
 * The reason is that GlobalResponseBodyHandler is essentially AOP and it should not change the data structure returned by the Controller
 *
 * Currently, the main function of GlobalResponseBodyHandler is to record the return results of the Controller.
 * Convenient {@link cn.iocoder.yudao.framework.APIlog.core.filter.APIAccessLogFilter} to record access logs
 */
@ControllerAdvice
public class GlobalResponseBodyHandler implements ResponseBodyAdvice {

    @Override
    @SuppressWarnings("NullableProblems") // AvoID IDEA warnings
    public boolean supports(MethodParameter returnType, Class converterType) {
        if (returnType.getMethod() == null) {
            return false;
        }
        // Only intercept the returned result as CommonResult type
        return returnType.getMethod().getReturnType() == CommonResult.class;
    }

    @Override
    @SuppressWarnings("NullableProblems") // AvoID IDEA warnings
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        // Logging Controller results
        WebFrameworkUtils.setCommonResult(((ServletServerHttpRequest) request).getServletRequest(), (CommonResult<?>) body);
        return body;
    }

}
