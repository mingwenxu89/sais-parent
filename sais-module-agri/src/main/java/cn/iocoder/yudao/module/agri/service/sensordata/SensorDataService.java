package cn.iocoder.yudao.module.agri.service.sensordata;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.agri.dal.dataobject.sensordata.SensorDataDO;

import java.util.List;

public interface SensorDataService {

    void recordSensorData(SensorDataDO data);

    PageResult<SensorDataDO> getSensorDataPage(Long sensorId, PageParam page);

    List<SensorDataDO> getLatestByField(Long fieldId);

}
