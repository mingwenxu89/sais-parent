package cn.iocoder.yudao.module.agri.controller.admin.weather;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.agri.controller.admin.weather.vo.WeatherDataConvert;
import cn.iocoder.yudao.module.agri.controller.admin.weather.vo.WeatherDataRespVO;
import cn.iocoder.yudao.module.agri.dal.dataobject.weather.WeatherDataDO;
import cn.iocoder.yudao.module.agri.service.weather.WeatherDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "Admin - Weather Data")
@RestController
@RequestMapping("/agri/weather")
@Validated
@Slf4j
public class WeatherDataController {

    @Resource
    private WeatherDataService weatherDataService;

    @GetMapping("/latest")
    @Operation(summary = "Get latest weather data for all farms")
    @PreAuthorize("@ss.hasPermission('agri:weather:query')")
    public CommonResult<List<WeatherDataRespVO>> getLatestWeatherList() {
        return success(weatherDataService.getLatestWeatherList());
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh weather data for all farms")
    @PreAuthorize("@ss.hasPermission('agri:weather:update')")
    public CommonResult<Boolean> refreshWeather() {
        weatherDataService.fetchAndSaveAllWeather();
        return success(true);
    }

    @GetMapping("/latest/{farmId}")
    @Operation(summary = "Get latest weather data for a specific farm")
    @PreAuthorize("@ss.hasPermission('agri:weather:query')")
    public CommonResult<WeatherDataRespVO> getLatestWeatherByFarmId(@PathVariable Long farmId) {
        WeatherDataDO weather = weatherDataService.getLatestWeatherByFarmId(farmId).orElse(null);
        WeatherDataRespVO vo = new WeatherDataConvert().convert(weather, null);
        return success(vo);
    }

}
