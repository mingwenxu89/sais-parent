package cn.iocoder.yudao.module.agri.convert.crop;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.agri.controller.admin.crop.vo.CropRespVO;
import cn.iocoder.yudao.module.agri.controller.admin.crop.vo.CropSaveReqVO;
import cn.iocoder.yudao.module.agri.dal.dataobject.crop.CropDO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CropConvert {

    CropConvert INSTANCE = Mappers.getMapper(CropConvert.class);

    PageResult<CropRespVO> convertPage(PageResult<CropDO> page);

    List<CropRespVO> convertList(List<CropDO> list);

    CropRespVO convert(CropDO bean);

    CropDO convert(CropSaveReqVO bean);

}
