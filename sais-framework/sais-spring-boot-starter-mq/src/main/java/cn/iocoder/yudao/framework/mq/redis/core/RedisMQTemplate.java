package cn.iocoder.yudao.framework.mq.redis.core;

import cn.iocoder.yudao.framework.common.util.json.JsonUtils;
import cn.iocoder.yudao.framework.mq.redis.core.interceptor.RedisMessageInterceptor;
import cn.iocoder.yudao.framework.mq.redis.core.message.AbstractRedisMessage;
import cn.iocoder.yudao.framework.mq.redis.core.pubsub.AbstractRedisChannelMessage;
import cn.iocoder.yudao.framework.mq.redis.core.stream.AbstractRedisStreamMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Redis MQ operation template class
 *
 * @author Yudao Source Code
 */
@AllArgsConstructor
public class RedisMQTemplate {

 @Getter
 private final RedisTemplate<String, ?> redisTemplate;
 /**
     * interceptor array
 */
 @Getter
 private final List<RedisMessageInterceptor> interceptors = new ArrayList<>();

 /**
     * Send Redis messages, implemented based on Redis pub/sub
 *
     * @param message information
 */
 public <T extends AbstractRedisChannelMessage> void send(T message) {
 try {
 sendMessageBefore(message);
            // Send message
 redisTemplate.convertAndSend(message.getChannel(), JsonUtils.toJsonString(message));
 } finally {
 sendMessageAfter(message);
 }
 }

 /**
     * Send Redis messages, implemented based on Redis Stream
 *
     * @param message information
     * @return The number object of the message record
 */
 public <T extends AbstractRedisStreamMessage> RecordId send(T message) {
 try {
 sendMessageBefore(message);
            // Send message
 return redisTemplate.opsForStream().add(StreamRecords.newRecord()
                    .ofObject(JsonUtils.toJsonString(message)) // Set content
                    .withStreamKey(message.getStreamKey())); // Set stream key
 } finally {
 sendMessageAfter(message);
 }
 }

 /**
     * Add interceptor
 *
     * @param interceptor Interceptor
 */
 public void addInterceptor(RedisMessageInterceptor interceptor) {
 interceptors.add(interceptor);
 }

 private void sendMessageBefore(AbstractRedisMessage message) {
        // positive sequence
 interceptors.forEach(interceptor -> interceptor.sendMessageBefore(message));
 }

 private void sendMessageAfter(AbstractRedisMessage message) {
        // reverse order
 for (int i = interceptors.size() - 1; i >= 0; i--) {
 interceptors.get(i).sendMessageAfter(message);
 }
 }

}
