package cn.iocoder.yudao.framework.websocket.core.sender.rocketmq;

import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;

/**
 * {@link RocketMQWebSocketMessage} is the consumer of broadcast messages and actually sends the messages out
 *
 * @author Yudao Source Code
 */
@RocketMQMessageListener( // Key point: Add the @RocketMQMessageListener annotation and declare the consumed topic
 topic = "${yudao.websocket.sender-rocketmq.topic}",
 consumerGroup = "${yudao.websocket.sender-rocketmq.consumer-group}",
        messageModel = MessageModel.BROADCASTING // Set to broadcast mode to ensure that each instance can receive messages
)
@RequiredArgsConstructor
public class RocketMQWebSocketMessageConsumer implements RocketMQListener<RocketMQWebSocketMessage> {

 private final RocketMQWebSocketMessageSender rocketMQWebSocketMessageSender;

 @Override
 public void onMessage(RocketMQWebSocketMessage message) {
 rocketMQWebSocketMessageSender.send(message.getSessionId(),
 message.getUserType(), message.getUserId(),
 message.getMessageType(), message.getMessageContent());
 }

}
