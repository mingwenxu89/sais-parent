package cn.iocoder.yudao.module.agri.dal.mysql.device;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.agri.controller.admin.device.vo.DevicePageReqVO;
import cn.iocoder.yudao.module.agri.dal.dataobject.device.DeviceDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DeviceMapper extends BaseMapperX<DeviceDO> {

    default PageResult<DeviceDO> selectPage(DevicePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<DeviceDO>()
                .likeIfPresent(DeviceDO::getName, reqVO.getName())
                .eqIfPresent(DeviceDO::getDeviceType, reqVO.getDeviceType())
                .eqIfPresent(DeviceDO::getFieldId, reqVO.getFieldId())
                .eqIfPresent(DeviceDO::getStatus, reqVO.getStatus())
                .orderByDesc(DeviceDO::getId));
    }

    default DeviceDO selectByDeviceCode(String deviceCode) {
        return selectOne(DeviceDO::getDeviceCode, deviceCode);
    }

}
