package cn.iocoder.yudao.framework.swagger.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import jakarta.validation.constraints.NotEmpty;

/**
 * Swagger configuration properties
 *
 * @author Yudao Source Code
 */
@ConfigurationProperties("yudao.swagger")
@Data
public class SwaggerProperties {

    /**
     * title
     */
    @NotEmpty(message = "title cannot be empty")
    private String title;
    /**
     * describe
     */
    @NotEmpty(message = "description cannot be empty")
    private String description;
    /**
     * author
     */
    @NotEmpty(message = "author cannot be empty")
    private String author;
    /**
     * Version
     */
    @NotEmpty(message = "version cannot be empty")
    private String version;
    /**
     * url
     */
    @NotEmpty(message = "the scanned package cannot be empty")
    private String url;
    /**
     * email
     */
    @NotEmpty(message = "scanned email cannot be empty")
    private String email;

    /**
     * license
     */
    @NotEmpty(message = "the scanned license cannot be empty")
    private String license;

    /**
     * license-url
     */
    @NotEmpty(message = "the scanned license-url cannot be empty")
    private String licenseUrl;

}
