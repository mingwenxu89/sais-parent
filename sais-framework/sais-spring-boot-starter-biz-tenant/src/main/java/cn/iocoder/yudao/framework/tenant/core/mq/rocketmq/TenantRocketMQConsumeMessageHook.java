package cn.iocoder.yudao.framework.tenant.core.mq.rocketmq;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.tenant.core.context.TenantContextHolder;
import org.apache.rocketmq.client.hook.ConsumeMessageContext;
import org.apache.rocketmq.client.hook.ConsumeMessageHook;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.messaging.handler.invocation.InvocableHandlerMethod;

import java.util.List;

import static cn.iocoder.yudao.framework.web.core.util.WebFrameworkUtils.HEADER_TENANT_ID;

/**
 * Multi-tenant {@link ConsumeMessageHook} implementation class of RocketMQ message queue
 *
 * When Consumer consumes a message, it adds the tenant ID of the header of the message to {@link TenantContextHolder} and implements it through {@link InvocableHandlerMethod}
 *
 * @author Yudao Source Code
 */
public class TenantRocketMQConsumeMessageHook implements ConsumeMessageHook {

 @Override
 public String hookName() {
 return getClass().getSimpleName();
 }

 @Override
 public void consumeMessageBefore(ConsumeMessageContext context) {
        // Verification, the message must be a single message, otherwise the tenant setting may be incorrect.
 List<MessageExt> messages = context.getMsgList();
        Assert.isTrue(messages.size() == 1, "message count ({}) is incorrect", messages.size());
        // Set tenant ID
 String tenantId = messages.get(0).getUserProperty(HEADER_TENANT_ID);
 if (StrUtil.isNotEmpty(tenantId)) {
 TenantContextHolder.setTenantId(Long.parseLong(tenantId));
 }
 }

 @Override
 public void consumeMessageAfter(ConsumeMessageContext context) {
 TenantContextHolder.clear();
 }

}
