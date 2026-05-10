package cn.iocoder.yudao.module.agri.dal.mysql.sensor;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.agri.controller.admin.sensor.vo.SensorPageReqVO;
import cn.iocoder.yudao.module.agri.dal.dataobject.sensor.SensorDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Set;

@Mapper
public interface SensorMapper extends BaseMapperX<SensorDO> {

    @Select("SELECT nextval('sais_sensor_code_seq')")
    Long nextVal();

    default PageResult<SensorDO> selectPage(SensorPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<SensorDO>()
                .likeIfPresent(SensorDO::getSensorCode, reqVO.getSensorCode())
                .eqIfPresent(SensorDO::getSensorType, reqVO.getSensorType())
                .eqIfPresent(SensorDO::getFarmId, reqVO.getFarmId())
                .eqIfPresent(SensorDO::getFieldId, reqVO.getFieldId())
                .eqIfPresent(SensorDO::getStatus, reqVO.getStatus())
                .orderByDesc(SensorDO::getId));
    }

    default PageResult<SensorDO> selectPage(SensorPageReqVO reqVO, Set<Long> excludedSensorIds) {
        LambdaQueryWrapperX<SensorDO> wrapper = new LambdaQueryWrapperX<SensorDO>()
                .likeIfPresent(SensorDO::getSensorCode, reqVO.getSensorCode())
                .eqIfPresent(SensorDO::getSensorType, reqVO.getSensorType())
                .eqIfPresent(SensorDO::getFarmId, reqVO.getFarmId())
                .eqIfPresent(SensorDO::getFieldId, reqVO.getFieldId())
                .eqIfPresent(SensorDO::getStatus, reqVO.getStatus())
                .orderByDesc(SensorDO::getId);
        if (!excludedSensorIds.isEmpty()) {
            wrapper.notIn(SensorDO::getId, excludedSensorIds);
        }
        return selectPage(reqVO, wrapper);
    }

}
