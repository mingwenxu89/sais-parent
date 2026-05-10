package cn.iocoder.yudao.module.agri.service.weather;

import cn.iocoder.yudao.module.agri.controller.admin.weather.vo.WeatherDataConvert;
import cn.iocoder.yudao.module.agri.controller.admin.weather.vo.WeatherDataRespVO;
import cn.iocoder.yudao.module.agri.dal.dataobject.farm.FarmDO;
import cn.iocoder.yudao.module.agri.dal.dataobject.weather.WeatherDataDO;
import cn.iocoder.yudao.module.agri.dal.mysql.farm.FarmMapper;
import cn.iocoder.yudao.module.agri.dal.mysql.weather.WeatherDataMapper;
import cn.iocoder.yudao.module.agri.framework.weather.WeatherApiClient;
import cn.iocoder.yudao.module.agri.service.alert.AlertCheckService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Validated
@Slf4j
public class WeatherDataServiceImpl implements WeatherDataService {

    @Resource
    private WeatherDataMapper weatherDataMapper;

    @Resource
    private FarmMapper farmMapper;

    @Resource
    private WeatherApiClient weatherApiClient;

    @Resource
    private AlertCheckService alertCheckService;

    private final WeatherDataConvert convert = new WeatherDataConvert();

    @Override
    public List<WeatherDataRespVO> getLatestWeatherList() {
        List<FarmDO> farms = farmMapper.selectList();
        List<WeatherDataRespVO> result = new ArrayList<>();
        
        for (FarmDO farm : farms) {
            List<WeatherDataDO> weatherList = weatherDataMapper.selectByFarmId(farm.getId());
            WeatherDataRespVO respVO = convert.convertWeatherList(weatherList, farm);
            result.add(respVO);
        }
        return result;
    }

    @Override
    public Optional<WeatherDataDO> getLatestWeatherByFarmId(Long farmId) {
        List<WeatherDataDO> list = weatherDataMapper.selectByFarmId(farmId);
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    @Override
    public void fetchAndSaveWeather(Long farmId) {
        FarmDO farm = farmMapper.selectById(farmId);
        if (farm == null) {
            log.warn("Farm not found: {}", farmId);
            return;
        }

        try {
            JsonNode forecastJson = weatherApiClient.getForecast(
                    farm.getLatitude().doubleValue(),
                    farm.getLongitude().doubleValue(),
                    4
            );

            parseAndSaveForecast(forecastJson, farmId);
            log.info("Weather forecast saved for farm {} ({})", farm.getFarmName(), farmId);
            alertCheckService.checkWeatherForecast(farmId);
        } catch (Exception e) {
            log.error("Failed to fetch weather for farm {}: {}", farmId, e.getMessage());
        }
    }

    @Override
    public void fetchAndSaveAllWeather() {
        List<FarmDO> farms = farmMapper.selectList();
        for (FarmDO farm : farms) {
            fetchAndSaveWeather(farm.getId());
        }
    }

    private void parseAndSaveForecast(JsonNode json, Long farmId) {
        JsonNode forecastDays = json.path("forecast").path("forecastday");
        
        for (JsonNode day : forecastDays) {
            WeatherDataDO weatherData = new WeatherDataDO();
            weatherData.setFarmId(farmId);
            weatherData.setSource("WeatherAPI");
            weatherData.setRecordTime(LocalDateTime.now());
            
            String dateStr = day.path("date").asText();
            weatherData.setForecastDate(LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE));
            
            JsonNode dayData = day.path("day");
            weatherData.setWeatherDesc(dayData.path("condition").path("text").asText(null));
            weatherData.setTempMin(BigDecimal.valueOf(dayData.path("mintemp_c").asDouble())
                    .setScale(2, RoundingMode.HALF_UP));
            weatherData.setTempMax(BigDecimal.valueOf(dayData.path("maxtemp_c").asDouble())
                    .setScale(2, RoundingMode.HALF_UP));
            weatherData.setHumidity(BigDecimal.valueOf(dayData.path("avghumidity").asDouble())
                    .setScale(2, RoundingMode.HALF_UP));
            weatherData.setRainfall(BigDecimal.valueOf(dayData.path("totalprecip_mm").asDouble())
                    .setScale(2, RoundingMode.HALF_UP));

            JsonNode current = json.path("current");
            weatherData.setTemperature(BigDecimal.valueOf(current.path("temp_c").asDouble())
                    .setScale(2, RoundingMode.HALF_UP));
            weatherData.setPressure(BigDecimal.valueOf(current.path("pressure_mb").asDouble())
                    .setScale(2, RoundingMode.HALF_UP));

            WeatherDataDO existing = weatherDataMapper.selectByFarmIdAndDate(farmId, weatherData.getForecastDate());
            if (existing != null) {
                weatherData.setId(existing.getId());
                weatherDataMapper.updateById(weatherData);
            } else {
                weatherDataMapper.insert(weatherData);
            }
        }
    }

}
