package cn.iocoder.yudao.module.agri.service.sensorreporting;

import cn.iocoder.yudao.module.agri.controller.admin.sensorreporting.vo.SensorReportingManualReqVO;
import cn.iocoder.yudao.module.agri.controller.admin.sensorreporting.vo.SensorReportingStatusRespVO;

public interface SensorReportingService {

    SensorReportingStatusRespVO getStatus();

    SensorReportingStatusRespVO start(Integer intervalSeconds);

    SensorReportingStatusRespVO stop();

    SensorReportingStatusRespVO reportAllTenantsRandom();

    void reportManual(SensorReportingManualReqVO reqVO);

}
