package cn.iocoder.yudao.framework.mq.redis.core.pubsub;

import cn.hutool.core.util.TypeUtil;
import cn.iocoder.yudao.framework.common.util.json.JsonUtils;
import cn.iocoder.yudao.framework.mq.redis.core.RedisMQTemplate;
import cn.iocoder.yudao.framework.mq.redis.core.interceptor.RedisMessageInterceptor;
import cn.iocoder.yudao.framework.mq.redis.core.message.AbstractRedisMessage;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Redis Pub/Sub listener abstract class, used to implement broadcast consumption
 *
 * @param <T> Message type. Be sure to fill it out, otherwise an error will be reported
 *
 * @author Yudao Source Code
 */
public abstract class AbstractRedisChannelMessageListener<T extends AbstractRedisChannelMessage> implements MessageListener {

 /**
     * Message type
 */
 private final Class<T> messageType;
 /**
 * Redis Channel
 */
 private final String channel;
 /**
 * RedisMQTemplate
 */
 @Setter
 private RedisMQTemplate redisMQTemplate;

 @SneakyThrows
 protected AbstractRedisChannelMessageListener() {
 this.messageType = getMessageClass();
 this.channel = messageType.getDeclaredConstructor().newInstance().getChannel();
 }

 /**
     * Get the Redis Channel channel subscribed by Sub
 *
 * @return channel
 */
 public final String getChannel() {
 return channel;
 }

 @Override
 public final void onMessage(Message message, byte[] bytes) {
 T messageObj = JsonUtils.parseObject(message.getBody(), messageType);
 try {
 consumeMessageBefore(messageObj);
            // Consume news
 this.onMessage(messageObj);
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
