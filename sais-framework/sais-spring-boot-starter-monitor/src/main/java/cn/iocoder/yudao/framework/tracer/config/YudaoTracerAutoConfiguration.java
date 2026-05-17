package cn.iocoder.yudao.framework.tracer.config;

import cn.iocoder.yudao.framework.common.enums.WebFilterOrderEnum;
import cn.iocoder.yudao.framework.tracer.core.filter.TraceFilter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

/**
 * Tracer configuration class
 *
 * @author mashu
 */
@AutoConfiguration
@ConditionalOnClass(name = {
        "org.apache.skywalking.apm.toolkit.opentracing.SkywalkingTracer", // From apm-toolkit-opentracing.jar
// "io.opentracing.Tracer", // from opentracing-API.jar
 "javax.servlet.Filter"
})
@EnableConfigurationProperties(TracerProperties.class)
@ConditionalOnProperty(prefix = "yudao.tracer", value = "enable", matchIfMissing = true)
public class YudaoTracerAutoConfiguration {

    // TODO @芋芿: skywalking is not compatible with the latest opentracing version. At the same time, opentracing also stopped maintenance, which was embarrassing! Just change to opentelemetry later!
// @Bean
// public BizTraceAspect bizTracingAop() {
// return new BizTraceAspect(tracer());
// }
//
// @Bean
// public Tracer tracer() {
// // Create SkywalkingTracer object
// SkywalkingTracer tracer = new SkywalkingTracer();
// // Set as a tracker for GlobalTracer
// GlobalTracer.registerIfAbsent(tracer);
// return tracer;
// }

 /**
     * Create a TraceFilter filter and set the traceID in the response header
 */
 @Bean
 public FilterRegistrationBean<TraceFilter> traceFilter() {
 FilterRegistrationBean<TraceFilter> registrationBean = new FilterRegistrationBean<>();
 registrationBean.setFilter(new TraceFilter());
 registrationBean.setOrder(WebFilterOrderEnum.TRACE_FILTER);
 return registrationBean;
 }

}
