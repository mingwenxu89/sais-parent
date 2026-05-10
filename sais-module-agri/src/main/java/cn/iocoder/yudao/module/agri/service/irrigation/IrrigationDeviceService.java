package cn.iocoder.yudao.module.agri.service.irrigation;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.agri.controller.admin.irrigation.vo.IrrigationDevicePageReqVO;
import cn.iocoder.yudao.module.agri.controller.admin.irrigation.vo.IrrigationDeviceRespVO;
import cn.iocoder.yudao.module.agri.controller.admin.irrigation.vo.IrrigationDeviceSaveReqVO;

import jakarta.validation.Valid;

public interface IrrigationDeviceService {

    Long createIrrigationDevice(@Valid IrrigationDeviceSaveReqVO createReqVO);

    void updateIrrigationDevice(@Valid IrrigationDeviceSaveReqVO updateReqVO);

    void deleteIrrigationDevice(Long id);

    IrrigationDeviceRespVO getIrrigationDevice(Long id);

    PageResult<IrrigationDeviceRespVO> getIrrigationDevicePage(IrrigationDevicePageReqVO pageReqVO);

    /**
     * Manually start watering immediately. Creates an EXECUTING plan and publishes an MQTT START command.
     *
     * @param durationMinutes planned duration; use 30 as the default if not specified
     */
    void startWatering(Long id, Integer durationMinutes);

    void stopWatering(Long id);

}
