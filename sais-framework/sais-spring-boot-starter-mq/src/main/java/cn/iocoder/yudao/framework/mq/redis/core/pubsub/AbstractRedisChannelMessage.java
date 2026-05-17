package cn.iocoder.yudao.framework.mq.redis.core.pubsub;

import cn.iocoder.yudao.framework.mq.redis.core.message.AbstractRedisMessage;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Redis Channel Message abstract class
 *
 * @author Yudao Source Code
 */
public abstract class AbstractRedisChannelMessage extends AbstractRedisMessage {

 /**
     * Get the Redis Channel, using the class name by default
 *
 * @return Channel
 */
    @JsonIgnore // AvoID serialization. The reason is that when Redis publishes Channel messages, it has already been specified.
 public String getChannel() {
 return getClass().getSimpleName();
 }

}
