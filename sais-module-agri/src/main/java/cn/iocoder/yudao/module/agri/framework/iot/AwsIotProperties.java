package cn.iocoder.yudao.module.agri.framework.iot;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;

@Data
@Validated
@ConfigurationProperties(prefix = "yudao.agri.iot")
public class AwsIotProperties {

    @NotBlank(message = "AWS IoT Core endpoint is required")
    private String endpoint;

    private int port;

    private String clientId;

    private String topicPrefix;

    @NotBlank(message = "Device certificate path is required")
    private String certPath;

    @NotBlank(message = "Device private key path is required")
    private String privateKeyPath;

    @NotBlank(message = "CA certificate path is required")
    private String caCertPath;

}
