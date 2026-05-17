package cn.iocoder.yudao.module.infra.websocket.message;

import lombok.Data;

/**
 * Example: server -> client synchronization message
 *
 * @author Yudao Source Code
 */
@Data
public class DemoReceiveMessage {

    /**
     * Recipient's ID
     */
    private Long fromUserId;
    /**
     * content
     */
    private String text;

    /**
     * Whether to chat alone
     */
    private Boolean single;

}
