package cn.iocoder.yudao.module.agri.framework.weather;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import jakarta.annotation.Resource;

@Component
@Slf4j
public class WeatherApiClient {

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private WeatherApiProperties properties;

    public JsonNode getCurrentWeather(double lat, double lon) {
        String url = properties.getBaseUrl() + "/current.json"
                + "?key=" + properties.getApiKey()
                + "&q=" + lat + "," + lon
                + "&aqi=no";

        log.debug("Fetching weather from: {}", url.replace(properties.getApiKey(), "***"));
        return restTemplate.getForObject(url, JsonNode.class);
    }

    public JsonNode getForecast(double lat, double lon, int days) {
        String url = properties.getBaseUrl() + "/forecast.json"
                + "?key=" + properties.getApiKey()
                + "&q=" + lat + "," + lon
                + "&days=" + days
                + "&aqi=no"
                + "&alerts=no";

        log.debug("Fetching forecast from: {}", url.replace(properties.getApiKey(), "***"));
        return restTemplate.getForObject(url, JsonNode.class);
    }

}
