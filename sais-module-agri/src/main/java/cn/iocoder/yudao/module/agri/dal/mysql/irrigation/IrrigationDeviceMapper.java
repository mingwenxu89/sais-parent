package cn.iocoder.yudao.module.agri.dal.mysql.irrigation;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.agri.controller.admin.irrigation.vo.IrrigationDevicePageReqVO;
import cn.iocoder.yudao.module.agri.dal.dataobject.irrigation.IrrigationDeviceDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface IrrigationDeviceMapper extends BaseMapperX<IrrigationDeviceDO> {

    @Select("SELECT nextval('sais_irrigation_device_code_seq')")
    Long nextVal();

    default PageResult<IrrigationDeviceDO> selectPage(IrrigationDevicePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<IrrigationDeviceDO>()
                .eqIfPresent(IrrigationDeviceDO::getFieldId, reqVO.getFieldId())
                .eqIfPresent(IrrigationDeviceDO::getFarmId, reqVO.getFarmId())
                .eqIfPresent(IrrigationDeviceDO::getStatus, reqVO.getStatus())
                .eqIfPresent(IrrigationDeviceDO::getIsWatering, reqVO.getIsWatering())
                .orderByDesc(IrrigationDeviceDO::getId));
    }

    default IrrigationDeviceDO selectByFieldId(Long fieldId) {
        return selectOne(new LambdaQueryWrapperX<IrrigationDeviceDO>().eq(IrrigationDeviceDO::getFieldId, fieldId));
    }

    default List<IrrigationDeviceDO> selectListByFieldId(Long fieldId) {
        return selectList(new LambdaQueryWrapperX<IrrigationDeviceDO>()
                .eq(IrrigationDeviceDO::getFieldId, fieldId));
    }

}
