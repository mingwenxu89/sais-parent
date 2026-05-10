package cn.iocoder.yudao.module.agri.controller.admin.device;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import cn.iocoder.yudao.module.agri.controller.admin.device.vo.DevicePageReqVO;
import cn.iocoder.yudao.module.agri.controller.admin.device.vo.DeviceRespVO;
import cn.iocoder.yudao.module.agri.controller.admin.device.vo.DeviceSaveReqVO;
import cn.iocoder.yudao.module.agri.convert.device.DeviceConvert;
import cn.iocoder.yudao.module.agri.dal.dataobject.device.DeviceDO;
import cn.iocoder.yudao.module.agri.service.device.DeviceService;
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
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.EXPORT;

@Tag(name = "Admin - IoT Device")
@RestController
@RequestMapping("/agri/device")
@Validated
public class DeviceController {

    @Resource
    private DeviceService deviceService;

    @PostMapping("/create")
    @Operation(summary = "Create IoT device")
    @PreAuthorize("@ss.hasPermission('agri:device:create')")
    public CommonResult<Long> createDevice(@RequestBody @Valid DeviceSaveReqVO createReqVO) {
        return success(deviceService.createDevice(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "Update IoT device")
    @PreAuthorize("@ss.hasPermission('agri:device:update')")
    public CommonResult<Boolean> updateDevice(@RequestBody @Valid DeviceSaveReqVO updateReqVO) {
        deviceService.updateDevice(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "Delete IoT device")
    @Parameter(name = "id", description = "Device ID", required = true)
    @PreAuthorize("@ss.hasPermission('agri:device:delete')")
    public CommonResult<Boolean> deleteDevice(@RequestParam("id") Long id) {
        deviceService.deleteDevice(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "Get IoT device")
    @Parameter(name = "id", description = "Device ID", required = true)
    @PreAuthorize("@ss.hasPermission('agri:device:query')")
    public CommonResult<DeviceRespVO> getDevice(@RequestParam("id") Long id) {
        DeviceDO device = deviceService.getDevice(id);
        return success(DeviceConvert.INSTANCE.convert(device));
    }

    @GetMapping("/page")
    @Operation(summary = "Get IoT device page")
    @PreAuthorize("@ss.hasPermission('agri:device:query')")
    public CommonResult<PageResult<DeviceRespVO>> getDevicePage(@Valid DevicePageReqVO pageReqVO) {
        PageResult<DeviceDO> pageResult = deviceService.getDevicePage(pageReqVO);
        return success(DeviceConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "Export IoT devices to Excel")
    @PreAuthorize("@ss.hasPermission('agri:device:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportDeviceExcel(@Valid DevicePageReqVO pageReqVO, HttpServletResponse response) throws IOException {
        PageResult<DeviceDO> pageResult = deviceService.getDevicePage(pageReqVO);
        List<DeviceRespVO> list = DeviceConvert.INSTANCE.convertList(pageResult.getList());
        ExcelUtils.write(response, "iot-devices.xls", "data", DeviceRespVO.class, list);
    }

}
