package cn.iocoder.yudao.module.agri.service.sensor;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.agri.controller.admin.sensor.vo.SensorPageReqVO;
import cn.iocoder.yudao.module.agri.controller.admin.sensor.vo.SensorRespVO;
import cn.iocoder.yudao.module.agri.controller.admin.sensor.vo.SensorSaveReqVO;

import jakarta.validation.Valid;

public interface SensorService {

    Long createSensor(@Valid SensorSaveReqVO createReqVO);

    void updateSensor(@Valid SensorSaveReqVO updateReqVO);

    void deleteSensor(Long id);

    SensorRespVO getSensor(Long id);

    PageResult<SensorRespVO> getSensorPage(SensorPageReqVO pageReqVO);

}
