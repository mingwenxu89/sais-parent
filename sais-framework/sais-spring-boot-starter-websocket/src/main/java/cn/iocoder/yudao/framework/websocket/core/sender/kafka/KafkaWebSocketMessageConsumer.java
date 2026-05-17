package cn.iocoder.yudao.framework.websocket.core.sender.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.kafka.annotation.KafkaListener;

/**
 * {@link KafkaWebSocketMessage} is the consumer of the broadcast message and actually sends the message out
 *
 * @author Yudao Source Code
 */
@RequiredArgsConstructor
public class KafkaWebSocketMessageConsumer {

 private final KafkaWebSocketMessageSender kafkaWebSocketMessageSender;

 @RabbitHandler
 @KafkaListener(
 topics = "${yudao.websocket.sender-kafka.topic}",
            // On a Group, use the UUID to generate its suffix. In this way, the Group of the started Consumer is different to achieve the purpose of broadcast consumption.
 groupId = "${yudao.websocket.sender-kafka.consumer-group}" + "-" + "#{T(java.util.UUID).randomUUID()}")
 public void onMessage(KafkaWebSocketMessage message) {
 kafkaWebSocketMessageSender.send(message.getSessionId(),
 message.getUserType(), message.getUserId(),
 message.getMessageType(), message.getMessageContent());
 }

}
