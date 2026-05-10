package cn.iocoder.yudao.module.agri.framework.web.config;

import cn.iocoder.yudao.framework.swagger.config.YudaoSwaggerAutoConfiguration;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Web component configuration for the agri module
 */
@Configuration(proxyBeanMethods = false)
public class AgriWebConfiguration {

    /**
     * API group for the agri module
     */
    @Bean
    public GroupedOpenApi agriGroupedOpenApi() {
        return YudaoSwaggerAutoConfiguration.buildGroupedOpenApi("agri");
    }

}
