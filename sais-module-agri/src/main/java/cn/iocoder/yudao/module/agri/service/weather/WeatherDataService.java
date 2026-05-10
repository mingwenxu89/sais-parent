package cn.iocoder.yudao.module.agri.service.weather;

import cn.iocoder.yudao.module.agri.controller.admin.weather.vo.WeatherDataRespVO;
import cn.iocoder.yudao.module.agri.dal.dataobject.weather.WeatherDataDO;

import java.util.List;
import java.util.Optional;

public interface WeatherDataService {

    List<WeatherDataRespVO> getLatestWeatherList();

    Optional<WeatherDataDO> getLatestWeatherByFarmId(Long farmId);

    void fetchAndSaveWeather(Long farmId);

    void fetchAndSaveAllWeather();

}
