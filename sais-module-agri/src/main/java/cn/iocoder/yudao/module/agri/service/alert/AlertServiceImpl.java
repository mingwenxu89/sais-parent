package cn.iocoder.yudao.module.agri.service.alert;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import cn.iocoder.yudao.module.agri.controller.admin.alert.vo.AlertPageReqVO;
import cn.iocoder.yudao.module.agri.controller.admin.alert.vo.AlertSaveReqVO;
import cn.iocoder.yudao.module.agri.convert.alert.AlertConvert;
import cn.iocoder.yudao.module.agri.dal.dataobject.alert.AlertDO;
import cn.iocoder.yudao.module.agri.dal.dataobject.irrigation.IrrigationDeviceDO;
import cn.iocoder.yudao.module.agri.dal.dataobject.sensor.SensorDO;
import cn.iocoder.yudao.module.agri.dal.mysql.alert.AlertMapper;
import cn.iocoder.yudao.module.agri.dal.mysql.irrigation.IrrigationDeviceMapper;
import cn.iocoder.yudao.module.agri.dal.mysql.sensor.SensorMapper;
import cn.iocoder.yudao.module.system.api.notify.NotifyMessageSendApi;
import cn.iocoder.yudao.module.system.api.notify.dto.NotifySendSingleToUserReqDTO;
import com.google.common.annotations.VisibleForTesting;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.agri.enums.ErrorCodeConstants.ALERT_NOT_EXISTS;

@Service
@Validated
@Slf4j
public class AlertServiceImpl implements AlertService {

    // ── Integer codes ─────────────────────────────────────────────────────────
    // alert_type: 1=SENSOR_ABNORMAL  2=EXTREME_WEATHER  3=IRRIGATION_ABNORMAL
    // level:      1=INFO  2=WARN  3=CRITICAL
    // status:     0=UNHANDLED  1=HANDLING  2=RESOLVED  3=IGNORED

    private static final Map<String, Integer> TYPE_CODE = Map.of(
            "SENSOR_ABNORMAL", 1, "EXTREME_WEATHER", 2, "IRRIGATION_ABNORMAL", 3);

    private static final Map<String, Integer> LEVEL_CODE = Map.of(
            "INFO", 1, "WARN", 2, "CRITICAL", 3);

    private static final Map<Integer, String> TYPE_LABEL = Map.of(
            1, "Sensor Abnormal", 2, "Extreme Weather", 3, "Irrigation Abnormal");

    private static final Map<Integer, String> LEVEL_LABEL = Map.of(
            1, "INFO", 2, "WARN", 3, "CRITICAL");

    private static final String NOTIFY_TEMPLATE_CODE = "agri_alert_raised";
    private static final Long NOTIFY_USER_ID = 1L;

    @Resource
    private AlertMapper alertMapper;
    @Resource
    private SensorMapper sensorMapper;
    @Resource
    private IrrigationDeviceMapper irrigationDeviceMapper;
    @Resource
    private NotifyMessageSendApi notifyMessageSendApi;

    @Override
    public Long createAlert(AlertSaveReqVO createReqVO) {
        AlertDO alert = AlertConvert.INSTANCE.convert(createReqVO);
        if (alert.getStatus() == null) {
            alert.setStatus(0); // UNHANDLED
        }
        if (alert.getTriggeredAt() == null) {
            alert.setTriggeredAt(LocalDateTime.now());
        }
        alertMapper.insert(alert);
        return alert.getId();
    }

    @Override
    public void updateAlert(AlertSaveReqVO updateReqVO) {
        validateAlertExists(updateReqVO.getId());
        AlertDO updateObj = AlertConvert.INSTANCE.convert(updateReqVO);
        alertMapper.updateById(updateObj);
    }

    @Override
    public void deleteAlert(Long id) {
        validateAlertExists(id);
        alertMapper.deleteById(id);
    }

    @Override
    public AlertDO getAlert(Long id) {
        return alertMapper.selectById(id);
    }

    @Override
    public PageResult<AlertDO> getAlertPage(AlertPageReqVO pageReqVO) {
        return alertMapper.selectPage(pageReqVO);
    }

    @Override
    public void raiseAlert(String alertType, String level, Long farmId, Long fieldId,
                           Long irrigationPlanId, String context) {
        AlertDO alert = new AlertDO();
        alert.setAlertType(TYPE_CODE.getOrDefault(alertType, 1));
        alert.setLevel(LEVEL_CODE.getOrDefault(level, 2));
        alert.setFarmId(farmId);
        alert.setFieldId(fieldId);
        alert.setIrrigationPlanId(irrigationPlanId);
        alert.setContext(context);
        alert.setStatus(0); // UNHANDLED
        alert.setTriggeredAt(LocalDateTime.now());
        alertMapper.insert(alert);
        log.info("[Alert] {} {} raised — farm={} field={}: {}", level, alertType, farmId, fieldId, context);
        sendAlertNotification(alert);
    }

    @Override
    public void triggerTestAlert(String type, Long farmId) {
        String level;
        String context;
        Long resolvedFieldId = null;

        switch (type) {
            case "SENSOR_ABNORMAL": {
                level = "CRITICAL";
                SensorDO sensor = sensorMapper.selectList(
                        new LambdaQueryWrapper<SensorDO>().last("LIMIT 1")).stream().findFirst().orElse(null);
                String sensorCode = sensor != null ? sensor.getSensorCode() : "SENSOR-001";
                resolvedFieldId = sensor != null ? sensor.getFieldId() : null;
                if (farmId == null && sensor != null) farmId = sensor.getFarmId();
                context = String.format(
                        "[TEST] Soil moisture 5.0%% is critically low — more than 15%% below the minimum threshold 20.0%% for growth stage 'Seedling'. Sensor: %s.",
                        sensorCode);
                break;
            }
            case "IRRIGATION_ABNORMAL": {
                level = "CRITICAL";
                IrrigationDeviceDO device = irrigationDeviceMapper.selectList(
                        new LambdaQueryWrapper<IrrigationDeviceDO>().last("LIMIT 1")).stream().findFirst().orElse(null);
                String deviceCode = device != null ? device.getDeviceCode() : "DEVICE-001";
                resolvedFieldId = device != null ? device.getFieldId() : null;
                if (farmId == null && device != null) farmId = device.getFarmId();
                String sensorCode = "N/A";
                if (device != null && device.getSensorId() != null) {
                    SensorDO sensor = sensorMapper.selectById(device.getSensorId());
                    if (sensor != null) sensorCode = sensor.getSensorCode();
                }
                context = String.format(
                        "[TEST] Irrigation plan started but no acknowledgement received from device %s (sensor: %s) within 5 minutes. Device may be offline or faulty.",
                        deviceCode, sensorCode);
                break;
            }
            default:
                throw new IllegalArgumentException("Unknown alert type: " + type);
        }
        raiseAlert(type, level, farmId, resolvedFieldId, null, context);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void sendAlertNotification(AlertDO alert) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("level", LEVEL_LABEL.getOrDefault(alert.getLevel(), String.valueOf(alert.getLevel())));
            params.put("alertType", TYPE_LABEL.getOrDefault(alert.getAlertType(), String.valueOf(alert.getAlertType())));
            params.put("context", alert.getContext() != null ? alert.getContext() : "");
            NotifySendSingleToUserReqDTO req = new NotifySendSingleToUserReqDTO();
            req.setUserId(NOTIFY_USER_ID);
            req.setTemplateCode(NOTIFY_TEMPLATE_CODE);
            req.setTemplateParams(params);
            notifyMessageSendApi.sendSingleMessageToAdmin(req);
        } catch (Exception e) {
            log.warn("[Alert] Failed to send in-app notification for alert {}: {}", alert.getId(), e.getMessage());
        }
    }

    @Override
    public void handleAlert(Long id, Integer status) {
        validateAlertExists(id);
        AlertDO update = new AlertDO();
        update.setId(id);
        update.setStatus(status);
        update.setHandledAt(LocalDateTime.now());
        alertMapper.updateById(update);
    }

    @Override
    public boolean hasActiveAlert(String alertType, Long farmId, Long fieldId) {
        Integer typeCode = TYPE_CODE.get(alertType);
        if (typeCode == null) {
            return false;
        }
        return alertMapper.existsActiveAlert(typeCode, farmId, fieldId);
    }

    @VisibleForTesting
    public AlertDO validateAlertExists(Long id) {
        if (id == null) {
            return null;
        }
        AlertDO alert = alertMapper.selectById(id);
        if (alert == null) {
            throw exception(ALERT_NOT_EXISTS);
        }
        return alert;
    }

}
