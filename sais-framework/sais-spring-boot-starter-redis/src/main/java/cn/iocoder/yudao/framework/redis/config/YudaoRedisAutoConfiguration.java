package cn.iocoder.yudao.framework.redis.config;

import cn.hutool.core.util.ReflectUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * Redis configuration class
 */
@AutoConfiguration(before = RedissonAutoConfiguration.class) // Purpose: Use your own defined RedisTemplate Bean
public class YudaoRedisAutoConfiguration {

 /**
     * Create a RedisTemplate Bean and use JSON serialization
 */
 @Bean
 public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        // Create a RedisTemplate object
 RedisTemplate<String, Object> template = new RedisTemplate<>();
        // Set the RedisConnectionFactory. It abstracts access to different Java Redis clients.
 template.setConnectionFactory(factory);
        // Use String serialization method to serialize KEY.
 template.setKeySerializer(RedisSerializer.string());
 template.setHashKeySerializer(RedisSerializer.string());
        // Use JSON serialization method (the library is Jackson) to serialize VALUE.
 template.setValueSerializer(buildRedisSerializer());
 template.setHashValueSerializer(buildRedisSerializer());
 return template;
 }

 public static RedisSerializer<?> buildRedisSerializer() {
 RedisSerializer<Object> json = RedisSerializer.json();
        // Solve the serialization of LocalDateTime
 ObjectMapper objectMapper = (ObjectMapper) ReflectUtil.getFieldValue(json, "mapper");
 objectMapper.registerModules(new JavaTimeModule());
 return json;
 }

}
