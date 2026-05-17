package cn.iocoder.yudao.framework.tenant.core.mq.redis;

import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.mq.redis.core.interceptor.RedisMessageInterceptor;
import cn.iocoder.yudao.framework.mq.redis.core.message.AbstractRedisMessage;
import cn.iocoder.yudao.framework.tenant.core.context.TenantContextHolder;

import static cn.iocoder.yudao.framework.web.core.util.WebFrameworkUtils.HEADER_TENANT_ID;

/**
 * Multi-tenant {@link AbstractRedisMessage} interceptor
 *
 * 1. When the Producer sends a message, add the {@link TenantContextHolder} tenant ID to the Header of the message.
 * 2. When Consumer consumes a message, add the tenant ID of the header of the message to {@link TenantContextHolder}
 *
 * @author Yudao Source Code
 */
public class TenantRedisMessageInterceptor implements RedisMessageInterceptor {

 @Override
 public void sendMessageBefore(AbstractRedisMessage message) {
 Long tenantId = TenantContextHolder.getTenantId();
 if (tenantId != null) {
 message.addHeader(HEADER_TENANT_ID, tenantId.toString());
 }
 }

 @Override
 public void consumeMessageBefore(AbstractRedisMessage message) {
 String tenantIdStr = message.getHeader(HEADER_TENANT_ID);
 if (StrUtil.isNotEmpty(tenantIdStr)) {
 TenantContextHolder.setTenantId(Long.valueOf(tenantIdStr));
 }
 }

 @Override
 public void consumeMessageAfter(AbstractRedisMessage message) {
        // Note that Consumer is a logical entry, so the tenant ID exists in the original context regardless of the original context.
 TenantContextHolder.clear();
 }

}
