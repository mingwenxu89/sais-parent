package cn.iocoder.yudao.module.agri.service.sensordata;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.agri.dal.dataobject.sensordata.SensorDataDO;
import cn.iocoder.yudao.module.agri.dal.mysql.sensordata.SensorDataMapper;
import cn.iocoder.yudao.module.agri.service.alert.AlertCheckService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class SensorDataServiceImpl implements SensorDataService {

    @Resource
    private SensorDataMapper sensorDataMapper;

    @Resource
    private AlertCheckService alertCheckService;

    @Override
    public void recordSensorData(SensorDataDO data) {
        sensorDataMapper.insert(data);
        alertCheckService.checkSensorData(data);
    }

    @Override
    public PageResult<SensorDataDO> getSensorDataPage(Long sensorId, PageParam page) {
        return sensorDataMapper.selectPageBySensorId(sensorId, page);
    }

    @Override
    public List<SensorDataDO> getLatestByField(Long fieldId) {
        return sensorDataMapper.selectLatestByFieldId(fieldId);
    }

}
