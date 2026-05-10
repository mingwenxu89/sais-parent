package cn.iocoder.yudao.module.agri.convert.sensor;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.agri.controller.admin.sensor.vo.SensorRespVO;
import cn.iocoder.yudao.module.agri.controller.admin.sensor.vo.SensorSaveReqVO;
import cn.iocoder.yudao.module.agri.dal.dataobject.sensor.SensorDO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SensorConvert {

    SensorConvert INSTANCE = Mappers.getMapper(SensorConvert.class);

    PageResult<SensorRespVO> convertPage(PageResult<SensorDO> page);

    List<SensorRespVO> convertList(List<SensorDO> list);

    SensorRespVO convert(SensorDO bean);

    SensorDO convert(SensorSaveReqVO bean);

}
