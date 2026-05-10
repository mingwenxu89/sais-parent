package cn.iocoder.yudao.module.agri.dal.dataobject.weather;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@TableName("sais_weather_data")
@KeySequence("sais_weather_data_id_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class WeatherDataDO extends TenantBaseDO {

    @TableId
    private Long id;

    private Long farmId;

    private LocalDateTime recordTime;

    private String weatherDesc;

    private BigDecimal temperature;

    private BigDecimal humidity;

    private BigDecimal rainfall;

    private BigDecimal pressure;

    private String source;

    private LocalDate forecastDate;

    private BigDecimal tempMin;

    private BigDecimal tempMax;

}
