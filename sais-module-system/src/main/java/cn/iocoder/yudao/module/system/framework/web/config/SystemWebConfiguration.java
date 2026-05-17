package cn.iocoder.yudao.module.system.framework.web.config;

import cn.iocoder.yudao.framework.swagger.config.YudaoSwaggerAutoConfiguration;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration of the web component of the system module
 *
 * @author Yudao Source Code
 */
@Configuration(proxyBeanMethods = false)
public class SystemWebConfiguration {

    /**
     * API grouping of system module
     */
    @Bean
    public GroupedOpenApi systemGroupedOpenApi() {
        return YudaoSwaggerAutoConfiguration.buildGroupedOpenApi("system");
    }

}
