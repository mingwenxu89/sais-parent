package cn.iocoder.yudao.framework.websocket.core.util;

import cn.iocoder.yudao.framework.security.core.LoginUser;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;

/**
 * Tool classes specific to web packages
 *
 * @author Yudao Source Code
 */
public class WebSocketFrameworkUtils {

 public static final String ATTRIBUTE_LOGIN_USER = "LOGIN_USER";

 /**
     * Set current user
 *
     * @param loginUser Login user
 * @param attributes Session
 */
 public static void setLoginUser(LoginUser loginUser, Map<String, Object> attributes) {
 attributes.put(ATTRIBUTE_LOGIN_USER, loginUser);
 }

 /**
     * Get current user
 *
     * @return current user
 */
 public static LoginUser getLoginUser(WebSocketSession session) {
 return (LoginUser) session.getAttributes().get(ATTRIBUTE_LOGIN_USER);
 }

 /**
     * Get the current user's number
 *
     * @return User ID
 */
 public static Long getLoginUserId(WebSocketSession session) {
 LoginUser loginUser = getLoginUser(session);
 return loginUser != null ? loginUser.getId(): null;
 }

 /**
     * Get the type of current user
 *
     * @return User ID
 */
 public static Integer getLoginUserType(WebSocketSession session) {
 LoginUser loginUser = getLoginUser(session);
 return loginUser != null ? loginUser.getUserType(): null;
 }

 /**
     * Get the current user’s tenant ID
 *
 * @param session Session
     * @return Tenant number
 */
 public static Long getTenantId(WebSocketSession session) {
 LoginUser loginUser = getLoginUser(session);
 return loginUser != null ? loginUser.getTenantId(): null;
 }

}
