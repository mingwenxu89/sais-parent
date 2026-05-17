package cn.iocoder.yudao.framework.websocket.core.message;

import cn.iocoder.yudao.framework.websocket.core.listener.WebSocketMessageListener;
import lombok.Data;

import java.io.Serializable;

/**
 * WebSocket message frame in JSON format
 *
 * @author Yudao Source Code
 */
@Data
public class JsonWebSocketMessage implements Serializable {

 /**
     * Message type
 *
     * Purpose: used to distribute to the corresponding {@link WebSocketMessageListener} implementation class
 */
 private String type;
 /**
     * Message content
 *
     * Request JSON object
 */
 private String content;

}
