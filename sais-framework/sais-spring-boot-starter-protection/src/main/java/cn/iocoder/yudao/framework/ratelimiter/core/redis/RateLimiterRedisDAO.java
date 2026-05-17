package cn.iocoder.yudao.framework.ratelimiter.core.redis;

import lombok.AllArgsConstructor;
import org.redisson.api.*;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Current limiting Redis DAO
 *
 * @author Yudao Source Code
 */
@AllArgsConstructor
public class RateLimiterRedisDAO {

 /**
     * Current limiting operation
 *
     * KEY format: rate_limiter:%s // The parameter is uuid
     * VALUE format: String
     * Expiration time: not fixed
 */
 private static final String RATE_LIMITER = "rate_limiter:%s";

 private final RedissonClient redissonClient;

 public Boolean tryAcquire(String key, int count, int time, TimeUnit timeUnit) {
        // 1. Obtain RRateLimiter and set the rate rate
 RRateLimiter rateLimiter = getRRateLimiter(key, count, time, timeUnit);
        // 2. Try to get 1
 return rateLimiter.tryAcquire();
 }

 private static String formatKey(String key) {
 return String.format(RATE_LIMITER, key);
 }

 private RRateLimiter getRRateLimiter(String key, long count, int time, TimeUnit timeUnit) {
 String redisKey = formatKey(key);
 RRateLimiter rateLimiter = redissonClient.getRateLimiter(redisKey);
 long rateInterval = timeUnit.toSeconds(time);
 Duration duration = Duration.ofSeconds(rateInterval);
        // 1. If it does not exist, set the rate rate
 RateLimiterConfig config = rateLimiter.getConfig();
 if (config == null) {
 rateLimiter.trySetRate(RateType.OVERALL, count, duration);
            // For the reasons, see https://t.zsxq.com/lcR0W
 rateLimiter.expire(duration);
 return rateLimiter;
 }
        // 2. If it exists and the configuration is the same, return directly
 if (config.getRateType() == RateType.OVERALL
 && Objects.equals(config.getRate(), count)
 && Objects.equals(config.getRateInterval(), TimeUnit.SECONDS.toMillis(rateInterval))) {
 return rateLimiter;
 }
        // 3. If it exists and the configuration is different, create a new one
 rateLimiter.setRate(RateType.OVERALL, count, duration);
        // For the reasons, see https://t.zsxq.com/lcR0W
 rateLimiter.expire(duration);
 return rateLimiter;
 }

}
