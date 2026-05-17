package cn.iocoder.yudao.module.infra.api.websocket;

import cn.iocoder.yudao.framework.websocket.core.sender.WebSocketMessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * API implementation class for WebSocket sender
 *
 * @author Yudao Source Code
 */
@Component
public class WebSocketSenderApiImpl implements WebSocketSenderApi {

    @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
    @Autowired(required = false) // Since the yudao.WebSocket.enable configuration item can turn off the WebSocket function, we can only force the injection here.
    private WebSocketMessageSender webSocketMessageSender;

    @Override
    public void send(Integer userType, Long userId, String messageType, String messageContent) {
        webSocketMessageSender.send(userType, userId, messageType, messageContent);
    }

    @Override
    public void send(Integer userType, String messageType, String messageContent) {
        webSocketMessageSender.send(userType, messageType, messageContent);
    }

    @Override
    public void send(String sessionId, String messageType, String messageContent) {
        webSocketMessageSender.send(sessionId, messageType, messageContent);
    }

}
