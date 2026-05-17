package cn.iocoder.yudao.framework.mq.redis.config;

import cn.iocoder.yudao.framework.mq.redis.core.RedisMQTemplate;
import cn.iocoder.yudao.framework.mq.redis.core.interceptor.RedisMessageInterceptor;
import cn.iocoder.yudao.framework.redis.config.YudaoRedisAutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;

/**
 * Redis message queue Producer configuration class
 *
 * @author Yudao Source Code
 */
@Slf4j
@AutoConfiguration(after = YudaoRedisAutoConfiguration.class)
public class YudaoRedisMQProducerAutoConfiguration {

 @Bean
 public RedisMQTemplate redisMQTemplate(StringRedisTemplate redisTemplate,
 List<RedisMessageInterceptor> interceptors) {
 RedisMQTemplate redisMQTemplate = new RedisMQTemplate(redisTemplate);
        // Add interceptor
 interceptors.forEach(redisMQTemplate::addInterceptor);
 return redisMQTemplate;
 }

}
