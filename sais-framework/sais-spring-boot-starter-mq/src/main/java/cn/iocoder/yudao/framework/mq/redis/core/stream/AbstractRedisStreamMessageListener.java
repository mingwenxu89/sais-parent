package cn.iocoder.yudao.framework.mq.redis.core.stream;

import cn.hutool.core.util.TypeUtil;
import cn.iocoder.yudao.framework.common.util.json.JsonUtils;
import cn.iocoder.yudao.framework.mq.redis.core.RedisMQTemplate;
import cn.iocoder.yudao.framework.mq.redis.core.interceptor.RedisMessageInterceptor;
import cn.iocoder.yudao.framework.mq.redis.core.message.AbstractRedisMessage;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.stream.StreamListener;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Redis Stream listener abstract class, used to implement cluster consumption
 *
 * @param <T> Message type. Be sure to fill it out, otherwise an error will be reported
 *
 * @author Yudao Source Code
 */
public abstract class AbstractRedisStreamMessageListener<T extends AbstractRedisStreamMessage>
 implements StreamListener<String, ObjectRecord<String, String>> {

 /**
     * Message type
 */
 private final Class<T> messageType;
 /**
 * Redis Channel
 */
 @Getter
 private final String streamKey;

 /**
     * Redis consumer group, uses spring.application.name name by default
 */
 @Value("${spring.application.name}")
 @Getter
 private String group;
 /**
 * RedisMQTemplate
 */
 @Setter
 private RedisMQTemplate redisMQTemplate;

 @SneakyThrows
 protected AbstractRedisStreamMessageListener() {
 this.messageType = getMessageClass();
 this.streamKey = messageType.getDeclaredConstructor().newInstance().getStreamKey();
 }

 protected AbstractRedisStreamMessageListener(String streamKey, String group) {
 this.messageType = null;
 this.streamKey = streamKey;
 this.group = group;
 }

 @Override
 public void onMessage(ObjectRecord<String, String> message) {
        // Consume news
 T messageObj = JsonUtils.parseObject(message.getValue(), messageType);
 try {
 consumeMessageBefore(messageObj);
            // Consume news
 this.onMessage(messageObj);
            // ack message consumption completed
 redisMQTemplate.getRedisTemplate().opsForStream().acknowledge(group, message);
            // TODO Taro: The following points need to be additionally considered:
            // 1. Handle abnormal situations
            // 2. Sending logs; and combining transactions
            // 3. Consumption log; and general idempotence
            // 4. Retry for failed consumption, https://zhuanlan.zhihu.com/p/60501638
 } finally {
 consumeMessageAfter(messageObj);
 }
 }

 /**
     * Process messages
 *
     * @param message information
 */
 public abstract void onMessage(T message);

 /**
     * Get the message type by parsing the generics on the class
 *
     * @return Message type
 */
 @SuppressWarnings("unchecked")
 private Class<T> getMessageClass() {
 Type type = TypeUtil.getTypeArgument(getClass(), 0);
 if (type == null) {
            throw new IllegalStateException(String.format("Type (%s) requires setting the message type", getClass().getName()));
 }
 return (Class<T>) type;
 }

 private void consumeMessageBefore(AbstractRedisMessage message) {
 assert redisMQTemplate != null;
 List<RedisMessageInterceptor> interceptors = redisMQTemplate.getInterceptors();
        // positive sequence
 interceptors.forEach(interceptor -> interceptor.consumeMessageBefore(message));
 }

 private void consumeMessageAfter(AbstractRedisMessage message) {
 assert redisMQTemplate != null;
 List<RedisMessageInterceptor> interceptors = redisMQTemplate.getInterceptors();
        // reverse order
 for (int i = interceptors.size() - 1; i >= 0; i--) {
 interceptors.get(i).consumeMessageAfter(message);
 }
 }

}
