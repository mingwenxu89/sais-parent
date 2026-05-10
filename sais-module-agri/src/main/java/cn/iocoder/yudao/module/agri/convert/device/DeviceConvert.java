package cn.iocoder.yudao.module.agri.convert.device;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.agri.controller.admin.device.vo.DeviceRespVO;
import cn.iocoder.yudao.module.agri.controller.admin.device.vo.DeviceSaveReqVO;
import cn.iocoder.yudao.module.agri.dal.dataobject.device.DeviceDO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DeviceConvert {

    DeviceConvert INSTANCE = Mappers.getMapper(DeviceConvert.class);

    PageResult<DeviceRespVO> convertPage(PageResult<DeviceDO> page);

    List<DeviceRespVO> convertList(List<DeviceDO> list);

    DeviceRespVO convert(DeviceDO bean);

    DeviceDO convert(DeviceSaveReqVO bean);

}
