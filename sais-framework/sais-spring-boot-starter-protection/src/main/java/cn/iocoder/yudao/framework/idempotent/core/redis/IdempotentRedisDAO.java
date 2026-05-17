package cn.iocoder.yudao.framework.idempotent.core.redis;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * Idempotent Redis DAO
 *
 * @author Yudao Source Code
 */
@AllArgsConstructor
public class IdempotentRedisDAO {

 /**
     * Idempotent operations
 *
     * KEY format: idempotent:%s // The parameter is uuid
     * VALUE format: String
     * Expiration time: not fixed
 */
 private static final String IDEMPOTENT = "idempotent:%s";

 private final StringRedisTemplate redisTemplate;

 public Boolean setIfAbsent(String key, long timeout, TimeUnit timeUnit) {
 String redisKey = formatKey(key);
 return redisTemplate.opsForValue().setIfAbsent(redisKey, "", timeout, timeUnit);
 }

 public void delete(String key) {
 String redisKey = formatKey(key);
 redisTemplate.delete(redisKey);
 }

 private static String formatKey(String key) {
 return String.format(IDEMPOTENT, key);
 }

}
