package cn.iocoder.yudao.framework.websocket.core.security;

import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.framework.security.core.filter.TokenAuthenticationFilter;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.framework.websocket.core.util.WebSocketFrameworkUtils;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

/**
 * Login user’s {@link HandshakeInterceptor} implementation class
 *
 * The process is as follows:
 * 1. When the front end connects to WebSocket, it will connect ?token={token} to ws:// so that it can be authenticated by {@link TokenAuthenticationFilter}
 * 2. {@link LoginUserHandshakeInterceptor} is responsible for adding {@link LoginUser} to {@link WebSocketSession}
 *
 * @author Yudao Source Code
 */
public class LoginUserHandshakeInterceptor implements HandshakeInterceptor {

 @Override
 public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
 WebSocketHandler wsHandler, Map<String, Object> attributes) {
 LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
 if (loginUser != null) {
 WebSocketFrameworkUtils.setLoginUser(loginUser, attributes);
 }
 return true;
 }

 @Override
 public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
 WebSocketHandler wsHandler, Exception exception) {
 // do nothing
 }

}
