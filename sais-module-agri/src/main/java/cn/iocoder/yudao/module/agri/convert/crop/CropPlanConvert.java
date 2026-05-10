package cn.iocoder.yudao.module.agri.convert.crop;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.agri.controller.admin.crop.vo.CropPlanRespVO;
import cn.iocoder.yudao.module.agri.controller.admin.crop.vo.CropPlanSaveReqVO;
import cn.iocoder.yudao.module.agri.dal.dataobject.crop.CropDO;
import cn.iocoder.yudao.module.agri.dal.dataobject.crop.CropPlanDO;
import cn.iocoder.yudao.module.agri.dal.dataobject.field.FieldDO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CropPlanConvert {

    CropPlanConvert INSTANCE = Mappers.getMapper(CropPlanConvert.class);

    PageResult<CropPlanRespVO> convertPage(PageResult<CropPlanDO> page);

    List<CropPlanRespVO> convertList(List<CropPlanDO> list);

    CropPlanRespVO convert(CropPlanDO bean);

    CropPlanDO convert(CropPlanSaveReqVO bean);

}
