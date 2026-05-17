package cn.iocoder.yudao.module.infra.framework.file.core.client.db;

import cn.iocoder.yudao.module.infra.framework.file.core.client.FileClientConfig;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.NotEmpty;

/**
 * Configuration class for file client based on DB storage
 *
 * @author Yudao Source Code
 */
@Data
public class DBFileClientConfig implements FileClientConfig {

    /**
     * Custom domain name
     */
    @NotEmpty(message = "domain cannot be empty")
    @URL(message = "domain must be in URL format")
    private String domain;

}
