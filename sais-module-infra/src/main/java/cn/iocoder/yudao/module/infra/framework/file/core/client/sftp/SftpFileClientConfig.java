package cn.iocoder.yudao.module.infra.framework.file.core.client.sftp;

import cn.iocoder.yudao.module.infra.framework.file.core.client.FileClientConfig;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * Configuration class for SFTP file client
 *
 * @author Yudao Source Code
 */
@Data
public class SftpFileClientConfig implements FileClientConfig {

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

    /**
     * host address
     */
    @NotEmpty(message = "host cannot be empty")
    private String host;
    /**
     * host port
     */
    @NotNull(message = "port cannot be empty")
    private Integer port;
    /**
     * Username
     */
    @NotEmpty(message = "Username cannot be empty")
    private String username;
    /**
     * Password
     */
    @NotEmpty(message = "Password cannot be empty")
    private String password;

}
