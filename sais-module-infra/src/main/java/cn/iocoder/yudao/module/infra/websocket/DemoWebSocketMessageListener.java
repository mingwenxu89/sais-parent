package cn.iocoder.yudao.module.infra.websocket;

import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.framework.websocket.core.listener.WebSocketMessageListener;
import cn.iocoder.yudao.framework.websocket.core.sender.WebSocketMessageSender;
import cn.iocoder.yudao.framework.websocket.core.util.WebSocketFrameworkUtils;
import cn.iocoder.yudao.module.infra.websocket.message.DemoReceiveMessage;
import cn.iocoder.yudao.module.infra.websocket.message.DemoSendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

/**
 * WebSocket example: single message
 *
 * @author Yudao Source Code
 */
@Component
public class DemoWebSocketMessageListener implements WebSocketMessageListener<DemoSendMessage> {

    @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
    @Autowired(required = false) // Since the yudao.WebSocket.enable configuration item can turn off the WebSocket function, we can only force the injection here.
    private WebSocketMessageSender webSocketMessageSender;

    @Override
    public void onMessage(WebSocketSession session, DemoSendMessage message) {
        Long fromUserId = WebSocketFrameworkUtils.getLoginUserId(session);
        // Situation 1: Single shot
        if (message.getToUserId() != null) {
            DemoReceiveMessage toMessage = new DemoReceiveMessage().setFromUserId(fromUserId)
                    .setText(message.getText()).setSingle(true);
            webSocketMessageSender.sendObject(UserTypeEnum.ADMIN.getValue(), message.getToUserId(), // to specified user
                    "demo-message-receive", toMessage);
            return;
        }
        // Situation 2: Mass sending
        DemoReceiveMessage toMessage = new DemoReceiveMessage().setFromUserId(fromUserId)
                .setText(message.getText()).setSingle(false);
        webSocketMessageSender.sendObject(UserTypeEnum.ADMIN.getValue(), // to all users
                "demo-message-receive", toMessage);
    }

    @Override
    public String getType() {
        return "demo-message-send";
    }

}
