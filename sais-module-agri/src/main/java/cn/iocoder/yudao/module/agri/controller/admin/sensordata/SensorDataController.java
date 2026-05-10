package cn.iocoder.yudao.module.agri.controller.admin.sensordata;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.agri.controller.admin.sensordata.vo.SensorDataRespVO;
import cn.iocoder.yudao.module.agri.dal.dataobject.field.FieldDO;
import cn.iocoder.yudao.module.agri.dal.dataobject.sensor.SensorDO;
import cn.iocoder.yudao.module.agri.dal.dataobject.sensordata.SensorDataDO;
import cn.iocoder.yudao.module.agri.dal.mysql.field.FieldMapper;
import cn.iocoder.yudao.module.agri.dal.mysql.sensor.SensorMapper;
import cn.iocoder.yudao.module.agri.service.sensordata.SensorDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "Admin - Sensor Data")
@RestController
@RequestMapping("/agri/sensor-data")
@Validated
public class SensorDataController {

    @Resource
    private SensorDataService sensorDataService;
    @Resource
    private SensorMapper sensorMapper;
    @Resource
    private FieldMapper fieldMapper;

    @GetMapping("/page")
    @Operation(summary = "Get sensor data page by sensor ID")
    @Parameter(name = "sensorId", description = "Sensor ID")
    @PreAuthorize("@ss.hasPermission('agri:sensor:query')")
    public CommonResult<PageResult<SensorDataRespVO>> getSensorDataPage(
            @RequestParam(value = "sensorId", required = false) Long sensorId,
            @Valid PageParam page) {
        PageResult<SensorDataDO> result = sensorDataService.getSensorDataPage(sensorId, page);

        // Cache sensors and fields to avoid repeated DB lookups
        Map<Long, SensorDO> sensorCache = new java.util.HashMap<>();
        Map<Long, FieldDO> fieldCache = new java.util.HashMap<>();

        List<SensorDataRespVO> list = result.getList().stream().map(do_ -> {
            SensorDO sensor = sensorCache.computeIfAbsent(do_.getSensorId(), sensorMapper::selectById);
            String resolvedSensorCode = sensor != null ? sensor.getSensorCode() : null;
            String resolvedFieldName = null;
            if (sensor != null && sensor.getFieldId() != null) {
                FieldDO field = fieldCache.computeIfAbsent(sensor.getFieldId(), fieldMapper::selectById);
                if (field != null) resolvedFieldName = field.getFieldName();
            }
            SensorDataRespVO vo = new SensorDataRespVO();
            vo.setId(do_.getId());
            vo.setSensorCode(resolvedSensorCode);
            vo.setFieldName(resolvedFieldName);
            vo.setDataType(do_.getDataType());
            vo.setValue(do_.getValue());
            vo.setCollectedAt(do_.getCollectedAt());
            return vo;
        }).collect(Collectors.toList());
        return success(new PageResult<>(list, result.getTotal()));
    }

    @PostMapping("/inject")
    @Operation(summary = "Inject a synthetic sensor reading to trigger real alert detection")
    @PreAuthorize("@ss.hasPermission('agri:sensor:create')")
    public CommonResult<Boolean> injectSensorData(
            @RequestParam String dataType,
            @RequestParam BigDecimal value,
            @RequestParam(required = false) Long sensorId,
            @RequestParam(required = false) Long farmId,
            @RequestParam(required = false) Long fieldId) {
        if (sensorId == null) {
            SensorDO any = sensorMapper.selectList(
                    new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<SensorDO>().last("LIMIT 1"))
                    .stream().findFirst().orElse(null);
            if (any != null) {
                sensorId = any.getId();
                if (farmId == null) farmId = any.getFarmId();
                if (fieldId == null) fieldId = any.getFieldId();
            }
        }
        SensorDataDO data = new SensorDataDO();
        data.setSensorId(sensorId);
        data.setFarmId(farmId);
        data.setFieldId(fieldId);
        data.setDataType(dataType);
        data.setValue(value);
        data.setCollectedAt(LocalDateTime.now());
        sensorDataService.recordSensorData(data);
        return success(true);
    }

    @GetMapping("/latest-by-field")
    @Operation(summary = "Get latest reading per sensor for a field (one reading per sensor)")
    @Parameter(name = "fieldId", description = "Field ID", required = true)
    @PreAuthorize("@ss.hasPermission('agri:sensor:query')")
    public CommonResult<Map<Long, SensorDataRespVO>> getLatestByField(@RequestParam Long fieldId) {
        List<SensorDataDO> doList = sensorDataService.getLatestByField(fieldId);

        FieldDO field = fieldMapper.selectById(fieldId);
        String fieldName = field != null ? field.getFieldName() : null;

        Map<Long, SensorDataRespVO> resultMap = doList.stream().collect(Collectors.toMap(
                SensorDataDO::getSensorId,
                do_ -> {
                    SensorDO sensor = sensorMapper.selectById(do_.getSensorId());
                    SensorDataRespVO vo = new SensorDataRespVO();
                    vo.setSensorCode(sensor != null ? sensor.getSensorCode() : null);
                    vo.setFieldName(fieldName);
                    vo.setDataType(do_.getDataType());
                    vo.setValue(do_.getValue());
                    vo.setCollectedAt(do_.getCollectedAt());
                    return vo;
                }
        ));
        return success(resultMap);
    }

}
