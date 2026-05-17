package cn.iocoder.yudao.framework.web.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@ConfigurationProperties(prefix = "yudao.web")
@Validated
@Data
public class WebProperties {

    @NotNull(message = "aPP API cannot be empty")
 private Api appApi = new Api("/app-api", "**.controller.app.**");
    @NotNull(message = "admin API cannot be empty")
 private Api adminApi = new Api("/admin-api", "**.controller.admin.**");

    @NotNull(message = "admin UI cannot be empty")
 private Ui adminUi;

 @Data
 @AllArgsConstructor
 @NoArgsConstructor
 @Valid
 public static class Api {

 /**
         * API prefix, implements a unified prefix for all RESTFul APIs provided by Controller
 *
 *
         * Meaning: Use this prefix to prevent Swagger and Actuator from being accidentally exposed to the outside through Nginx, causing security issues.
         * In this way, Nginx only needs to configure all interfaces forwarded to /API/*.
 *
 * @see YudaoWebAutoConfiguration#configurePathMatch(PathMatchConfigurer)
 */
        @NotEmpty(message = "aPI prefix cannot be empty")
 private String prefix;

 /**
         * Ant path rules for the package where the Controller is located
 *
         * The main purpose is to set the specified {@link #prefix} to the Controller
 */
        @NotEmpty(message = "the package where the Controller is located cannot be empty")
 private String controller;

 }

 @Data
 @Valid
 public static class Ui {

 /**
         * Access address
 */
 private String url;

 }

}
