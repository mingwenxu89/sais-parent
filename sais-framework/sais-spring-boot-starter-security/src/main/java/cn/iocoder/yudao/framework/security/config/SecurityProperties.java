package cn.iocoder.yudao.framework.security.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;

@ConfigurationProperties(prefix = "yudao.security")
@Validated
@Data
public class SecurityProperties {

 /**
     * When making an HTTP request, the request header of the access token
 */
    @NotEmpty(message = "token Header cannot be empty")
 private String tokenHeader = "Authorization";
 /**
     * When making an HTTP request, the request parameters of the access token
 *
     * Initial purpose: To solve the problem that WebSocket cannot pass parameters through header and can only be spliced ​​through token parameters.
 */
    @NotEmpty(message = "token Parameter cannot be empty")
 private String tokenParameter = "token";

 /**
     * Mock mode switch
 */
    @NotNull(message = "the mock mode switch cannot be empty")
 private Boolean mockEnable = false;
 /**
     * key for mock mode
     * Be sure to configure the key to ensure security
 */
    @NotEmpty(message = "the key for mock mode cannot be empty") // A default value is set here because configuration is actually only required when mockEnable is true.
 private String mockSecret = "test";

 /**
     * Login-free URL list
 */
 private List<String> permitAllUrls = Collections.emptyList();

 /**
     * PasswordEncoder encryption complexity, the higher the value, the greater the overhead.
 */
 private Integer passwordEncoderLength = 4;
}
