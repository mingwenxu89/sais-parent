package cn.iocoder.yudao.framework.web.config;

import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.biz.infra.logger.ApiErrorLogCommonApi;
import cn.iocoder.yudao.framework.common.enums.WebFilterOrderEnum;
import cn.iocoder.yudao.framework.web.core.filter.CacheRequestBodyFilter;
import cn.iocoder.yudao.framework.web.core.filter.DemoFilter;
import cn.iocoder.yudao.framework.web.core.handler.GlobalExceptionHandler;
import cn.iocoder.yudao.framework.web.core.handler.GlobalResponseBodyHandler;
import cn.iocoder.yudao.framework.web.core.util.WebFrameworkUtils;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.client.RestTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import jakarta.servlet.Filter;
import java.util.Map;
import java.util.function.Predicate;

@AutoConfiguration
@EnableConfigurationProperties(WebProperties.class)
public class YudaoWebAutoConfiguration {

 /**
     * Application name
 */
 @Value("${spring.application.name}")
 private String applicationName;

 @Bean
 public WebMvcRegistrations webMvcRegistrations(WebProperties webProperties) {
 return new WebMvcRegistrations() {

 @Override
 public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
 RequestMappingHandlerMapping mapping = new RequestMappingHandlerMapping();
                // Add the prefix when instantiating
 mapping.setPathPrefixes(buildPathPrefixes(webProperties));
 return mapping;
 }

 /**
             * Build prefix → mapping of matching conditions
 */
 private Map<String, Predicate<Class<?>>> buildPathPrefixes(WebProperties webProperties) {
 AntPathMatcher antPathMatcher = new AntPathMatcher(".");
 Map<String, Predicate<Class<?>>> pathPrefixes = Maps.newLinkedHashMapWithExpectedSize(2);
 putPathPrefix(pathPrefixes, webProperties.getAdminApi(), antPathMatcher);
 putPathPrefix(pathPrefixes, webProperties.getAppApi(), antPathMatcher);
 return pathPrefixes;
 }

 /**
             * Set the API prefix to only match those under the controller package
 */
 private void putPathPrefix(Map<String, Predicate<Class<?>>> pathPrefixes, WebProperties.Api api, AntPathMatcher matcher) {
 if (api == null || StrUtil.isEmpty(api.getPrefix())) {
 return;
 }
                pathPrefixes.put(api.getPrefix(), // API prefix
 clazz -> clazz.isAnnotationPresent(RestController.class)
 && matcher.match(api.getController(), clazz.getPackage().getName()));
 }

 };
 }

 @Bean
 @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
 public GlobalExceptionHandler globalExceptionHandler(ApiErrorLogCommonApi apiErrorLogApi) {
 return new GlobalExceptionHandler(applicationName, apiErrorLogApi);
 }

 @Bean
 public GlobalResponseBodyHandler globalResponseBodyHandler() {
 return new GlobalResponseBodyHandler();
 }

 @Bean
 @SuppressWarnings("InstantiationOfUtilityClass")
 public WebFrameworkUtils webFrameworkUtils(WebProperties webProperties) {
        // Since WebFrameworkUtils needs to use the webProperties attribute, it is registered as a Bean
 return new WebFrameworkUtils(webProperties);
 }

    // ========== Filter related ==========

 /**
     * Create CorsFilter Bean to solve cross-domain issues
 */
 @Bean
    @Order(value = WebFilterOrderEnum.CORS_FILTER) // Special: Fixed the problem that cross-domain configuration does not take effect due to the execution order.
 public FilterRegistrationBean<CorsFilter> corsFilterBean() {
        // Create CorsConfiguration object
 CorsConfiguration config = new CorsConfiguration();
 config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*"); // Set access source address
        config.addAllowedHeader("*"); // Set access source request header
        config.addAllowedMethod("*"); // Set access source request method
        // Create UrlBasedCorsConfigurationSource object
 UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // Configure cross-domain settings for the interface
 return createFilterBean(new CorsFilter(source), WebFilterOrderEnum.CORS_FILTER);
 }

 /**
     * Create a RequestBodyCacheFilter Bean to read request content repeatedly
 */
 @Bean
 public FilterRegistrationBean<CacheRequestBodyFilter> requestBodyCacheFilter() {
 return createFilterBean(new CacheRequestBodyFilter(), WebFilterOrderEnum.REQUEST_BODY_CACHE_FILTER);
 }

 /**
     * Create DemoFilter Bean, demo mode
 */
 @Bean
 @ConditionalOnProperty(value = "yudao.demo", havingValue = "true")
 public FilterRegistrationBean<DemoFilter> demoFilter() {
 return createFilterBean(new DemoFilter(), WebFilterOrderEnum.DEMO_FILTER);
 }

 public static <T extends Filter> FilterRegistrationBean<T> createFilterBean(T filter, Integer order) {
 FilterRegistrationBean<T> bean = new FilterRegistrationBean<>(filter);
 bean.setOrder(order);
 return bean;
 }

 /**
     * Create RestTemplate instance
 *
 * @param restTemplateBuilder {@link RestTemplateAutoConfiguration#restTemplateBuilder}
 */
 @Bean
 @ConditionalOnMissingBean
 public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
 return restTemplateBuilder.build();
 }

}
