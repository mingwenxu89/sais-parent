package cn.iocoder.yudao.module.agri.framework.deepseek;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "yudao.agri.deepseek")
public class DeepSeekProperties {

    /** Whether DeepSeek integration is enabled. */
    private boolean enabled = false;

    /** DeepSeek API key from the DeepSeek Open Platform. */
    private String apiKey;

    /** OpenAI-compatible API base URL. */
    private String baseUrl = "https://api.deepseek.com";

    /** DeepSeek model ID. */
    private String model = "deepseek-v4-flash";

    /** Per-request timeout in milliseconds. */
    private int timeoutMs = 30000;

}
