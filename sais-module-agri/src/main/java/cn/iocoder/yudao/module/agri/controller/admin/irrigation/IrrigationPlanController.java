package cn.iocoder.yudao.module.agri.controller.admin.irrigation;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import cn.iocoder.yudao.module.agri.controller.admin.irrigation.vo.IrrigationPlanPageReqVO;
import cn.iocoder.yudao.module.agri.controller.admin.irrigation.vo.IrrigationPlanRespVO;
import cn.iocoder.yudao.module.agri.controller.admin.irrigation.vo.IrrigationPlanSaveReqVO;
import cn.iocoder.yudao.module.agri.service.irrigation.IrrigationPlanService;
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

@Tag(name = "Admin - Irrigation Plan")
@RestController
@RequestMapping("/agri/irrigation-plan")
@Validated
public class IrrigationPlanController {

    @Resource
    private IrrigationPlanService irrigationPlanService;

    @PostMapping("/create")
    @Operation(summary = "Create irrigation plan")
    @PreAuthorize("@ss.hasPermission('agri:irrigation-plan:create')")
    public CommonResult<Long> createIrrigationPlan(@RequestBody @Valid IrrigationPlanSaveReqVO createReqVO) {
        return success(irrigationPlanService.createIrrigationPlan(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "Update irrigation plan")
    @PreAuthorize("@ss.hasPermission('agri:irrigation-plan:update')")
    public CommonResult<Boolean> updateIrrigationPlan(@RequestBody @Valid IrrigationPlanSaveReqVO updateReqVO) {
        irrigationPlanService.updateIrrigationPlan(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "Delete irrigation plan")
    @Parameter(name = "id", description = "Plan ID", required = true)
    @PreAuthorize("@ss.hasPermission('agri:irrigation-plan:delete')")
    public CommonResult<Boolean> deleteIrrigationPlan(@RequestParam("id") Long id) {
        irrigationPlanService.deleteIrrigationPlan(id);
        return success(true);
    }

    @PostMapping("/cancel")
    @Operation(summary = "Cancel irrigation plan")
    @Parameter(name = "id", description = "Plan ID", required = true)
    @PreAuthorize("@ss.hasPermission('agri:irrigation-plan:update')")
    public CommonResult<Boolean> cancelIrrigationPlan(@RequestParam("id") Long id) {
        irrigationPlanService.cancelIrrigationPlan(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "Get irrigation plan")
    @Parameter(name = "id", description = "Plan ID", required = true)
    @PreAuthorize("@ss.hasPermission('agri:irrigation-plan:query')")
    public CommonResult<IrrigationPlanRespVO> getIrrigationPlan(@RequestParam("id") Long id) {
        return success(irrigationPlanService.getIrrigationPlan(id));
    }

    @GetMapping("/page")
    @Operation(summary = "Get irrigation plan page")
    @PreAuthorize("@ss.hasPermission('agri:irrigation-plan:query')")
    public CommonResult<PageResult<IrrigationPlanRespVO>> getIrrigationPlanPage(@Valid IrrigationPlanPageReqVO pageReqVO) {
        return success(irrigationPlanService.getIrrigationPlanPage(pageReqVO));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "Export irrigation plans to Excel")
    @PreAuthorize("@ss.hasPermission('agri:irrigation-plan:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportIrrigationPlanExcel(@Valid IrrigationPlanPageReqVO pageReqVO, HttpServletResponse response) throws IOException {
        PageResult<IrrigationPlanRespVO> pageResult = irrigationPlanService.getIrrigationPlanPage(pageReqVO);
        ExcelUtils.write(response, "irrigation-plans.xls", "data", IrrigationPlanRespVO.class, pageResult.getList());
    }

}
