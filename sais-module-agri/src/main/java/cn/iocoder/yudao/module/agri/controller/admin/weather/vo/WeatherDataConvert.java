package cn.iocoder.yudao.module.agri.controller.admin.weather.vo;

import cn.iocoder.yudao.module.agri.dal.dataobject.weather.WeatherDataDO;
import cn.iocoder.yudao.module.agri.dal.dataobject.farm.FarmDO;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class WeatherDataConvert {

    public WeatherDataRespVO convertWeatherList(List<WeatherDataDO> weatherList, FarmDO farm) {
        WeatherDataRespVO vo = new WeatherDataRespVO();
        vo.setFarmId(farm.getId());
        vo.setFarmName(farm.getFarmName());
        vo.setSource("WeatherAPI");

        if (weatherList == null || weatherList.isEmpty()) {
            return vo;
        }

        WeatherDataDO current = weatherList.get(0);
        vo.setRecordTime(current.getRecordTime());
        vo.setWeatherDesc(current.getWeatherDesc());
        vo.setTemperature(current.getTemperature());
        vo.setHumidity(current.getHumidity());
        vo.setRainfall(current.getRainfall());
        vo.setPressure(current.getPressure());

        List<WeatherDataRespVO.ForecastDayVO> forecast = weatherList.stream()
                .map(this::convertToForecastDay)
                .collect(Collectors.toList());
        vo.setForecast(forecast);

        return vo;
    }

    private WeatherDataRespVO.ForecastDayVO convertToForecastDay(WeatherDataDO weather) {
        WeatherDataRespVO.ForecastDayVO day = new WeatherDataRespVO.ForecastDayVO();
        day.setForecastDate(weather.getForecastDate());
        day.setWeatherDesc(weather.getWeatherDesc());
        day.setTempMin(weather.getTempMin());
        day.setTempMax(weather.getTempMax());
        day.setHumidity(weather.getHumidity());
        day.setRainfall(weather.getRainfall());
        return day;
    }

    public WeatherDataRespVO convert(WeatherDataDO weather, FarmDO farm) {
        if (weather == null) {
            return null;
        }
        WeatherDataRespVO vo = new WeatherDataRespVO();
        vo.setFarmId(weather.getFarmId());
        vo.setFarmName(farm != null ? farm.getFarmName() : null);
        vo.setRecordTime(weather.getRecordTime());
        vo.setWeatherDesc(weather.getWeatherDesc());
        vo.setTemperature(weather.getTemperature());
        vo.setHumidity(weather.getHumidity());
        vo.setRainfall(weather.getRainfall());
        vo.setPressure(weather.getPressure());
        vo.setSource(weather.getSource());
        return vo;
    }

}
