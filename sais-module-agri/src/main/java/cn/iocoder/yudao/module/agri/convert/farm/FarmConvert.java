package cn.iocoder.yudao.module.agri.convert.farm;

import cn.iocoder.yudao.module.agri.controller.admin.farm.vo.FarmRespVO;
import cn.iocoder.yudao.module.agri.controller.admin.farm.vo.FarmSaveReqVO;
import cn.iocoder.yudao.module.agri.dal.dataobject.farm.FarmDO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FarmConvert {

    FarmConvert INSTANCE = Mappers.getMapper(FarmConvert.class);

    FarmRespVO convert(FarmDO bean);

    FarmDO convert(FarmSaveReqVO bean);

}
