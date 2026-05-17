package cn.iocoder.yudao.framework.websocket.core.sender.rabbitmq;

import lombok.Data;

import java.io.Serializable;

/**
 * RabbitMQ broadcasts WebSocket messages
 *
 * @author Yudao Source Code
 */
@Data
public class RabbitMQWebSocketMessage implements Serializable {

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
