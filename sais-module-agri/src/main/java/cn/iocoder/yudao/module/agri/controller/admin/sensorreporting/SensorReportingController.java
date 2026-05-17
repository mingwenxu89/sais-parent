package cn.iocoder.yudao.module.agri.controller.admin.sensorreporting;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.agri.controller.admin.sensorreporting.vo.SensorReportingManualReqVO;
import cn.iocoder.yudao.module.agri.controller.admin.sensorreporting.vo.SensorReportingStartReqVO;
import cn.iocoder.yudao.module.agri.controller.admin.sensorreporting.vo.SensorReportingStatusRespVO;
import cn.iocoder.yudao.module.agri.service.sensorreporting.SensorReportingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "Admin - Sensor Reporting")
@RestController
@RequestMapping("/agri/sensor-reporting")
@Validated
public class SensorReportingController {

    @Resource
    private SensorReportingService sensorReportingService;

    @GetMapping("/status")
    @Operation(summary = "Get sensor reporting status")
    @PreAuthorize("@ss.hasPermission('agri:sensor:query')")
    public CommonResult<SensorReportingStatusRespVO> getStatus() {
        return success(sensorReportingService.getStatus());
    }

    @PostMapping("/start")
    @Operation(summary = "Start periodic sensor reporting")
    @PreAuthorize("@ss.hasPermission('agri:sensor:query')")
    public CommonResult<SensorReportingStatusRespVO> start(@RequestBody @Valid SensorReportingStartReqVO reqVO) {
        return success(sensorReportingService.start(reqVO.getIntervalSeconds()));
    }

    @PostMapping("/stop")
    @Operation(summary = "Stop periodic sensor reporting")
    @PreAuthorize("@ss.hasPermission('agri:sensor:query')")
    public CommonResult<SensorReportingStatusRespVO> stop() {
        return success(sensorReportingService.stop());
    }

    @PostMapping("/manual-report")
    @Operation(summary = "Publish one manual sensor reading")
    @PreAuthorize("@ss.hasPermission('agri:sensor:create')")
    public CommonResult<Boolean> reportManual(@RequestBody @Valid SensorReportingManualReqVO reqVO) {
        sensorReportingService.reportManual(reqVO);
        return success(true);
    }

}
