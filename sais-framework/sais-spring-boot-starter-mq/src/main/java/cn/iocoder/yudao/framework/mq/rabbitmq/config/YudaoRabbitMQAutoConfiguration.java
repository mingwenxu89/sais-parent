package cn.iocoder.yudao.framework.mq.rabbitmq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;

/**
 * RabbitMQ message queue configuration class
 *
 * @author Yudao Source Code
 */
@AutoConfiguration
@Slf4j
@ConditionalOnClass(name = "org.springframework.amqp.rabbit.core.RabbitTemplate")
public class YudaoRabbitMQAutoConfiguration {

 /**
     * Jackson2JSONMessageConverter Bean: Serialize messages using jackson
 */
 @Bean
 public MessageConverter createMessageConverter() {
 return new Jackson2JsonMessageConverter();
 }

}
