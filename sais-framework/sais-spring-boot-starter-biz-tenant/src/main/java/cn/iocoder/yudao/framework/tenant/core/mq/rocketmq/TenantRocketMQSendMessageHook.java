package cn.iocoder.yudao.framework.tenant.core.mq.rocketmq;

import cn.iocoder.yudao.framework.tenant.core.context.TenantContextHolder;
import org.apache.rocketmq.client.hook.SendMessageContext;
import org.apache.rocketmq.client.hook.SendMessageHook;

import static cn.iocoder.yudao.framework.web.core.util.WebFrameworkUtils.HEADER_TENANT_ID;

/**
 * Multi-tenant {@link SendMessageHook} implementation class of RocketMQ message queue
 *
 * When the Producer sends a message, it adds the {@link TenantContextHolder} tenant ID to the Header of the message.
 *
 * @author Yudao Source Code
 */
public class TenantRocketMQSendMessageHook implements SendMessageHook {

 @Override
 public String hookName() {
 return getClass().getSimpleName();
 }

 @Override
 public void sendMessageBefore(SendMessageContext sendMessageContext) {
 Long tenantId = TenantContextHolder.getTenantId();
 if (tenantId == null) {
 return;
 }
 sendMessageContext.getMessage().putUserProperty(HEADER_TENANT_ID, tenantId.toString());
 }

 @Override
 public void sendMessageAfter(SendMessageContext sendMessageContext) {
 }

}
