package cn.iocoder.yudao.framework.websocket.core.sender.redis;

import cn.iocoder.yudao.framework.mq.redis.core.RedisMQTemplate;
import cn.iocoder.yudao.framework.websocket.core.sender.AbstractWebSocketMessageSender;
import cn.iocoder.yudao.framework.websocket.core.sender.WebSocketMessageSender;
import cn.iocoder.yudao.framework.websocket.core.session.WebSocketSessionManager;
import lombok.extern.slf4j.Slf4j;

/**
 * Redis-based {@link WebSocketMessageSender} implementation class
 *
 * @author Yudao Source Code
 */
@Slf4j
public class RedisWebSocketMessageSender extends AbstractWebSocketMessageSender {

 private final RedisMQTemplate redisMQTemplate;

 public RedisWebSocketMessageSender(WebSocketSessionManager sessionManager,
 RedisMQTemplate redisMQTemplate) {
 super(sessionManager);
 this.redisMQTemplate = redisMQTemplate;
 }

 @Override
 public void send(Integer userType, Long userId, String messageType, String messageContent) {
 sendRedisMessage(null, userId, userType, messageType, messageContent);
 }

 @Override
 public void send(Integer userType, String messageType, String messageContent) {
 sendRedisMessage(null, null, userType, messageType, messageContent);
 }

 @Override
 public void send(String sessionId, String messageType, String messageContent) {
 sendRedisMessage(sessionId, null, null, messageType, messageContent);
 }

 /**
     * Broadcast messages via Redis
 *
     * @param sessionId Session number
     * @param userId User ID
     * @param userType User type
     * @param messageType Message type
     * @param messageContent Message content
 */
 private void sendRedisMessage(String sessionId, Long userId, Integer userType,
 String messageType, String messageContent) {
 RedisWebSocketMessage mqMessage = new RedisWebSocketMessage()
.setSessionId(sessionId).setUserId(userId).setUserType(userType)
.setMessageType(messageType).setMessageContent(messageContent);
 redisMQTemplate.send(mqMessage);
 }

}
