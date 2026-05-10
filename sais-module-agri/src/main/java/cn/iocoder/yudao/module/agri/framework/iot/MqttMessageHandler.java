package cn.iocoder.yudao.module.agri.framework.iot;

@FunctionalInterface
public interface MqttMessageHandler {
    void onMessage(String topic, byte[] payload);
}
