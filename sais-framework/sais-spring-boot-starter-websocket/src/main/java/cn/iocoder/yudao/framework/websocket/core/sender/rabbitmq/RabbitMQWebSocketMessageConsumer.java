package cn.iocoder.yudao.framework.websocket.core.sender.rabbitmq;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;

/**
 * {@link RabbitMQWebSocketMessage} is the consumer of broadcast messages and actually sends the messages.
 *
 * @author Yudao Source Code
 */
@RabbitListener(
 bindings = @QueueBinding(
 value = @Queue(
                        // On the Queue name, use the UUID to generate its suffix. In this way, the Queue of the started Consumer is different to achieve the purpose of broadcast consumption.
 name = "${yudao.websocket.sender-rabbitmq.queue}" + "-" + "#{T(java.util.UUID).randomUUID()}",
                        // When the Consumer is closed, the queue can be automatically deleted.
 autoDelete = "true"
 ),
 exchange = @Exchange(
 name = "${yudao.websocket.sender-rabbitmq.exchange}",
 type = ExchangeTypes.TOPIC,
 declare = "false"
 )
 )
)
@RequiredArgsConstructor
public class RabbitMQWebSocketMessageConsumer {

 private final RabbitMQWebSocketMessageSender rabbitMQWebSocketMessageSender;

 @RabbitHandler
 public void onMessage(RabbitMQWebSocketMessage message) {
 rabbitMQWebSocketMessageSender.send(message.getSessionId(),
 message.getUserType(), message.getUserId(),
 message.getMessageType(), message.getMessageContent());
 }

}
