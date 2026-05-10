package cn.iocoder.yudao.module.agri.convert.crop;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.agri.controller.admin.crop.vo.CropGrowthStageRespVO;
import cn.iocoder.yudao.module.agri.controller.admin.crop.vo.CropGrowthStageSaveReqVO;
import cn.iocoder.yudao.module.agri.dal.dataobject.crop.CropGrowthStageDO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CropGrowthStageConvert {

    CropGrowthStageConvert INSTANCE = Mappers.getMapper(CropGrowthStageConvert.class);

    PageResult<CropGrowthStageRespVO> convertPage(PageResult<CropGrowthStageDO> page);

    List<CropGrowthStageRespVO> convertList(List<CropGrowthStageDO> list);

    CropGrowthStageRespVO convert(CropGrowthStageDO bean);

    CropGrowthStageDO convert(CropGrowthStageSaveReqVO bean);

}
