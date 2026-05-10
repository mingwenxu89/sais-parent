package cn.iocoder.yudao.module.agri.dal.mysql.sensordata;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.agri.dal.dataobject.sensordata.SensorDataDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SensorDataMapper extends BaseMapperX<SensorDataDO> {

    default PageResult<SensorDataDO> selectPageBySensorId(Long sensorId, PageParam page) {
        return selectPage(page, new LambdaQueryWrapperX<SensorDataDO>()
                .eqIfPresent(SensorDataDO::getSensorId, sensorId)
                .orderByDesc(SensorDataDO::getCollectedAt));
    }

    @Select("SELECT DISTINCT ON (sensor_id) * FROM sais_sensor_data " +
            "WHERE field_id = #{fieldId} AND deleted = 0 " +
            "ORDER BY sensor_id, collected_at DESC")
    List<SensorDataDO> selectLatestByFieldId(@Param("fieldId") Long fieldId);

    @Select("SELECT DISTINCT ON (sensor_id) * FROM sais_sensor_data " +
            "WHERE sensor_id = #{sensorId} AND deleted = 0 " +
            "ORDER BY sensor_id, collected_at DESC")
    List<SensorDataDO> selectLatestBySensorId(@Param("sensorId") Long sensorId);

}
