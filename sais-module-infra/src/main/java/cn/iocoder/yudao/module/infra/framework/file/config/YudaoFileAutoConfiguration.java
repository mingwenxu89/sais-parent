package cn.iocoder.yudao.module.infra.framework.file.config;

import cn.iocoder.yudao.module.infra.framework.file.core.client.FileClientFactory;
import cn.iocoder.yudao.module.infra.framework.file.core.client.FileClientFactoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * File configuration class
 *
 * @author Yudao Source Code
 */
@Configuration(proxyBeanMethods = false)
public class YudaoFileAutoConfiguration {

    @Bean
    public FileClientFactory fileClientFactory() {
        return new FileClientFactoryImpl();
    }

}
