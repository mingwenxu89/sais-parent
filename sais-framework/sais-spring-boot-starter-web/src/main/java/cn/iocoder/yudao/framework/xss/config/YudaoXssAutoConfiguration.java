package cn.iocoder.yudao.framework.xss.config;

import cn.iocoder.yudao.framework.common.enums.WebFilterOrderEnum;
import cn.iocoder.yudao.framework.xss.core.clean.JsoupXssCleaner;
import cn.iocoder.yudao.framework.xss.core.clean.XssCleaner;
import cn.iocoder.yudao.framework.xss.core.filter.XssFilter;
import cn.iocoder.yudao.framework.xss.core.json.XssStringJsonDeserializer;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.util.PathMatcher;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static cn.iocoder.yudao.framework.web.config.YudaoWebAutoConfiguration.createFilterBean;

@AutoConfiguration
@EnableConfigurationProperties(XssProperties.class)
@ConditionalOnProperty(prefix = "yudao.xss", name = "enable", havingValue = "true", matchIfMissing = true) // When set to false, disables
public class YudaoXssAutoConfiguration implements WebMvcConfigurer {

 /**
     * Xss cleaner
 *
 * @return XssCleaner
 */
 @Bean
 @ConditionalOnMissingBean(XssCleaner.class)
 public XssCleaner xssCleaner() {
 return new JsoupXssCleaner();
 }

 /**
     * Register Jackson's serializer to handle xss filtering of JSON type parameters
 *
 * @return Jackson2ObjectMapperBuilderCustomizer
 */
 @Bean
 @ConditionalOnMissingBean(name = "xssJacksonCustomizer")
 @ConditionalOnProperty(value = "yudao.xss.enable", havingValue = "true")
 public Jackson2ObjectMapperBuilderCustomizer xssJacksonCustomizer(XssProperties properties,
 PathMatcher pathMatcher,
 XssCleaner xssCleaner) {
        // To perform xss filtering during deserialization, you can use XssStringJSONSerializer instead and process it during serialization.
 return builder ->
 builder.deserializerByType(String.class, new XssStringJsonDeserializer(properties, pathMatcher, xssCleaner));
 }

 /**
     * Create XssFilter Bean to solve Xss security issues
 */
 @Bean
 @ConditionalOnBean(XssCleaner.class)
 public FilterRegistrationBean<XssFilter> xssFilter(XssProperties properties, PathMatcher pathMatcher, XssCleaner xssCleaner) {
 return createFilterBean(new XssFilter(properties, pathMatcher, xssCleaner), WebFilterOrderEnum.XSS_FILTER);
 }

}
