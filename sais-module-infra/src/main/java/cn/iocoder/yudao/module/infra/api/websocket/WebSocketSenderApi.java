package cn.iocoder.yudao.module.infra.api.websocket;

import cn.iocoder.yudao.framework.common.util.json.JsonUtils;

/**
 * WebSocket sender API API
 *
 * Encapsulate WebSocketMessageSender and provide it for use by other modules
 *
 * @author Yudao Source Code
 */
public interface WebSocketSenderApi {

    /**
     * Send message to specified user
     *
     * @param userType User type
     * @param userId User ID
     * @param messageType Message type
     * @param messageContent Message content, JSON format
     */
    void send(Integer userType, Long userId, String messageType, String messageContent);

    /**
     * Send message to specified user type
     *
     * @param userType User type
     * @param messageType Message type
     * @param messageContent Message content, JSON format
     */
    void send(Integer userType, String messageType, String messageContent);

    /**
     * Send a message to the specified Session
     *
     * @param sessionId Session ID
     * @param messageType Message type
     * @param messageContent Message content, JSON format
     */
    void send(String sessionId, String messageType, String messageContent);

    default void sendObject(Integer userType, Long userId, String messageType, Object messageContent) {
        send(userType, userId, messageType, JsonUtils.toJsonString(messageContent));
    }

    default void sendObject(Integer userType, String messageType, Object messageContent) {
        send(userType, messageType, JsonUtils.toJsonString(messageContent));
    }

    default void sendObject(String sessionId, String messageType, Object messageContent) {
        send(sessionId, messageType, JsonUtils.toJsonString(messageContent));
    }

}
