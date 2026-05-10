package cn.iocoder.yudao.module.agri.dal.mysql.alert;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.agri.controller.admin.alert.vo.AlertPageReqVO;
import cn.iocoder.yudao.module.agri.dal.dataobject.alert.AlertDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AlertMapper extends BaseMapperX<AlertDO> {

    default PageResult<AlertDO> selectPage(AlertPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<AlertDO>()
                .eqIfPresent(AlertDO::getFarmId, reqVO.getFarmId())
                .eqIfPresent(AlertDO::getFieldId, reqVO.getFieldId())
                .eqIfPresent(AlertDO::getAlertType, reqVO.getAlertType())
                .eqIfPresent(AlertDO::getLevel, reqVO.getLevel())
                .eqIfPresent(AlertDO::getStatus, reqVO.getStatus())
                .orderByDesc(AlertDO::getId));
    }

    // status 0=UNHANDLED 1=HANDLING are considered "active"
    default boolean existsActiveAlert(Integer alertType, Long farmId, Long fieldId) {
        LambdaQueryWrapperX<AlertDO> wrapper = new LambdaQueryWrapperX<AlertDO>()
                .eq(AlertDO::getAlertType, alertType)
                .in(AlertDO::getStatus, List.of(0, 1));
        if (farmId != null) {
            wrapper.eq(AlertDO::getFarmId, farmId);
        }
        if (fieldId != null) {
            wrapper.eq(AlertDO::getFieldId, fieldId);
        }
        return selectCount(wrapper) > 0;
    }

}
