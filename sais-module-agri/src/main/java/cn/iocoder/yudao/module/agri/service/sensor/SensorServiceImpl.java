package cn.iocoder.yudao.module.agri.service.sensor;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.agri.controller.admin.sensor.vo.SensorPageReqVO;
import cn.iocoder.yudao.module.agri.controller.admin.sensor.vo.SensorRespVO;
import cn.iocoder.yudao.module.agri.controller.admin.sensor.vo.SensorSaveReqVO;
import cn.iocoder.yudao.module.agri.convert.sensor.SensorConvert;
import cn.iocoder.yudao.module.agri.dal.dataobject.farm.FarmDO;
import cn.iocoder.yudao.module.agri.dal.dataobject.field.FieldDO;
import cn.iocoder.yudao.module.agri.dal.dataobject.sensor.SensorDO;
import cn.iocoder.yudao.module.agri.dal.dataobject.irrigation.IrrigationDeviceDO;
import cn.iocoder.yudao.module.agri.dal.mysql.farm.FarmMapper;
import cn.iocoder.yudao.module.agri.dal.mysql.field.FieldMapper;
import cn.iocoder.yudao.module.agri.dal.mysql.irrigation.IrrigationDeviceMapper;
import cn.iocoder.yudao.module.agri.dal.mysql.sensor.SensorMapper;
import com.google.common.annotations.VisibleForTesting;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.agri.enums.ErrorCodeConstants.SENSOR_NOT_EXISTS;

@Service
@Validated
@Slf4j
public class SensorServiceImpl implements SensorService {

    @Resource
    private SensorMapper sensorMapper;
    @Resource
    private FarmMapper farmMapper;
    @Resource
    private FieldMapper fieldMapper;
    @Resource
    private IrrigationDeviceMapper irrigationDeviceMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createSensor(SensorSaveReqVO createReqVO) {
        SensorDO sensor = SensorConvert.INSTANCE.convert(createReqVO);
        sensor.setSensorCode(String.format("SNS-%03d", sensorMapper.nextVal()));
        sensorMapper.insert(sensor);
        return sensor.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSensor(SensorSaveReqVO updateReqVO) {
        validateSensorExists(updateReqVO.getId());
        SensorDO updateObj = SensorConvert.INSTANCE.convert(updateReqVO);
        sensorMapper.updateById(updateObj);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSensor(Long id) {
        validateSensorExists(id);
        sensorMapper.deleteById(id);
    }

    @Override
    public SensorRespVO getSensor(Long id) {
        SensorDO sensor = sensorMapper.selectById(id);
        if (sensor == null) {
            return null;
        }
        return convertWithNames(sensor);
    }

    @Override
    public PageResult<SensorRespVO> getSensorPage(SensorPageReqVO pageReqVO) {
        Set<Long> excludedSensorIds = Collections.emptySet();
        if (pageReqVO.getExcludeDeviceId() != null && pageReqVO.getFieldId() != null) {
            excludedSensorIds = irrigationDeviceMapper.selectListByFieldId(pageReqVO.getFieldId()).stream()
                    .filter(d -> !Objects.equals(d.getId(), pageReqVO.getExcludeDeviceId()) && d.getSensorId() != null)
                    .map(IrrigationDeviceDO::getSensorId)
                    .collect(Collectors.toSet());
        }
        PageResult<SensorDO> page = excludedSensorIds.isEmpty()
                ? sensorMapper.selectPage(pageReqVO)
                : sensorMapper.selectPage(pageReqVO, excludedSensorIds);
        List<SensorRespVO> list = new ArrayList<>();
        for (SensorDO sensor : page.getList()) {
            list.add(convertWithNames(sensor));
        }
        return new PageResult<>(list, page.getTotal());
    }

    private SensorRespVO convertWithNames(SensorDO sensor) {
        SensorRespVO resp = SensorConvert.INSTANCE.convert(sensor);
        FarmDO farm = sensor.getFarmId() != null ? farmMapper.selectById(sensor.getFarmId()) : null;
        FieldDO field = sensor.getFieldId() != null ? fieldMapper.selectById(sensor.getFieldId()) : null;
        if (farm != null) {
            resp.setFarmName(farm.getFarmName());
        }
        if (field != null) {
            resp.setFieldName(field.getFieldName());
        }
        return resp;
    }

    @VisibleForTesting
    public SensorDO validateSensorExists(Long id) {
        if (id == null) {
            return null;
        }
        SensorDO sensor = sensorMapper.selectById(id);
        if (sensor == null) {
            throw exception(SENSOR_NOT_EXISTS);
        }
        return sensor;
    }

}
