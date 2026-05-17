package cn.iocoder.yudao.framework.websocket.core.session;

import org.springframework.web.socket.WebSocketSession;

import java.util.Collection;

/**
 * {@link WebSocketSession} manager interface
 *
 * @author Yudao Source Code
 */
public interface WebSocketSessionManager {

 /**
     * Add session
 *
 * @param session Session
 */
 void addSession(WebSocketSession session);

 /**
     * Remove Session
 *
 * @param session Session
 */
 void removeSession(WebSocketSession session);

 /**
     * Get the Session with the specified number
 *
     * @param id Session number
 * @return Session
 */
 WebSocketSession getSession(String id);

 /**
     * Get the Session list of the specified user type
 *
     * @param userType User type
     * @return Session list
 */
 Collection<WebSocketSession> getSessionList(Integer userType);

 /**
     * Get the Session list of the specified user ID
 *
     * @param userType User type
     * @param userId User ID
     * @return Session list
 */
 Collection<WebSocketSession> getSessionList(Integer userType, Long userId);

}