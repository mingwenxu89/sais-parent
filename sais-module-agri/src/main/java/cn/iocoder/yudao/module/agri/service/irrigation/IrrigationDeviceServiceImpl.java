package cn.iocoder.yudao.module.agri.service.irrigation;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.agri.controller.admin.irrigation.vo.IrrigationDevicePageReqVO;
import cn.iocoder.yudao.module.agri.controller.admin.irrigation.vo.IrrigationDeviceRespVO;
import cn.iocoder.yudao.module.agri.controller.admin.irrigation.vo.IrrigationDeviceSaveReqVO;
import cn.iocoder.yudao.module.agri.convert.irrigation.IrrigationDeviceConvert;
import cn.iocoder.yudao.module.agri.dal.dataobject.farm.FarmDO;
import cn.iocoder.yudao.module.agri.dal.dataobject.field.FieldDO;
import cn.iocoder.yudao.module.agri.dal.dataobject.irrigation.IrrigationDeviceDO;
import cn.iocoder.yudao.module.agri.dal.dataobject.irrigation.IrrigationPlanDO;
import cn.iocoder.yudao.module.agri.dal.dataobject.sensor.SensorDO;
import cn.iocoder.yudao.module.agri.dal.mysql.farm.FarmMapper;
import cn.iocoder.yudao.module.agri.dal.mysql.field.FieldMapper;
import cn.iocoder.yudao.module.agri.dal.mysql.irrigation.IrrigationDeviceMapper;
import cn.iocoder.yudao.module.agri.dal.mysql.irrigation.IrrigationPlanMapper;
import cn.iocoder.yudao.module.agri.dal.mysql.sensor.SensorMapper;
import cn.iocoder.yudao.module.agri.framework.iot.AwsIotMqttClient;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.annotations.VisibleForTesting;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.agri.enums.ErrorCodeConstants.IRRIGATION_DEVICE_NOT_ACTIVE;
import static cn.iocoder.yudao.module.agri.enums.ErrorCodeConstants.IRRIGATION_DEVICE_NOT_EXISTS;

@Service
@Validated
@Slf4j
public class IrrigationDeviceServiceImpl implements IrrigationDeviceService {

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Resource
    private IrrigationDeviceMapper irrigationDeviceMapper;
    @Resource
    private IrrigationPlanMapper irrigationPlanMapper;
    @Resource
    private FieldMapper fieldMapper;
    @Resource
    private FarmMapper farmMapper;
    @Resource
    private SensorMapper sensorMapper;

    @Autowired(required = false)
    private AwsIotMqttClient mqttClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createIrrigationDevice(IrrigationDeviceSaveReqVO createReqVO) {
        FieldDO field = fieldMapper.selectById(createReqVO.getFieldId());
        IrrigationDeviceDO device = IrrigationDeviceConvert.INSTANCE.convert(createReqVO);
        device.setFarmId(field != null ? field.getFarmId() : null);
        device.setDeviceCode(String.format("IRR-%03d", irrigationDeviceMapper.nextVal()));
        device.setIsWatering(false);
        device.setSensorId(createReqVO.getSensorId());
        irrigationDeviceMapper.insert(device);
        return device.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateIrrigationDevice(IrrigationDeviceSaveReqVO updateReqVO) {
        IrrigationDeviceDO existing = validateDeviceExists(updateReqVO.getId());
        FieldDO field = fieldMapper.selectById(updateReqVO.getFieldId());
        IrrigationDeviceDO updateObj = IrrigationDeviceConvert.INSTANCE.convert(updateReqVO);
        updateObj.setId(updateReqVO.getId());
        updateObj.setFarmId(field != null ? field.getFarmId() : null);
        updateObj.setDeviceCode(existing.getDeviceCode());
        updateObj.setIsWatering(existing.getIsWatering());
        // updateById skips null fields; use update() to force-write sensorId/simulateFault even when cleared
        irrigationDeviceMapper.update(updateObj,
                new LambdaUpdateWrapper<IrrigationDeviceDO>()
                        .set(IrrigationDeviceDO::getSensorId, updateReqVO.getSensorId())
                        .set(updateReqVO.getSimulateFault() != null,
                                IrrigationDeviceDO::getSimulateFault, updateReqVO.getSimulateFault())
                        .eq(IrrigationDeviceDO::getId, updateReqVO.getId()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteIrrigationDevice(Long id) {
        validateDeviceExists(id);
        irrigationDeviceMapper.deleteById(id);
    }

    @Override
    public IrrigationDeviceRespVO getIrrigationDevice(Long id) {
        IrrigationDeviceDO device = irrigationDeviceMapper.selectById(id);
        if (device == null) {
            return null;
        }
        return convertWithNames(device);
    }

    @Override
    public PageResult<IrrigationDeviceRespVO> getIrrigationDevicePage(IrrigationDevicePageReqVO pageReqVO) {
        PageResult<IrrigationDeviceDO> page = irrigationDeviceMapper.selectPage(pageReqVO);
        List<IrrigationDeviceRespVO> list = new ArrayList<>();
        for (IrrigationDeviceDO device : page.getList()) {
            list.add(convertWithNames(device));
        }
        return new PageResult<>(list, page.getTotal());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void startWatering(Long id, Integer durationMinutes) {
        IrrigationDeviceDO device = validateDeviceExists(id);
        if (device.getStatus() != 1) {
            throw exception(IRRIGATION_DEVICE_NOT_ACTIVE);
        }
        int duration = (durationMinutes != null && durationMinutes > 0) ? durationMinutes : 30;
        LocalDateTime now = LocalDateTime.now();

        // Mark device as watering
        IrrigationDeviceDO deviceUpdate = new IrrigationDeviceDO();
        deviceUpdate.setId(id);
        deviceUpdate.setIsWatering(true);
        irrigationDeviceMapper.updateById(deviceUpdate);

        // Create an EXECUTING plan to represent this immediate manual start
        IrrigationPlanDO plan = new IrrigationPlanDO();
        plan.setDeviceId(id);
        plan.setFarmId(device.getFarmId());
        plan.setFieldId(device.getFieldId());
        plan.setDecisionSource("MANUAL");
        plan.setPlannedStartTime(now);
        plan.setPlannedDuration(duration);
        plan.setStatus("EXECUTING");
        plan.setActualStartTime(now);
        irrigationPlanMapper.insert(plan);

        publishCommand(device, plan.getId(), duration, "START");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void stopWatering(Long id) {
        IrrigationDeviceDO device = validateDeviceExists(id);

        // Mark device as not watering
        IrrigationDeviceDO deviceUpdate = new IrrigationDeviceDO();
        deviceUpdate.setId(id);
        deviceUpdate.setIsWatering(false);
        irrigationDeviceMapper.updateById(deviceUpdate);

        // Complete the executing plan
        IrrigationPlanDO executingPlan = irrigationPlanMapper.selectExecutingByDeviceId(id);
        if (executingPlan != null) {
            LocalDateTime endTime = LocalDateTime.now();
            long actualMinutes = java.time.temporal.ChronoUnit.MINUTES.between(
                    executingPlan.getActualStartTime(), endTime);
            BigDecimal waterQuantity = null;
            if (device.getFlowRate() != null && actualMinutes > 0) {
                waterQuantity = device.getFlowRate().multiply(BigDecimal.valueOf(actualMinutes));
            }
            IrrigationPlanDO update = new IrrigationPlanDO();
            update.setId(executingPlan.getId());
            update.setStatus("COMPLETED");
            update.setActualEndTime(endTime);
            update.setWaterQuantity(waterQuantity);
            irrigationPlanMapper.updateById(update);

            publishCommand(device, executingPlan.getId(), executingPlan.getPlannedDuration(), "STOP");
        }
    }

    private void publishCommand(IrrigationDeviceDO device, Long planId, Integer durationMinutes, String action) {
        if (mqttClient == null) {
            return;
        }
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("action", action);
            payload.put("planId", planId);
            payload.put("deviceId", device.getId());
            payload.put("durationMinutes", durationMinutes);
            String topic = mqttClient.getTopicPrefix() + "/" + device.getDeviceCode() + "/command";
            mqttClient.publish(topic, objectMapper.writeValueAsString(payload));
            log.info("[IrrigationDevice] Published {} to {}", action, topic);
        } catch (Exception e) {
            log.error("[IrrigationDevice] Failed to publish MQTT {} for planId={}", action, planId, e);
        }
    }

    private IrrigationDeviceRespVO convertWithNames(IrrigationDeviceDO device) {
        IrrigationDeviceRespVO resp = IrrigationDeviceConvert.INSTANCE.convert(device);
        FieldDO field = fieldMapper.selectById(device.getFieldId());
        FarmDO farm = device.getFarmId() != null ? farmMapper.selectById(device.getFarmId()) : null;
        if (field != null) {
            resp.setFieldName(field.getFieldName());
        }
        if (farm != null) {
            resp.setFarmName(farm.getFarmName());
        }
        if (Boolean.TRUE.equals(device.getIsWatering())) {
            IrrigationPlanDO executingPlan = irrigationPlanMapper.selectExecutingByDeviceId(device.getId());
            if (executingPlan != null) {
                resp.setWateringStartedAt(executingPlan.getActualStartTime());
            }
        }
        if (device.getSensorId() != null) {
            resp.setSensorId(device.getSensorId());
            SensorDO sensor = sensorMapper.selectById(device.getSensorId());
            if (sensor != null) {
                resp.setSensorCode(sensor.getSensorCode());
            }
        }
        return resp;
    }

    @VisibleForTesting
    public IrrigationDeviceDO validateDeviceExists(Long id) {
        if (id == null) {
            return null;
        }
        IrrigationDeviceDO device = irrigationDeviceMapper.selectById(id);
        if (device == null) {
            throw exception(IRRIGATION_DEVICE_NOT_EXISTS);
        }
        return device;
    }

}
