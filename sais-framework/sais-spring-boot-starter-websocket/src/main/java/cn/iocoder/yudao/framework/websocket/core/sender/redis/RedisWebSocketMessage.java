package cn.iocoder.yudao.framework.websocket.core.sender.redis;

import cn.iocoder.yudao.framework.mq.redis.core.pubsub.AbstractRedisChannelMessage;
import lombok.Data;

/**
 * Redis broadcasts WebSocket messages
 */
@Data
public class RedisWebSocketMessage extends AbstractRedisChannelMessage {

 /**
     * Session number
 */
 private String sessionId;
 /**
     * User type
 */
 private Integer userType;
 /**
     * User ID
 */
 private Long userId;

 /**
     * Message type
 */
 private String messageType;
 /**
     * Message content
 */
 private String messageContent;

}
