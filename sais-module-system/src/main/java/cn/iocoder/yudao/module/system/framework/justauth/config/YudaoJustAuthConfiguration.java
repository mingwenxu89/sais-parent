package cn.iocoder.yudao.module.system.framework.justauth.config;

import cn.iocoder.yudao.module.system.framework.justauth.core.AuthRequestFactory;
import com.xkcoding.justauth.autoconfigure.JustAuthProperties;
import com.xkcoding.justauth.support.cache.RedisStateCache;
import me.zhyd.oauth.cache.AuthStateCache;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * JustAuth configuration class TODO: Wait for justauth 1.4.1 version to be released! ! !
 *
 * @author Yudao Source Code
 */
@Configuration(proxyBeanMethods = false)
public class YudaoJustAuthConfiguration {

    @Bean(name = "authRequestFactory2") // TODO @Taro: You can remove it after justauth1.4.1 is released.
    @ConditionalOnProperty(
            prefix = "justauth",
            value = {"enabled"},
            havingValue = "true",
            matchIfMissing = true
    )
    public AuthRequestFactory authRequestFactory(JustAuthProperties properties, AuthStateCache authStateCache) {
        return new AuthRequestFactory(properties, authStateCache);
    }

    @Bean
    public AuthStateCache authStateCache(RedisTemplate<String, String> justAuthRedisCacheTemplate,
                                         JustAuthProperties justAuthProperties) {
        return new RedisStateCache(justAuthRedisCacheTemplate, justAuthProperties.getCache());
    }

}
