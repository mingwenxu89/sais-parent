package cn.iocoder.yudao.module.agri.controller.admin.alert;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import cn.iocoder.yudao.module.agri.controller.admin.alert.vo.AlertHandleReqVO;
import cn.iocoder.yudao.module.agri.controller.admin.alert.vo.AlertPageReqVO;
import cn.iocoder.yudao.module.agri.controller.admin.alert.vo.AlertRespVO;
import cn.iocoder.yudao.module.agri.controller.admin.alert.vo.AlertSaveReqVO;
import cn.iocoder.yudao.module.agri.convert.alert.AlertConvert;
import cn.iocoder.yudao.module.agri.dal.dataobject.alert.AlertDO;
import cn.iocoder.yudao.module.agri.dal.dataobject.field.FieldDO;
import cn.iocoder.yudao.module.agri.dal.mysql.field.FieldMapper;
import cn.iocoder.yudao.module.agri.service.alert.AlertService;
import cn.iocoder.yudao.module.system.api.notify.NotifyMessageSendApi;
import cn.iocoder.yudao.module.system.api.notify.dto.NotifySendSingleToUserReqDTO;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.EXPORT;

@Tag(name = "Admin - Alert")
@RestController
@RequestMapping("/agri/alert")
@Validated
public class AlertController {

    @Resource
    private AlertService alertService;
    @Resource
    private FieldMapper fieldMapper;
    @Resource
    private NotifyMessageSendApi notifyMessageSendApi;

    @PostMapping("/create")
    @Operation(summary = "Create alert")
    @PreAuthorize("@ss.hasPermission('agri:alert:create')")
    public CommonResult<Long> createAlert(@RequestBody @Valid AlertSaveReqVO createReqVO) {
        return success(alertService.createAlert(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "Update alert")
    @PreAuthorize("@ss.hasPermission('agri:alert:update')")
    public CommonResult<Boolean> updateAlert(@RequestBody @Valid AlertSaveReqVO updateReqVO) {
        alertService.updateAlert(updateReqVO);
        return success(true);
    }

    @PutMapping("/handle")
    @Operation(summary = "Handle alert (HANDLING / RESOLVED / IGNORED)")
    @PreAuthorize("@ss.hasPermission('agri:alert:update')")
    public CommonResult<Boolean> handleAlert(@RequestBody @Valid AlertHandleReqVO handleReqVO) {
        alertService.handleAlert(handleReqVO.getId(), handleReqVO.getStatus());
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "Delete alert")
    @Parameter(name = "id", description = "Alert ID", required = true)
    @PreAuthorize("@ss.hasPermission('agri:alert:delete')")
    public CommonResult<Boolean> deleteAlert(@RequestParam("id") Long id) {
        alertService.deleteAlert(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "Get alert")
    @Parameter(name = "id", description = "Alert ID", required = true)
    @PreAuthorize("@ss.hasPermission('agri:alert:query')")
    public CommonResult<AlertRespVO> getAlert(@RequestParam("id") Long id) {
        AlertDO alert = alertService.getAlert(id);
        return success(AlertConvert.INSTANCE.convert(alert));
    }

    @GetMapping("/page")
    @Operation(summary = "Get alert page")
    @PreAuthorize("@ss.hasPermission('agri:alert:query')")
    public CommonResult<PageResult<AlertRespVO>> getAlertPage(@Valid AlertPageReqVO pageReqVO) {
        PageResult<AlertDO> pageResult = alertService.getAlertPage(pageReqVO);
        PageResult<AlertRespVO> voPage = AlertConvert.INSTANCE.convertPage(pageResult);
        Map<Long, String> fieldNameCache = new HashMap<>();
        voPage.getList().forEach(vo -> {
            if (vo.getFieldId() != null) {
                String name = fieldNameCache.computeIfAbsent(vo.getFieldId(), id -> {
                    FieldDO field = fieldMapper.selectById(id);
                    return field != null ? field.getFieldName() : null;
                });
                vo.setFieldName(name);
            }
        });
        return success(voPage);
    }

    @PostMapping("/trigger-test")
    @Operation(summary = "Trigger test alert (for demo)")
    @PreAuthorize("@ss.hasPermission('agri:alert:create')")
    public CommonResult<Boolean> triggerTestAlert(
            @RequestParam @NotBlank String type,
            @RequestParam(required = false) Long farmId) {
        alertService.triggerTestAlert(type, farmId);
        return success(true);
    }

    @PostMapping("/inject-weather")
    @Operation(summary = "Send extreme weather notification (for demo)")
    @PreAuthorize("@ss.hasPermission('agri:alert:create')")
    public CommonResult<Boolean> injectWeatherAlert(@RequestParam String scenario) {
        String level;
        String context;
        switch (scenario) {
            case "HEAVY_RAIN":
                level = "CRITICAL";
                context = "Extreme rainfall of 60.0mm forecast for tomorrow. High risk of flooding and crop waterlogging.";
                break;
            case "FROST":
                level = "CRITICAL";
                context = "Frost risk tomorrow: minimum temperature forecast -3.0°C. Protect crops immediately.";
                break;
            case "HEATWAVE":
                level = "CRITICAL";
                context = "Extreme heat tomorrow: maximum temperature forecast 40.0°C. Crops at high risk of heat stress.";
                break;
            default:
                throw new IllegalArgumentException("Unknown scenario: " + scenario);
        }
        Map<String, Object> params = new HashMap<>();
        params.put("level", level);
        params.put("alertType", "Extreme Weather");
        params.put("context", context);
        NotifySendSingleToUserReqDTO req = new NotifySendSingleToUserReqDTO();
        req.setUserId(getLoginUserId());
        req.setTemplateCode("agri_alert_raised");
        req.setTemplateParams(params);
        notifyMessageSendApi.sendSingleMessageToAdmin(req);
        return success(true);
    }

    @GetMapping("/export-excel")
    @Operation(summary = "Export alerts to Excel")
    @PreAuthorize("@ss.hasPermission('agri:alert:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportAlertExcel(@Valid AlertPageReqVO pageReqVO, HttpServletResponse response) throws IOException {
        PageResult<AlertDO> pageResult = alertService.getAlertPage(pageReqVO);
        List<AlertRespVO> list = AlertConvert.INSTANCE.convertList(pageResult.getList());
        ExcelUtils.write(response, "alerts.xls", "data", AlertRespVO.class, list);
    }

}
