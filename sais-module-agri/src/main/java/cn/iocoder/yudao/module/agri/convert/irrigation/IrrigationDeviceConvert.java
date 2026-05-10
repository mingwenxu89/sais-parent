package cn.iocoder.yudao.module.agri.convert.irrigation;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.agri.controller.admin.irrigation.vo.IrrigationDeviceRespVO;
import cn.iocoder.yudao.module.agri.controller.admin.irrigation.vo.IrrigationDeviceSaveReqVO;
import cn.iocoder.yudao.module.agri.dal.dataobject.irrigation.IrrigationDeviceDO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IrrigationDeviceConvert {

    IrrigationDeviceConvert INSTANCE = Mappers.getMapper(IrrigationDeviceConvert.class);

    PageResult<IrrigationDeviceRespVO> convertPage(PageResult<IrrigationDeviceDO> page);

    List<IrrigationDeviceRespVO> convertList(List<IrrigationDeviceDO> list);

    IrrigationDeviceRespVO convert(IrrigationDeviceDO bean);

    IrrigationDeviceDO convert(IrrigationDeviceSaveReqVO bean);

}
