package cn.iocoder.yudao.framework.mq.redis.config;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.system.SystemUtil;
import cn.iocoder.yudao.framework.common.enums.DocumentEnum;
import cn.iocoder.yudao.framework.mq.redis.core.RedisMQTemplate;
import cn.iocoder.yudao.framework.mq.redis.core.job.RedisPendingMessageResendJob;
import cn.iocoder.yudao.framework.mq.redis.core.job.RedisStreamMessageCleanupJob;
import cn.iocoder.yudao.framework.mq.redis.core.pubsub.AbstractRedisChannelMessageListener;
import cn.iocoder.yudao.framework.mq.redis.core.stream.AbstractRedisStreamMessageListener;
import cn.iocoder.yudao.framework.redis.config.YudaoRedisAutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.List;
import java.util.Properties;

/**
 * Redis message queue Consumer configuration class
 *
 * @author Yudao Source Code
 */
@Slf4j
@EnableScheduling // Enable scheduled tasks for RedisPendingMessageResendJob to resend messages
@AutoConfiguration(after = YudaoRedisAutoConfiguration.class)
public class YudaoRedisMQConsumerAutoConfiguration {

 /**
     * Create a container for Redis Pub/Sub broadcast consumption
 */
 @Bean
    @ConditionalOnBean(AbstractRedisChannelMessageListener.class) // Only when AbstractChannelMessageListener exists, you need to register Redis pubsub listener
 public RedisMessageListenerContainer redisMessageListenerContainer(
 RedisMQTemplate redisMQTemplate, List<AbstractRedisChannelMessageListener<?>> listeners) {
        // Create a RedisMessageListenerContainer object
 RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        // Set up the RedisConnection factory.
 container.setConnectionFactory(redisMQTemplate.getRedisTemplate().getRequiredConnectionFactory());
        // Add listener
 listeners.forEach(listener -> {
 listener.setRedisMQTemplate(redisMQTemplate);
 container.addMessageListener(listener, new ChannelTopic(listener.getChannel()));
            log.info("[RedisMessageListenerContainer][Register the listener ({}) corresponding to Channel({})]",
 listener.getChannel(), listener.getClass().getName());
 });
 return container;
 }

 /**
     * Create a Redis Stream re-consumer task
 */
 @Bean
    @ConditionalOnBean(AbstractRedisStreamMessageListener.class) // Only when AbstractStreamMessageListener exists, you need to register the Redis pubsub listener.
 public RedisPendingMessageResendJob redisPendingMessageResendJob(List<AbstractRedisStreamMessageListener<?>> listeners,
 RedisMQTemplate redisTemplate,
 RedissonClient redissonClient) {
 return new RedisPendingMessageResendJob(listeners, redisTemplate, redissonClient);
 }

 /**
     * Create a Redis Stream message cleaning task
 */
 @Bean
 @ConditionalOnBean(AbstractRedisStreamMessageListener.class)
 public RedisStreamMessageCleanupJob redisStreamMessageCleanupJob(List<AbstractRedisStreamMessageListener<?>> listeners,
 RedisMQTemplate redisTemplate,
 RedissonClient redissonClient) {
 return new RedisStreamMessageCleanupJob(listeners, redisTemplate, redissonClient);
 }

 /**
     * Create a container for Redis Stream cluster consumption
 *
     * Basic knowledge: <a href="https://www.geek-book.com/src/docs/Redis/Redis/Redis.io/commands/xreadgroup.html">Redis Stream’s xreadgroup command</a>
 */
 @Bean(initMethod = "start", destroyMethod = "stop")
    @ConditionalOnBean(AbstractRedisStreamMessageListener.class) // Only when AbstractStreamMessageListener exists, you need to register the Redis pubsub listener.
 public StreamMessageListenerContainer<String, ObjectRecord<String, String>> redisStreamMessageListenerContainer(
 RedisMQTemplate redisMQTemplate, List<AbstractRedisStreamMessageListener<?>> listeners) {
 RedisTemplate<String, ?> redisTemplate = redisMQTemplate.getRedisTemplate();
 checkRedisVersion(redisTemplate);
        // The first step is to create the StreamMessageListenerContainer container
        // Create options configuration
 StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, ObjectRecord<String, String>> containerOptions =
 StreamMessageListenerContainer.StreamMessageListenerContainerOptions.builder()
                        .batchSize(10) // How many messages can be pulled at most at one time?
                        .targetType(String.class) // Target type. Use String uniformly and deserialize through its own encapsulated AbstractStreamMessageListener
.build();
        // Create container object
 StreamMessageListenerContainer<String, ObjectRecord<String, String>> container =
 StreamMessageListenerContainer.create(redisMQTemplate.getRedisTemplate().getRequiredConnectionFactory(), containerOptions);

        // The second step is to register the listener and consume the corresponding Stream topic.
 String consumerName = buildConsumerName();
 listeners.parallelStream().forEach(listener -> {
            log.info("[RedisStreamMessageListenerContainer][Start registering the listener ({}) corresponding to StreamKey({})]",
 listener.getStreamKey(), listener.getClass().getName());
            // Create a consumer group corresponding to listener
 try {
 redisTemplate.opsForStream().createGroup(listener.getStreamKey(), listener.getGroup());
 } catch (Exception ignore) {
 }
            // Set the RedisTemplate corresponding to listener
 listener.setRedisMQTemplate(redisMQTemplate);
            // Create Consumer object
 Consumer consumer = Consumer.from(listener.getGroup(), consumerName);
            // Set the Consumer consumption progress, subject to the minimum consumption progress
 StreamOffset<String> streamOffset = StreamOffset.create(listener.getStreamKey(), ReadOffset.lastConsumed());
            // Set up Consumer monitoring
 StreamMessageListenerContainer.StreamReadRequestBuilder<String> builder = StreamMessageListenerContainer.StreamReadRequest
.builder(streamOffset).consumer(consumer)
                    .autoAcknowledge(false) // No automatic ack
                    .cancelOnError(throwable -> false); // The default configuration cancels consumption when an exception occurs, which obviously does not meet expectations; therefore, we set it to false
 container.register(builder.build(), listener);
            log.info("[RedisStreamMessageListenerContainer][Complete registration of the listener ({}) corresponding to StreamKey({})]",
 listener.getStreamKey(), listener.getClass().getName());
 });
 return container;
 }

 /**
     * Construct the consumer name using the local IP + process number.
     * Refer to the implementation of RocketMQ clientId
 *
     * @return Consumer name
 */
 public static String buildConsumerName() {
 return String.format("%s@%d", SystemUtil.getHostInfo().getAddress(), SystemUtil.getCurrentPID());
 }

 /**
     * Verify the Redis version number to see if it meets the minimum version number requirements!
 */
 public static void checkRedisVersion(RedisTemplate<String, ?> redisTemplate) {
        // Get the Redis version
 Properties info = redisTemplate.execute((RedisCallback<Properties>) RedisServerCommands::info);
 String version = MapUtil.getStr(info, "redis_version");
        // The minimum version to be verified must be greater than or equal to 5.0.0
 int majorVersion = Integer.parseInt(StrUtil.subBefore(version, '.', false));
 if (majorVersion < 5) {
            throw new IllegalStateException(StrUtil.format("Your current Redis version is {}, which is less than the minimum required version 5.0.0!" +
                    "Please refer to the {} document for installation.", version, DocumentEnum.REDIS_INSTALL.getUrl()));
 }
 }

}
