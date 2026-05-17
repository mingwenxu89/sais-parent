package cn.iocoder.yudao.framework.websocket.core.sender.redis;

import cn.iocoder.yudao.framework.mq.redis.core.pubsub.AbstractRedisChannelMessageListener;
import lombok.RequiredArgsConstructor;

/**
 * {@link RedisWebSocketMessage} is the consumer of broadcast messages and actually sends the messages.
 *
 * @author Yudao Source Code
 */
@RequiredArgsConstructor
public class RedisWebSocketMessageConsumer extends AbstractRedisChannelMessageListener<RedisWebSocketMessage> {

 private final RedisWebSocketMessageSender redisWebSocketMessageSender;

 @Override
 public void onMessage(RedisWebSocketMessage message) {
 redisWebSocketMessageSender.send(message.getSessionId(),
 message.getUserType(), message.getUserId(),
 message.getMessageType(), message.getMessageContent());
 }

}
