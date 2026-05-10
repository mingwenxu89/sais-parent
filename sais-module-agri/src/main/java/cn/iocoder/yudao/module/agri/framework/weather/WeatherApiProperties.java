package cn.iocoder.yudao.module.agri.framework.weather;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;

@Data
@Validated
@Configuration
@ConfigurationProperties(prefix = "yudao.agri.weather.weather-api")
public class WeatherApiProperties {

    @NotBlank(message = "WeatherAPI key is required")
    private String apiKey;

    private String baseUrl = "https://api.weatherapi.com/v1";

    private Integer timeout = 10000;

}
