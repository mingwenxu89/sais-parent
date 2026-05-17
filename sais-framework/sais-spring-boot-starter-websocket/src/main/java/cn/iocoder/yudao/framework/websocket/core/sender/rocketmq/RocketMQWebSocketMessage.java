package cn.iocoder.yudao.framework.websocket.core.sender.rocketmq;

import lombok.Data;

/**
 * RocketMQ broadcasts WebSocket messages
 *
 * @author Yudao Source Code
 */
@Data
public class RocketMQWebSocketMessage {

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
