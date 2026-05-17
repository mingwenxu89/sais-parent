package cn.iocoder.yudao.framework.websocket.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * WebSocket configuration items
 *
 * @author xingyu4j
 */
@ConfigurationProperties("yudao.websocket")
@Data
@Validated
public class WebSocketProperties {

 /**
     * WebSocket connection path
 */
    @NotEmpty(message = "the connection path of WebSocket cannot be empty")
 private String path = "/ws";

 /**
     * Type of message sender
 *
     * Optional values: local, Redis, rocketMQ, kafka, rabbitMQ
 */
    @NotNull(message = "webSocket message sender cannot be empty")
 private String senderType = "local";

}
