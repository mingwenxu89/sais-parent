package cn.iocoder.yudao.framework.mq.redis.core.stream;

import cn.iocoder.yudao.framework.mq.redis.core.message.AbstractRedisMessage;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Redis Stream Message abstract class
 *
 * @author Yudao Source Code
 */
public abstract class AbstractRedisStreamMessage extends AbstractRedisMessage {

 /**
     * Get the Redis Stream Key, using the class name by default
 *
 * @return Channel
 */
    @JsonIgnore // avoID serialization
 public String getStreamKey() {
 return getClass().getSimpleName();
 }

}
