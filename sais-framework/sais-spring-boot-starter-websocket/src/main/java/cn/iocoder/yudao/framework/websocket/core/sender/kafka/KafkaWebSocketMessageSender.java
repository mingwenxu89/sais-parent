package cn.iocoder.yudao.framework.websocket.core.sender.kafka;

import cn.iocoder.yudao.framework.websocket.core.sender.AbstractWebSocketMessageSender;
import cn.iocoder.yudao.framework.websocket.core.sender.WebSocketMessageSender;
import cn.iocoder.yudao.framework.websocket.core.session.WebSocketSessionManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.concurrent.ExecutionException;

/**
 * Kafka-based {@link WebSocketMessageSender} implementation class
 *
 * @author Yudao Source Code
 */
@Slf4j
public class KafkaWebSocketMessageSender extends AbstractWebSocketMessageSender {

 private final KafkaTemplate<Object, Object> kafkaTemplate;

 private final String topic;

 public KafkaWebSocketMessageSender(WebSocketSessionManager sessionManager,
 KafkaTemplate<Object, Object> kafkaTemplate,
 String topic) {
 super(sessionManager);
 this.kafkaTemplate = kafkaTemplate;
 this.topic = topic;
 }

 @Override
 public void send(Integer userType, Long userId, String messageType, String messageContent) {
 sendKafkaMessage(null, userId, userType, messageType, messageContent);
 }

 @Override
 public void send(Integer userType, String messageType, String messageContent) {
 sendKafkaMessage(null, null, userType, messageType, messageContent);
 }

 @Override
 public void send(String sessionId, String messageType, String messageContent) {
 sendKafkaMessage(sessionId, null, null, messageType, messageContent);
 }

 /**
     * Broadcast messages through Kafka
 *
     * @param sessionId Session number
     * @param userId User ID
     * @param userType User type
     * @param messageType Message type
     * @param messageContent Message content
 */
 private void sendKafkaMessage(String sessionId, Long userId, Integer userType,
 String messageType, String messageContent) {
 KafkaWebSocketMessage mqMessage = new KafkaWebSocketMessage()
.setSessionId(sessionId).setUserId(userId).setUserType(userType)
.setMessageType(messageType).setMessageContent(messageContent);
 try {
 kafkaTemplate.send(topic, mqMessage).get();
 } catch (InterruptedException | ExecutionException e) {
            log.error("[sendKafkaMessage][Failed to send message ({}) to Kafka]", mqMessage, e);
 }
 }

}
