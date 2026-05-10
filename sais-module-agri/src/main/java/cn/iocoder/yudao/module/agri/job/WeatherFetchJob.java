package cn.iocoder.yudao.module.agri.job;

import cn.iocoder.yudao.framework.tenant.core.job.TenantJob;
import cn.iocoder.yudao.module.agri.service.weather.WeatherDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;

/**
 * Scheduled job to fetch and persist weather data for all farms every 30 minutes.
 */
@Component
@Slf4j
public class WeatherFetchJob {

    @Resource
    private WeatherDataService weatherDataService;

    @Scheduled(cron = "0 0/10 * * * ?")
    @TenantJob
    public void fetchWeather() {
        log.info("[WeatherFetchJob] Starting scheduled weather fetch");
        try {
            weatherDataService.fetchAndSaveAllWeather();
            log.info("[WeatherFetchJob] Weather fetch completed successfully");
        } catch (Exception e) {
            log.error("[WeatherFetchJob] Weather fetch failed", e);
        }
    }

}
