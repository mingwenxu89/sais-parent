package cn.iocoder.yudao.framework.websocket.core.session;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;

/**
 * The decoration class of {@link WebSocketHandler} implements the following functions:
 *
 * 1. Use {@link #sessionManager} to manage when {@link WebSocketSession} is connected or closed.
 * 2. Encapsulate {@link WebSocketSession} to support concurrent operations
 *
 * @author Yudao Source Code
 */
public class WebSocketSessionHandlerDecorator extends WebSocketHandlerDecorator {

 /**
     * Sending time limit, unit: milliseconds
 */
 private static final Integer SEND_TIME_LIMIT = 1000 * 5;
 /**
     * Send message buffer online, unit: bytes
 */
 private static final Integer BUFFER_SIZE_LIMIT = 1024 * 100;

 private final WebSocketSessionManager sessionManager;

 public WebSocketSessionHandlerDecorator(WebSocketHandler delegate,
 WebSocketSessionManager sessionManager) {
 super(delegate);
 this.sessionManager = sessionManager;
 }

 @Override
 public void afterConnectionEstablished(WebSocketSession session) {
        // To implement session support for concurrency, please refer to https://blog.csdn.net/abu935009066/article/details/131218149
 session = new ConcurrentWebSocketSessionDecorator(session, SEND_TIME_LIMIT, BUFFER_SIZE_LIMIT);
        // Add to WebSocketSessionManager
 sessionManager.addSession(session);
 }

 @Override
 public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) {
 sessionManager.removeSession(session);
 }

}
