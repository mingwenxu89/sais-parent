package cn.iocoder.yudao.module.agri.controller.admin.irrigation;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import cn.iocoder.yudao.module.agri.controller.admin.irrigation.vo.IrrigationDevicePageReqVO;
import cn.iocoder.yudao.module.agri.controller.admin.irrigation.vo.IrrigationDeviceRespVO;
import cn.iocoder.yudao.module.agri.controller.admin.irrigation.vo.IrrigationDeviceSaveReqVO;
import cn.iocoder.yudao.module.agri.service.irrigation.IrrigationDeviceService;
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

@Tag(name = "Admin - Irrigation Device")
@RestController
@RequestMapping("/agri/irrigation-device")
@Validated
public class IrrigationDeviceController {

    @Resource
    private IrrigationDeviceService irrigationDeviceService;

    @PostMapping("/create")
    @Operation(summary = "Create irrigation device")
    @PreAuthorize("@ss.hasPermission('agri:irrigation-device:create')")
    public CommonResult<Long> createIrrigationDevice(@RequestBody @Valid IrrigationDeviceSaveReqVO createReqVO) {
        return success(irrigationDeviceService.createIrrigationDevice(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "Update irrigation device")
    @PreAuthorize("@ss.hasPermission('agri:irrigation-device:update')")
    public CommonResult<Boolean> updateIrrigationDevice(@RequestBody @Valid IrrigationDeviceSaveReqVO updateReqVO) {
        irrigationDeviceService.updateIrrigationDevice(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "Delete irrigation device")
    @Parameter(name = "id", description = "Irrigation device ID", required = true)
    @PreAuthorize("@ss.hasPermission('agri:irrigation-device:delete')")
    public CommonResult<Boolean> deleteIrrigationDevice(@RequestParam("id") Long id) {
        irrigationDeviceService.deleteIrrigationDevice(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "Get irrigation device")
    @Parameter(name = "id", description = "Irrigation device ID", required = true)
    @PreAuthorize("@ss.hasPermission('agri:irrigation-device:query')")
    public CommonResult<IrrigationDeviceRespVO> getIrrigationDevice(@RequestParam("id") Long id) {
        return success(irrigationDeviceService.getIrrigationDevice(id));
    }

    @GetMapping("/page")
    @Operation(summary = "Get irrigation device page")
    @PreAuthorize("@ss.hasPermission('agri:irrigation-device:query')")
    public CommonResult<PageResult<IrrigationDeviceRespVO>> getIrrigationDevicePage(@Valid IrrigationDevicePageReqVO pageReqVO) {
        return success(irrigationDeviceService.getIrrigationDevicePage(pageReqVO));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "Export irrigation device Excel")
    @PreAuthorize("@ss.hasPermission('agri:irrigation-device:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportIrrigationDeviceExcel(@Valid IrrigationDevicePageReqVO pageReqVO, HttpServletResponse response) throws IOException {
        PageResult<IrrigationDeviceRespVO> pageResult = irrigationDeviceService.getIrrigationDevicePage(pageReqVO);
        ExcelUtils.write(response, "Irrigation Devices.xls", "Data", IrrigationDeviceRespVO.class, pageResult.getList());
    }

    @PostMapping("/start-watering/{id}")
    @Operation(summary = "Start watering")
    @Parameter(name = "id", description = "Irrigation device ID", required = true)
    @Parameter(name = "durationMinutes", description = "Planned duration in minutes (default 30)")
    @PreAuthorize("@ss.hasPermission('agri:irrigation-device:update')")
    public CommonResult<Boolean> startWatering(
            @PathVariable("id") Long id,
            @RequestParam(value = "durationMinutes", defaultValue = "30") Integer durationMinutes) {
        irrigationDeviceService.startWatering(id, durationMinutes);
        return success(true);
    }

    @PostMapping("/stop-watering/{id}")
    @Operation(summary = "Stop watering")
    @Parameter(name = "id", description = "Irrigation device ID", required = true)
    @PreAuthorize("@ss.hasPermission('agri:irrigation-device:update')")
    public CommonResult<Boolean> stopWatering(@PathVariable("id") Long id) {
        irrigationDeviceService.stopWatering(id);
        return success(true);
    }

}
