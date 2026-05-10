package cn.iocoder.yudao.module.agri.convert.irrigation;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.agri.controller.admin.irrigation.vo.IrrigationPlanRespVO;
import cn.iocoder.yudao.module.agri.controller.admin.irrigation.vo.IrrigationPlanSaveReqVO;
import cn.iocoder.yudao.module.agri.dal.dataobject.irrigation.IrrigationPlanDO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IrrigationPlanConvert {

    IrrigationPlanConvert INSTANCE = Mappers.getMapper(IrrigationPlanConvert.class);

    PageResult<IrrigationPlanRespVO> convertPage(PageResult<IrrigationPlanDO> page);

    List<IrrigationPlanRespVO> convertList(List<IrrigationPlanDO> list);

    IrrigationPlanRespVO convert(IrrigationPlanDO bean);

    IrrigationPlanDO convert(IrrigationPlanSaveReqVO bean);

}
