package cn.iocoder.yudao.module.infra.framework.file.core.client.local;

import cn.iocoder.yudao.module.infra.framework.file.core.client.FileClientConfig;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.NotEmpty;

/**
 * Configuration class for local file client
 *
 * @author Yudao Source Code
 */
@Data
public class LocalFileClientConfig implements FileClientConfig {

    /**
     * base path
     */
    @NotEmpty(message = "The base path cannot be empty")
    private String basePath;

    /**
     * Custom domain name
     */
    @NotEmpty(message = "domain cannot be empty")
    @URL(message = "domain must be in URL format")
    private String domain;

}
