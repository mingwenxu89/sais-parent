package cn.iocoder.yudao.module.infra.websocket.message;

import lombok.Data;

/**
 * Example: client -> server sends message
 *
 * @author Yudao Source Code
 */
@Data
public class DemoSendMessage {

    /**
     * To whom to send
     *
     * If empty, instructions are sent to everyone
     */
    private Long toUserId;
    /**
     * content
     */
    private String text;

}
