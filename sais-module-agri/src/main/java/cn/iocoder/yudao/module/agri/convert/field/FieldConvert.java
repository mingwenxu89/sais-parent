package cn.iocoder.yudao.module.agri.convert.field;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.agri.controller.admin.field.vo.FieldRespVO;
import cn.iocoder.yudao.module.agri.controller.admin.field.vo.FieldSaveReqVO;
import cn.iocoder.yudao.module.agri.dal.dataobject.field.FieldDO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FieldConvert {

    FieldConvert INSTANCE = Mappers.getMapper(FieldConvert.class);

    PageResult<FieldRespVO> convertPage(PageResult<FieldDO> page);

    List<FieldRespVO> convertList(List<FieldDO> list);

    FieldRespVO convert(FieldDO bean);

    FieldDO convert(FieldSaveReqVO bean);

}
