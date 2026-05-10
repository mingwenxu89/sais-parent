package cn.iocoder.yudao.module.agri.controller.admin.weather.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Schema(name = "Weather Data Response VO")
public class WeatherDataRespVO {

    @Schema(description = "Farm ID")
    private Long farmId;

    @Schema(description = "Farm Name")
    private String farmName;

    @Schema(description = "Record Time")
    private LocalDateTime recordTime;

    @Schema(description = "Weather Description")
    private String weatherDesc;

    @Schema(description = "Temperature (℃)")
    private BigDecimal temperature;

    @Schema(description = "Humidity (%)")
    private BigDecimal humidity;

    @Schema(description = "Rainfall (mm)")
    private BigDecimal rainfall;

    @Schema(description = "Pressure (hPa)")
    private BigDecimal pressure;

    @Schema(description = "Data Source")
    private String source;

    @Schema(description = "Forecast List")
    private java.util.List<ForecastDayVO> forecast;

    @Data
    @Schema(name = "Forecast Day VO")
    public static class ForecastDayVO {
        @Schema(description = "Forecast Date")
        private LocalDate forecastDate;
        
        @Schema(description = "Weather Description")
        private String weatherDesc;
        
        @Schema(description = "Min Temperature (℃)")
        private BigDecimal tempMin;
        
        @Schema(description = "Max Temperature (℃)")
        private BigDecimal tempMax;
        
        @Schema(description = "Humidity (%)")
        private BigDecimal humidity;
        
        @Schema(description = "Rainfall (mm)")
        private BigDecimal rainfall;
    }

}
