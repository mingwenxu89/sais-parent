package cn.iocoder.yudao.module.agri.service.device;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.agri.controller.admin.device.vo.DevicePageReqVO;
import cn.iocoder.yudao.module.agri.controller.admin.device.vo.DeviceSaveReqVO;
import cn.iocoder.yudao.module.agri.dal.dataobject.device.DeviceDO;

import jakarta.validation.Valid;

/**
 * IoT Device Service interface
 */
public interface DeviceService {

    Long createDevice(@Valid DeviceSaveReqVO createReqVO);

    void updateDevice(@Valid DeviceSaveReqVO updateReqVO);

    void deleteDevice(Long id);

    DeviceDO getDevice(Long id);

    PageResult<DeviceDO> getDevicePage(DevicePageReqVO pageReqVO);

}
