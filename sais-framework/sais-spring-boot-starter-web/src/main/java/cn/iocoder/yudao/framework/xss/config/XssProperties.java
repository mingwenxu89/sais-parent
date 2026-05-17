package cn.iocoder.yudao.framework.xss.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;

/**
 * Xss configuration properties
 *
 * @author Yudao Source Code
 */
@ConfigurationProperties(prefix = "yudao.xss")
@Validated
@Data
public class XssProperties {

 /**
     * Whether to enable, default is true
 */
 private boolean enable = true;
 /**
     * URLs to be excluded, empty by default
 */
 private List<String> excludeUrls = Collections.emptyList();

}
