package cn.iocoder.yudao.framework.websocket.core.listener;

import cn.iocoder.yudao.framework.websocket.core.message.JsonWebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * WebSocket message listener interface
 *
 * Purpose: After the front end sends a message to the back end, it processes the message corresponding to the {@link #getType()} type.
 *
 * @param <T> Generic, message type
 */
public interface WebSocketMessageListener<T> {

 /**
     * Process messages
 *
 * @param session Session
     * @param message information
 */
 void onMessage(WebSocketSession session, T message);

 /**
     * Get message type
 *
 * @see JsonWebSocketMessage#getType()
     * @return Message type
 */
 String getType();

}
