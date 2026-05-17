package cn.iocoder.yudao.framework.redis.core;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;

import java.time.Duration;

/**
 * {@link RedisCacheManager} implementation class that supports custom expiration time
 *
 * When the format of {@link Cacheable#cacheNames()} is "key#ttl", the ttl after # is the expiration time.
 * The unit is the last letter (supported units are: d days, h hours, m minutes, s seconds), the default unit is s seconds
 *
 * @author Yudao Source Code
 */
public class TimeoutRedisCacheManager extends RedisCacheManager {

 private static final String SPLIT = "#";

 public TimeoutRedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration) {
 super(cacheWriter, defaultCacheConfiguration);
 }

 @Override
 protected RedisCache createRedisCache(String name, RedisCacheConfiguration cacheConfig) {
 if (StrUtil.isEmpty(name)) {
 return super.createRedisCache(name, cacheConfig);
 }
        // If separated by # and the size is not 2, it means that the custom expiration time is not used
 String[] names = StrUtil.splitToArray(name, SPLIT);
 if (names.length != 2) {
 return super.createRedisCache(name, cacheConfig);
 }

        // Core: Customize the expiration time by modifying the expiration time of cacheConfig
 if (cacheConfig != null) {
            // Remove the : and following content after # to avoID affecting the parsing
            String ttlStr = StrUtil.subBefore(names[1], StrUtil.COLON, false); // Get the time part of ttlStr
            names[1] = StrUtil.subAfter(names[1], ttlStr, false); // Remove the ttlStr time part
            // parsing time
 Duration duration = parseDuration(ttlStr);
 cacheConfig = cacheConfig.entryTtl(duration);
 }

        // To create a RedisCache object, you need to ignore ttlStr
 return super.createRedisCache(names[0] + names[1], cacheConfig);
 }

 /**
     * Parse expiration time Duration
 *
     * @param ttlStr Expiration time string
     * @return Expiration time Duration
 */
 private Duration parseDuration(String ttlStr) {
 String timeUnit = StrUtil.subSuf(ttlStr, -1);
 switch (timeUnit) {
 case "d":
 return Duration.ofDays(removeDurationSuffix(ttlStr));
 case "h":
 return Duration.ofHours(removeDurationSuffix(ttlStr));
 case "m":
 return Duration.ofMinutes(removeDurationSuffix(ttlStr));
 case "s":
 return Duration.ofSeconds(removeDurationSuffix(ttlStr));
 default:
 return Duration.ofSeconds(Long.parseLong(ttlStr));
 }
 }

 /**
     * Remove redundant suffixes and return specific time
 *
     * @param ttlStr Expiration time string
     * @return time
 */
 private Long removeDurationSuffix(String ttlStr) {
 return NumberUtil.parseLong(StrUtil.sub(ttlStr, 0, ttlStr.length() - 1));
 }

}
