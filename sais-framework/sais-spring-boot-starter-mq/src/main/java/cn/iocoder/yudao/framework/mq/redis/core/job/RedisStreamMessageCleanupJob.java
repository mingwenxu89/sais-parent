package cn.iocoder.yudao.framework.mq.redis.core.job;

import cn.iocoder.yudao.framework.mq.redis.core.RedisMQTemplate;
import cn.iocoder.yudao.framework.mq.redis.core.stream.AbstractRedisStreamMessageListener;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StreamOperations;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

/**
 * Redis Stream message cleaning task
 * Used to regularly clean up consumed messages to prevent excessive memory usage
 *
 * @see <a href="https://www.cnblogs.com/nanxiang/p/16179519.html">Remember the problem of Redis stream data type memory not being released</a>
 *
 * @author Yudao Source Code
 */
@Slf4j
@AllArgsConstructor
public class RedisStreamMessageCleanupJob {

 private static final String LOCK_KEY = "redis:stream:message-cleanup:lock";

 /**
     * The number of messages to retain, the most recent 10,000 messages are retained by default
 */
 private static final long MAX_COUNT = 10000;

 private final List<AbstractRedisStreamMessageListener<?>> listeners;
 private final RedisMQTemplate redisTemplate;
 private final RedissonClient redissonClient;

 /**
     * Perform cleaning tasks every hour
 */
 @Scheduled(cron = "0 0 * * * ?")
 public void cleanup() {
 RLock lock = redissonClient.getLock(LOCK_KEY);
        // Try to lock
 if (lock.tryLock()) {
 try {
 execute();
 } catch (Exception ex) {
                log.error("[cleanup][execution exception]", ex);
 } finally {
 lock.unlock();
 }
 }
 }

 /**
     * Execute cleanup logic
 */
 private void execute() {
 StreamOperations<String, Object, Object> ops = redisTemplate.getRedisTemplate().opsForStream();
 listeners.forEach(listener -> {
 try {
                // Use the XTRIM command to clean up messages and keep only the most recent MAX_LEN messages
 Long trimCount = ops.trim(listener.getStreamKey(), MAX_COUNT, true);
 if (trimCount != null && trimCount > 0) {
                    log.info("[execute][Stream({}) Clean the number of messages ({})]", listener.getStreamKey(), trimCount);
 }
 } catch (Exception ex) {
                log.error("[execute][Stream({}) cleanup exception]", listener.getStreamKey(), ex);
 }
 });
 }
}