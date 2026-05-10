package cn.iocoder.yudao.module.agri.controller.admin.crop;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import cn.iocoder.yudao.module.agri.controller.admin.crop.vo.CropPlanPageReqVO;
import cn.iocoder.yudao.module.agri.controller.admin.crop.vo.CropPlanRespVO;
import cn.iocoder.yudao.module.agri.controller.admin.crop.vo.CropPlanSaveReqVO;
import cn.iocoder.yudao.module.agri.service.crop.CropPlanService;
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

@Tag(name = "Admin - Crop Plan")
@RestController
@RequestMapping("/agri/crop-plan")
@Validated
public class CropPlanController {

    @Resource
    private CropPlanService cropPlanService;

    @PostMapping("/create")
    @Operation(summary = "Create crop plan")
    @PreAuthorize("@ss.hasPermission('agri:crop-plan:create')")
    public CommonResult<Long> createCropPlan(@RequestBody @Valid CropPlanSaveReqVO createReqVO) {
        return success(cropPlanService.createCropPlan(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "Update crop plan")
    @PreAuthorize("@ss.hasPermission('agri:crop-plan:update')")
    public CommonResult<Boolean> updateCropPlan(@RequestBody @Valid CropPlanSaveReqVO updateReqVO) {
        cropPlanService.updateCropPlan(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "Delete crop plan")
    @Parameter(name = "id", description = "Crop plan ID", required = true)
    @PreAuthorize("@ss.hasPermission('agri:crop-plan:delete')")
    public CommonResult<Boolean> deleteCropPlan(@RequestParam("id") Long id) {
        cropPlanService.deleteCropPlan(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "Get crop plan")
    @Parameter(name = "id", description = "Crop plan ID", required = true)
    @PreAuthorize("@ss.hasPermission('agri:crop-plan:query')")
    public CommonResult<CropPlanRespVO> getCropPlan(@RequestParam("id") Long id) {
        return success(cropPlanService.getCropPlan(id));
    }

    @GetMapping("/current")
    @Operation(summary = "Get the currently ongoing crop plan for a field")
    @Parameter(name = "fieldId", description = "Field ID", required = true)
    @PreAuthorize("@ss.hasPermission('agri:crop-plan:query')")
    public CommonResult<CropPlanRespVO> getCurrentCropPlan(@RequestParam Long fieldId) {
        return success(cropPlanService.getCurrentCropPlanByField(fieldId));
    }

    @GetMapping("/page")
    @Operation(summary = "Get crop plan page")
    @PreAuthorize("@ss.hasPermission('agri:crop-plan:query')")
    public CommonResult<PageResult<CropPlanRespVO>> getCropPlanPage(@Valid CropPlanPageReqVO pageReqVO) {
        return success(cropPlanService.getCropPlanPage(pageReqVO));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "Export crop plan Excel")
    @PreAuthorize("@ss.hasPermission('agri:crop-plan:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportCropPlanExcel(@Valid CropPlanPageReqVO pageReqVO, HttpServletResponse response) throws IOException {
        PageResult<CropPlanRespVO> pageResult = cropPlanService.getCropPlanPage(pageReqVO);
        ExcelUtils.write(response, "Crop Plan.xls", "Data", CropPlanRespVO.class, pageResult.getList());
    }

}
