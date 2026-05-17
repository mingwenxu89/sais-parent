package cn.iocoder.yudao.framework.tenant.core.mq.kafka;

import cn.hutool.core.util.ReflectUtil;
import cn.iocoder.yudao.framework.tenant.core.context.TenantContextHolder;
import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.header.Headers;
import org.springframework.messaging.handler.invocation.InvocableHandlerMethod;

import java.util.Map;

import static cn.iocoder.yudao.framework.web.core.util.WebFrameworkUtils.HEADER_TENANT_ID;

/**
 * Multi-tenant {@link ProducerInterceptor} implementation class for Kafka message queue
 *
 * 1. When the Producer sends a message, add the {@link TenantContextHolder} tenant ID to the Header of the message.
 * 2. When Consumer consumes a message, add the tenant ID of the message Header to {@link TenantContextHolder} and implement it through {@link InvocableHandlerMethod}
 *
 * @author Yudao Source Code
 */
public class TenantKafkaProducerInterceptor implements ProducerInterceptor<Object, Object> {

 @Override
 public ProducerRecord<Object, Object> onSend(ProducerRecord<Object, Object> record) {
 Long tenantId = TenantContextHolder.getTenantId();
 if (tenantId != null) {
            Headers headers = (Headers) ReflectUtil.getFieldValue(record, "headers"); // private property, no get method, smart reflection
 headers.add(HEADER_TENANT_ID, tenantId.toString().getBytes());
 }
 return record;
 }

 @Override
 public void onAcknowledgement(RecordMetadata metadata, Exception exception) {
 }

 @Override
 public void close() {
 }

 @Override
 public void configure(Map<String, ?> configs) {
 }

}
