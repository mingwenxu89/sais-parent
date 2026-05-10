package cn.iocoder.yudao.module.agri.service.device;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.agri.controller.admin.device.vo.DevicePageReqVO;
import cn.iocoder.yudao.module.agri.controller.admin.device.vo.DeviceSaveReqVO;
import cn.iocoder.yudao.module.agri.convert.device.DeviceConvert;
import cn.iocoder.yudao.module.agri.dal.dataobject.device.DeviceDO;
import cn.iocoder.yudao.module.agri.dal.mysql.device.DeviceMapper;
import com.google.common.annotations.VisibleForTesting;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.agri.enums.ErrorCodeConstants.DEVICE_CODE_DUPLICATE;
import static cn.iocoder.yudao.module.agri.enums.ErrorCodeConstants.DEVICE_NOT_EXISTS;

@Service
@Validated
@Slf4j
public class DeviceServiceImpl implements DeviceService {

    @Resource
    private DeviceMapper deviceMapper;

    @Override
    public Long createDevice(DeviceSaveReqVO createReqVO) {
        validateDeviceCodeUnique(null, createReqVO.getDeviceCode());
        DeviceDO device = DeviceConvert.INSTANCE.convert(createReqVO);
        deviceMapper.insert(device);
        return device.getId();
    }

    @Override
    public void updateDevice(DeviceSaveReqVO updateReqVO) {
        validateDeviceExists(updateReqVO.getId());
        validateDeviceCodeUnique(updateReqVO.getId(), updateReqVO.getDeviceCode());
        DeviceDO updateObj = DeviceConvert.INSTANCE.convert(updateReqVO);
        deviceMapper.updateById(updateObj);
    }

    @Override
    public void deleteDevice(Long id) {
        validateDeviceExists(id);
        deviceMapper.deleteById(id);
    }

    @Override
    public DeviceDO getDevice(Long id) {
        return deviceMapper.selectById(id);
    }

    @Override
    public PageResult<DeviceDO> getDevicePage(DevicePageReqVO pageReqVO) {
        return deviceMapper.selectPage(pageReqVO);
    }

    @VisibleForTesting
    public DeviceDO validateDeviceExists(Long id) {
        if (id == null) {
            return null;
        }
        DeviceDO device = deviceMapper.selectById(id);
        if (device == null) {
            throw exception(DEVICE_NOT_EXISTS);
        }
        return device;
    }

    @VisibleForTesting
    public void validateDeviceCodeUnique(Long id, String deviceCode) {
        DeviceDO device = deviceMapper.selectByDeviceCode(deviceCode);
        if (device == null) {
            return;
        }
        // If id is null, no need to check whether this is the same device
        if (id == null) {
            throw exception(DEVICE_CODE_DUPLICATE);
        }
        if (!device.getId().equals(id)) {
            throw exception(DEVICE_CODE_DUPLICATE);
        }
    }

}
