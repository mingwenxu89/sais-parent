package cn.iocoder.yudao.module.agri.framework.bedrock;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "yudao.agri.bedrock")
public class AwsBedrockProperties {

    /** AWS IAM access key ID. When blank, Bedrock integration is disabled. */
    private String accessKeyId;

    /** AWS IAM secret access key. */
    private String secretAccessKey;

    /** AWS region. Defaults to ap-southeast-2 to match the IoT endpoint region. */
    private String region = "ap-southeast-2";

    /** Bedrock model ID to invoke via the Converse API. */
    private String modelId = "anthropic.claude-3-5-haiku-20241022-v1:0";

    /** Per-request timeout in milliseconds. */
    private int timeoutMs = 30000;

}
