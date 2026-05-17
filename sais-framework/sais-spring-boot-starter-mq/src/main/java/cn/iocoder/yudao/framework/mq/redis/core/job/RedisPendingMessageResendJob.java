package cn.iocoder.yudao.framework.mq.redis.core.job;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.yudao.framework.mq.redis.core.RedisMQTemplate;
import cn.iocoder.yudao.framework.mq.redis.core.stream.AbstractRedisStreamMessageListener;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.domain.Range;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.StreamOperations;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * This task is used to process unconsumed messages by consumers after the crash.
 */
@Slf4j
@AllArgsConstructor
public class RedisPendingMessageResendJob {

 private static final String LOCK_KEY = "redis:stream:pending-message-resend:lock";

 /**
     * Message timeout, default 5 minutes
 *
     * 1. Only messages that time out will be re-delivered.
     * 2. Since the scheduled task occurs once every minute, the message will not be re-delivered immediately after timeout. In extreme cases, after the message expires in 5 minutes, it will wait for another 1 minute before being scanned.
 */
 private static final int EXPIRE_TIME = 5 * 60;

 private final List<AbstractRedisStreamMessageListener<?>> listeners;
 private final RedisMQTemplate redisTemplate;
 private final RedissonClient redissonClient;

 /**
     * Executed once a minute, here we choose 35 seconds to execute every minute to avoID the problem of too many tasks on the hour
 */
 @Scheduled(cron = "35 * * * * ?")
 public void messageResend() {
 RLock lock = redissonClient.getLock(LOCK_KEY);
        // Try to lock
 if (lock.tryLock()) {
 try {
 execute();
 } catch (Exception ex) {
                log.error("[messageResend][Execution exception]", ex);
 } finally {
 lock.unlock();
 }
 }
 }

 /**
     * Execute cleanup logic
 *
     * @see <a href="https://gitee.com/zhijiantianya/ruoyi-vue-pro/pulls/480/files">Discussion</a>
 */
 private void execute() {
 StreamOperations<String, Object, Object> ops = redisTemplate.getRedisTemplate().opsForStream();
 listeners.forEach(listener -> {
 PendingMessagesSummary pendingMessagesSummary = Objects.requireNonNull(ops.pending(listener.getStreamKey(), listener.getGroup()));
            // The number of pending queue messages per consumer
 Map<String, Long> pendingMessagesPerConsumer = pendingMessagesSummary.getPendingMessagesPerConsumer();
 pendingMessagesPerConsumer.forEach((consumerName, pendingMessageCount) -> {
                log.info("[processPendingMessage][Consumer({}) Number of messages({})]", consumerName, pendingMessageCount);
                // Details of pending messages for each consumer
 PendingMessages pendingMessages = ops.pending(listener.getStreamKey(), Consumer.from(listener.getGroup(), consumerName), Range.unbounded(), pendingMessageCount);
 if (pendingMessages.isEmpty()) {
 return;
 }
 pendingMessages.forEach(pendingMessage -> {
                    // Get the time when the message was last delivered to the consumer,
 long lastDelivery = pendingMessage.getElapsedTimeSinceLastDelivery().getSeconds();
 if (lastDelivery < EXPIRE_TIME){
 return;
 }
                    // Get the message body with the specified id
 List<MapRecord<String, Object, Object>> records = ops.range(listener.getStreamKey(),
 Range.of(Range.Bound.inclusive(pendingMessage.getIdAsString()), Range.Bound.inclusive(pendingMessage.getIdAsString())));
 if (CollUtil.isEmpty(records)) {
 return;
 }
                    // redeliver message
 redisTemplate.getRedisTemplate().opsForStream().add(StreamRecords.newRecord()
                            .ofObject(records.get(0).getValue()) // Set content
.withStreamKey(listener.getStreamKey()));
                    // ack message consumption completed
 redisTemplate.getRedisTemplate().opsForStream().acknowledge(listener.getGroup(), records.get(0));
                    log.info("[processPendingMessage][Message ({}) re-delivered successfully]", records.get(0).getId());
 });
 });
 });
 }
}
