package cn.iocoder.yudao.framework.datasource.config;

import cn.iocoder.yudao.framework.datasource.core.filter.DruidAdRemoveFilter;
import com.alibaba.druid.spring.boot3.autoconfigure.properties.DruidStatProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Database configuration class
 *
 * @author Yudao Source Code
 */
@AutoConfiguration
@EnableTransactionManagement(proxyTargetClass = true) // Start transaction management
@EnableConfigurationProperties(DruidStatProperties.class)
public class YudaoDataSourceAutoConfiguration {

 /**
     * Create a DruidAdRemoveFilter filter to filter common.js ads
 */
 @Bean
 @ConditionalOnProperty(name = "spring.datasource.druid.stat-view-servlet.enabled", havingValue = "true")
 public FilterRegistrationBean<DruidAdRemoveFilter> druidAdRemoveFilterFilter(DruidStatProperties properties) {
        // Get parameters of druID web monitoring page
 DruidStatProperties.StatViewServlet config = properties.getStatViewServlet();
        // Extract the configuration path of common.js
 String pattern = config.getUrlPattern() != null ? config.getUrlPattern(): "/druid/*";
 String commonJsPattern = pattern.replaceAll("\\*", "js/common.js");
        // Create a DruidAdRemoveFilter bean
 FilterRegistrationBean<DruidAdRemoveFilter> registrationBean = new FilterRegistrationBean<>();
 registrationBean.setFilter(new DruidAdRemoveFilter());
 registrationBean.addUrlPatterns(commonJsPattern);
 return registrationBean;
 }

}
