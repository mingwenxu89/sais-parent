package cn.iocoder.yudao.module.agri.convert.alert;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.agri.controller.admin.alert.vo.AlertRespVO;
import cn.iocoder.yudao.module.agri.controller.admin.alert.vo.AlertSaveReqVO;
import cn.iocoder.yudao.module.agri.dal.dataobject.alert.AlertDO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AlertConvert {

    AlertConvert INSTANCE = Mappers.getMapper(AlertConvert.class);

    PageResult<AlertRespVO> convertPage(PageResult<AlertDO> page);

    List<AlertRespVO> convertList(List<AlertDO> list);

    AlertRespVO convert(AlertDO bean);

    AlertDO convert(AlertSaveReqVO bean);

}
