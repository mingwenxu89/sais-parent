package cn.iocoder.yudao.module.infra.framework.web.config;

import cn.iocoder.yudao.framework.swagger.config.YudaoSwaggerAutoConfiguration;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration of the web component of the infra module
 *
 * @author Yudao Source Code
 */
@Configuration(proxyBeanMethods = false)
public class InfraWebConfiguration {

    /**
     * API grouping for infra module
     */
    @Bean
    public GroupedOpenApi infraGroupedOpenApi() {
        return YudaoSwaggerAutoConfiguration.buildGroupedOpenApi("infra");
    }

}
