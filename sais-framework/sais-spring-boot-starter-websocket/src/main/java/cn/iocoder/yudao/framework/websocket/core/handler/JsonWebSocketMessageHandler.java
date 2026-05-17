package cn.iocoder.yudao.framework.websocket.core.handler;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.TypeUtil;
import cn.iocoder.yudao.framework.common.util.json.JsonUtils;
import cn.iocoder.yudao.framework.tenant.core.util.TenantUtils;
import cn.iocoder.yudao.framework.websocket.core.listener.WebSocketMessageListener;
import cn.iocoder.yudao.framework.websocket.core.message.JsonWebSocketMessage;
import cn.iocoder.yudao.framework.websocket.core.util.WebSocketFrameworkUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * JSON format {@link WebSocketHandler} implementation class
 *
 * Based on the {@link JSONWebSocketMessage#getType()} message type, dispatch to the corresponding {@link WebSocketMessageListener} listener.
 *
 * @author Yudao Source Code
 */
@Slf4j
public class JsonWebSocketMessageHandler extends TextWebSocketHandler {

 /**
     * Mapping of type to WebSocketMessageListener
 */
 private final Map<String, WebSocketMessageListener<Object>> listeners = new HashMap<>();

 @SuppressWarnings({"rawtypes", "unchecked"})
 public JsonWebSocketMessageHandler(List<? extends WebSocketMessageListener> listenersList) {
 listenersList.forEach((Consumer<WebSocketMessageListener>)
 listener -> listeners.put(listener.getType(), listener));
 }

 @Override
 protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 1.1 Empty message, skip
 if (message.getPayloadLength() == 0) {
 return;
 }
        // 1.2 Ping heartbeat message, directly return pong message.
 if (message.getPayloadLength() == 4 && Objects.equals(message.getPayload(), "ping")) {
 session.sendMessage(new TextMessage("pong"));
 return;
 }

        // 2.1 Parse the message
 try {
 JsonWebSocketMessage jsonMessage = JsonUtils.parseObject(message.getPayload(), JsonWebSocketMessage.class);
 if (jsonMessage == null) {
                log.error("[handleTextMessage][session({}) message({}) resolves to empty]", session.getId(), message.getPayload());
 return;
 }
 if (StrUtil.isEmpty(jsonMessage.getType())) {
                log.error("[handleTextMessage][session({}) message({}) type is empty]", session.getId(), message.getPayload());
 return;
 }
            // 2.2 Obtain the corresponding WebSocketMessageListener
 WebSocketMessageListener<Object> messageListener = listeners.get(jsonMessage.getType());
 if (messageListener == null) {
                log.error("[handleTextMessage][session({}) message({}) listener is empty]", session.getId(), message.getPayload());
 return;
 }
            // 2.3 Processing messages
 Type type = TypeUtil.getTypeArgument(messageListener.getClass(), 0);
 Object messageObj = JsonUtils.parseObject(jsonMessage.getContent(), type);
 Long tenantId = WebSocketFrameworkUtils.getTenantId(session);
 TenantUtils.execute(tenantId, () -> messageListener.onMessage(session, messageObj));
 } catch (Throwable ex) {
            log.error("[handleTextMessage][session({}) message({}) handling exception]", session.getId(), message.getPayload());
 }
 }

}
