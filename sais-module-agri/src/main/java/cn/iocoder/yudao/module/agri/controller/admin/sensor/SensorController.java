package cn.iocoder.yudao.module.agri.controller.admin.sensor;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import cn.iocoder.yudao.module.agri.controller.admin.sensor.vo.SensorPageReqVO;
import cn.iocoder.yudao.module.agri.controller.admin.sensor.vo.SensorRespVO;
import cn.iocoder.yudao.module.agri.controller.admin.sensor.vo.SensorSaveReqVO;
import cn.iocoder.yudao.module.agri.service.sensor.SensorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.IOException;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.EXPORT;

@Tag(name = "Admin - Sensor")
@RestController
@RequestMapping("/agri/sensor")
@Validated
public class SensorController {

    @Resource
    private SensorService sensorService;

    @PostMapping("/create")
    @Operation(summary = "Create sensor")
    @PreAuthorize("@ss.hasPermission('agri:sensor:create')")
    public CommonResult<Long> createSensor(@RequestBody @Valid SensorSaveReqVO createReqVO) {
        return success(sensorService.createSensor(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "Update sensor")
    @PreAuthorize("@ss.hasPermission('agri:sensor:update')")
    public CommonResult<Boolean> updateSensor(@RequestBody @Valid SensorSaveReqVO updateReqVO) {
        sensorService.updateSensor(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "Delete sensor")
    @Parameter(name = "id", description = "Sensor ID", required = true)
    @PreAuthorize("@ss.hasPermission('agri:sensor:delete')")
    public CommonResult<Boolean> deleteSensor(@RequestParam("id") Long id) {
        sensorService.deleteSensor(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "Get sensor")
    @Parameter(name = "id", description = "Sensor ID", required = true)
    @PreAuthorize("@ss.hasPermission('agri:sensor:query')")
    public CommonResult<SensorRespVO> getSensor(@RequestParam("id") Long id) {
        return success(sensorService.getSensor(id));
    }

    @GetMapping("/page")
    @Operation(summary = "Get sensor page")
    @PreAuthorize("@ss.hasPermission('agri:sensor:query')")
    public CommonResult<PageResult<SensorRespVO>> getSensorPage(@Valid SensorPageReqVO pageReqVO) {
        return success(sensorService.getSensorPage(pageReqVO));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "Export sensor Excel")
    @PreAuthorize("@ss.hasPermission('agri:sensor:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportSensorExcel(@Valid SensorPageReqVO pageReqVO, HttpServletResponse response) throws IOException {
        PageResult<SensorRespVO> pageResult = sensorService.getSensorPage(pageReqVO);
        ExcelUtils.write(response, "Sensors.xls", "Data", SensorRespVO.class, pageResult.getList());
    }

}
