package cn.iocoder.yudao.framework.websocket.core.sender.kafka;

import lombok.Data;

/**
 * Kafka broadcasts WebSocket messages
 *
 * @author Yudao Source Code
 */
@Data
public class KafkaWebSocketMessage {

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
