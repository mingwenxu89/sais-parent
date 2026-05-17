package cn.iocoder.yudao.framework.websocket.config;

import cn.iocoder.yudao.framework.mq.redis.config.YudaoRedisMQConsumerAutoConfiguration;
import cn.iocoder.yudao.framework.mq.redis.core.RedisMQTemplate;
import cn.iocoder.yudao.framework.websocket.core.handler.JsonWebSocketMessageHandler;
import cn.iocoder.yudao.framework.websocket.core.listener.WebSocketMessageListener;
import cn.iocoder.yudao.framework.websocket.core.security.LoginUserHandshakeInterceptor;
import cn.iocoder.yudao.framework.websocket.core.security.WebSocketAuthorizeRequestsCustomizer;
import cn.iocoder.yudao.framework.websocket.core.sender.kafka.KafkaWebSocketMessageConsumer;
import cn.iocoder.yudao.framework.websocket.core.sender.kafka.KafkaWebSocketMessageSender;
import cn.iocoder.yudao.framework.websocket.core.sender.local.LocalWebSocketMessageSender;
import cn.iocoder.yudao.framework.websocket.core.sender.rabbitmq.RabbitMQWebSocketMessageConsumer;
import cn.iocoder.yudao.framework.websocket.core.sender.rabbitmq.RabbitMQWebSocketMessageSender;
import cn.iocoder.yudao.framework.websocket.core.sender.redis.RedisWebSocketMessageConsumer;
import cn.iocoder.yudao.framework.websocket.core.sender.redis.RedisWebSocketMessageSender;
import cn.iocoder.yudao.framework.websocket.core.sender.rocketmq.RocketMQWebSocketMessageConsumer;
import cn.iocoder.yudao.framework.websocket.core.sender.rocketmq.RocketMQWebSocketMessageSender;
import cn.iocoder.yudao.framework.websocket.core.session.WebSocketSessionHandlerDecorator;
import cn.iocoder.yudao.framework.websocket.core.session.WebSocketSessionManager;
import cn.iocoder.yudao.framework.websocket.core.session.WebSocketSessionManagerImpl;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.List;

/**
 * WebSocket auto-configuration
 *
 * @author xingyu4j
 */
@AutoConfiguration(before = YudaoRedisMQConsumerAutoConfiguration.class) // before YudaoRedisMQConsumerAutoConfiguration The reason is that RedisWebSocketMessageConsumer needs to be created first before RedisMessageListenerContainer can be created
@EnableWebSocket // Turn on WebSocket
@ConditionalOnProperty(prefix = "yudao.websocket", value = "enable", matchIfMissing = true) // Allow using yudao.WebSocket.enable=false to disable WebSocket
@EnableConfigurationProperties(WebSocketProperties.class)
public class YudaoWebSocketAutoConfiguration {

 @Bean
 public WebSocketConfigurer webSocketConfigurer(HandshakeInterceptor[] handshakeInterceptors,
 WebSocketHandler webSocketHandler,
 WebSocketProperties webSocketProperties) {
 return registry -> registry
                // Add WebSocketHandler
.addHandler(webSocketHandler, webSocketProperties.getPath())
.addInterceptors(handshakeInterceptors)
                // Cross-domain is allowed, otherwise the front-end connection will be disconnected directly
.setAllowedOriginPatterns("*");
 }

 @Bean
 public HandshakeInterceptor handshakeInterceptor() {
 return new LoginUserHandshakeInterceptor();
 }

 @Bean
 public WebSocketHandler webSocketHandler(WebSocketSessionManager sessionManager,
 List<? extends WebSocketMessageListener<?>> messageListeners) {
        // 1. Create a JSONWebSocketMessageHandler object and process the message
 JsonWebSocketMessageHandler messageHandler = new JsonWebSocketMessageHandler(messageListeners);
        // 2. Create a WebSocketSessionHandlerDecorator object and handle the connection
 return new WebSocketSessionHandlerDecorator(messageHandler, sessionManager);
 }

 @Bean
 public WebSocketSessionManager webSocketSessionManager() {
 return new WebSocketSessionManagerImpl();
 }

 @Bean
 public WebSocketAuthorizeRequestsCustomizer webSocketAuthorizeRequestsCustomizer(WebSocketProperties webSocketProperties) {
 return new WebSocketAuthorizeRequestsCustomizer(webSocketProperties);
 }

    // ==================== Sender related ====================

 @Configuration
 @ConditionalOnProperty(prefix = "yudao.websocket", name = "sender-type", havingValue = "local")
 public class LocalWebSocketMessageSenderConfiguration {

 @Bean
 public LocalWebSocketMessageSender localWebSocketMessageSender(WebSocketSessionManager sessionManager) {
 return new LocalWebSocketMessageSender(sessionManager);
 }

 }

 @Configuration
 @ConditionalOnProperty(prefix = "yudao.websocket", name = "sender-type", havingValue = "redis")
 public class RedisWebSocketMessageSenderConfiguration {

 @Bean
 public RedisWebSocketMessageSender redisWebSocketMessageSender(WebSocketSessionManager sessionManager,
 RedisMQTemplate redisMQTemplate) {
 return new RedisWebSocketMessageSender(sessionManager, redisMQTemplate);
 }

 @Bean
 public RedisWebSocketMessageConsumer redisWebSocketMessageConsumer(
 RedisWebSocketMessageSender redisWebSocketMessageSender) {
 return new RedisWebSocketMessageConsumer(redisWebSocketMessageSender);
 }

 }

 @Configuration
 @ConditionalOnProperty(prefix = "yudao.websocket", name = "sender-type", havingValue = "rocketmq")
 public class RocketMQWebSocketMessageSenderConfiguration {

 @Bean
 public RocketMQWebSocketMessageSender rocketMQWebSocketMessageSender(
 WebSocketSessionManager sessionManager, RocketMQTemplate rocketMQTemplate,
 @Value("${yudao.websocket.sender-rocketmq.topic}") String topic) {
 return new RocketMQWebSocketMessageSender(sessionManager, rocketMQTemplate, topic);
 }

 @Bean
 public RocketMQWebSocketMessageConsumer rocketMQWebSocketMessageConsumer(
 RocketMQWebSocketMessageSender rocketMQWebSocketMessageSender) {
 return new RocketMQWebSocketMessageConsumer(rocketMQWebSocketMessageSender);
 }

 }

 @Configuration
 @ConditionalOnProperty(prefix = "yudao.websocket", name = "sender-type", havingValue = "rabbitmq")
 public class RabbitMQWebSocketMessageSenderConfiguration {

 @Bean
 public RabbitMQWebSocketMessageSender rabbitMQWebSocketMessageSender(
 WebSocketSessionManager sessionManager, RabbitTemplate rabbitTemplate,
 TopicExchange websocketTopicExchange) {
 return new RabbitMQWebSocketMessageSender(sessionManager, rabbitTemplate, websocketTopicExchange);
 }

 @Bean
 public RabbitMQWebSocketMessageConsumer rabbitMQWebSocketMessageConsumer(
 RabbitMQWebSocketMessageSender rabbitMQWebSocketMessageSender) {
 return new RabbitMQWebSocketMessageConsumer(rabbitMQWebSocketMessageSender);
 }

 /**
         * Create Topic Exchange
 */
 @Bean
 public TopicExchange websocketTopicExchange(@Value("${yudao.websocket.sender-rabbitmq.exchange}") String exchange) {
 return new TopicExchange(exchange,
                    true,  // durable: whether it is durable
                    false);  // exclusive: whether to be exclusive
 }

 }

 @Configuration
 @ConditionalOnProperty(prefix = "yudao.websocket", name = "sender-type", havingValue = "kafka")
 public class KafkaWebSocketMessageSenderConfiguration {

 @Bean
 public KafkaWebSocketMessageSender kafkaWebSocketMessageSender(
 WebSocketSessionManager sessionManager, KafkaTemplate<Object, Object> kafkaTemplate,
 @Value("${yudao.websocket.sender-kafka.topic}") String topic) {
 return new KafkaWebSocketMessageSender(sessionManager, kafkaTemplate, topic);
 }

 @Bean
 public KafkaWebSocketMessageConsumer kafkaWebSocketMessageConsumer(
 KafkaWebSocketMessageSender kafkaWebSocketMessageSender) {
 return new KafkaWebSocketMessageConsumer(kafkaWebSocketMessageSender);
 }

 }

}