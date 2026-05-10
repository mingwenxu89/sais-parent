package cn.iocoder.yudao.module.agri.dal.mysql.weather;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.agri.dal.dataobject.weather.WeatherDataDO;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface WeatherDataMapper extends BaseMapperX<WeatherDataDO> {

    default List<WeatherDataDO> selectByFarmId(Long farmId) {
        return selectList(new LambdaQueryWrapperX<WeatherDataDO>()
                .eq(WeatherDataDO::getFarmId, farmId)
                .orderByAsc(WeatherDataDO::getForecastDate));
    }

    default WeatherDataDO selectByFarmIdAndDate(Long farmId, LocalDate date) {
        return selectOne(new LambdaQueryWrapperX<WeatherDataDO>()
                .eq(WeatherDataDO::getFarmId, farmId)
                .eq(WeatherDataDO::getForecastDate, date));
    }

}
