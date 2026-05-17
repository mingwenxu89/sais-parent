package cn.iocoder.yudao.framework.mq.redis.core.interceptor;

import cn.iocoder.yudao.framework.mq.redis.core.message.AbstractRedisMessage;

/**
 * {@link AbstractRedisMessage} message interceptor
 * Expansion is achieved through interceptors as a plug-in mechanism.
 * For example, MQ message processing in multi-tenant scenarios
 *
 * @author Yudao Source Code
 */
public interface RedisMessageInterceptor {

 default void sendMessageBefore(AbstractRedisMessage message) {
 }

 default void sendMessageAfter(AbstractRedisMessage message) {
 }

 default void consumeMessageBefore(AbstractRedisMessage message) {
 }

 default void consumeMessageAfter(AbstractRedisMessage message) {
 }

}
