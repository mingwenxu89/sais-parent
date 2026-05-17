package cn.iocoder.yudao.framework.websocket.core.sender.local;

import cn.iocoder.yudao.framework.websocket.core.sender.AbstractWebSocketMessageSender;
import cn.iocoder.yudao.framework.websocket.core.sender.WebSocketMessageSender;
import cn.iocoder.yudao.framework.websocket.core.session.WebSocketSessionManager;

/**
 * Local {@link WebSocketMessageSender} implementation class
 *
 * Note: Only suitable for stand-alone scenarios! ! !
 *
 * @author Yudao Source Code
 */
public class LocalWebSocketMessageSender extends AbstractWebSocketMessageSender {

 public LocalWebSocketMessageSender(WebSocketSessionManager sessionManager) {
 super(sessionManager);
 }

}
