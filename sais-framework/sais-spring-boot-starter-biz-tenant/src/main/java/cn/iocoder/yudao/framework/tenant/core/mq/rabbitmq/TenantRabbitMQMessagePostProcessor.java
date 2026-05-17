package cn.iocoder.yudao.framework.tenant.core.mq.rabbitmq;

import cn.iocoder.yudao.framework.tenant.core.context.TenantContextHolder;
import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.messaging.handler.invocation.InvocableHandlerMethod;

import static cn.iocoder.yudao.framework.web.core.util.WebFrameworkUtils.HEADER_TENANT_ID;

/**
 * Multi-tenant {@link ProducerInterceptor} implementation class for RabbitMQ message queue
 *
 * 1. When the Producer sends a message, add the {@link TenantContextHolder} tenant ID to the Header of the message.
 * 2. When Consumer consumes a message, add the tenant ID of the message Header to {@link TenantContextHolder} and implement it through {@link InvocableHandlerMethod}
 *
 * @author Yudao Source Code
 */
public class TenantRabbitMQMessagePostProcessor implements MessagePostProcessor {

 @Override
 public Message postProcessMessage(Message message) throws AmqpException {
 Long tenantId = TenantContextHolder.getTenantId();
 if (tenantId != null) {
 message.getMessageProperties().getHeaders().put(HEADER_TENANT_ID, tenantId);
 }
 return message;
 }

}
