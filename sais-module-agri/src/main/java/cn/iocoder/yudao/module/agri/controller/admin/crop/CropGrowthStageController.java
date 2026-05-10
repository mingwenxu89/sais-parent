package cn.iocoder.yudao.module.agri.controller.admin.crop;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import cn.iocoder.yudao.module.agri.controller.admin.crop.vo.CropGrowthStagePageReqVO;
import cn.iocoder.yudao.module.agri.controller.admin.crop.vo.CropGrowthStageRespVO;
import cn.iocoder.yudao.module.agri.controller.admin.crop.vo.CropGrowthStageSaveReqVO;
import cn.iocoder.yudao.module.agri.convert.crop.CropGrowthStageConvert;
import cn.iocoder.yudao.module.agri.dal.dataobject.crop.CropGrowthStageDO;
import cn.iocoder.yudao.module.agri.service.crop.CropGrowthStageService;
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

@Tag(name = "Admin - Crop Growth Stage")
@RestController
@RequestMapping("/agri/crop-growth-stage")
@Validated
public class CropGrowthStageController {

    @Resource
    private CropGrowthStageService cropGrowthStageService;

    @PostMapping("/create")
    @Operation(summary = "Create crop growth stage")
    @PreAuthorize("@ss.hasPermission('agri:crop:create')")
    public CommonResult<Long> createCropGrowthStage(@RequestBody @Valid CropGrowthStageSaveReqVO createReqVO) {
        return success(cropGrowthStageService.createCropGrowthStage(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "Update crop growth stage")
    @PreAuthorize("@ss.hasPermission('agri:crop:update')")
    public CommonResult<Boolean> updateCropGrowthStage(@RequestBody @Valid CropGrowthStageSaveReqVO updateReqVO) {
        cropGrowthStageService.updateCropGrowthStage(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "Delete crop growth stage")
    @Parameter(name = "id", description = "Growth stage ID", required = true)
    @PreAuthorize("@ss.hasPermission('agri:crop:delete')")
    public CommonResult<Boolean> deleteCropGrowthStage(@RequestParam("id") Long id) {
        cropGrowthStageService.deleteCropGrowthStage(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "Get crop growth stage")
    @Parameter(name = "id", description = "Growth stage ID", required = true)
    @PreAuthorize("@ss.hasPermission('agri:crop:query')")
    public CommonResult<CropGrowthStageRespVO> getCropGrowthStage(@RequestParam("id") Long id) {
        CropGrowthStageDO cropGrowthStage = cropGrowthStageService.getCropGrowthStage(id);
        return success(CropGrowthStageConvert.INSTANCE.convert(cropGrowthStage));
    }

    @GetMapping("/page")
    @Operation(summary = "Get crop growth stage page")
    @PreAuthorize("@ss.hasPermission('agri:crop:query')")
    public CommonResult<PageResult<CropGrowthStageRespVO>> getCropGrowthStagePage(@Valid CropGrowthStagePageReqVO pageReqVO) {
        PageResult<CropGrowthStageDO> pageResult = cropGrowthStageService.getCropGrowthStagePage(pageReqVO);
        return success(CropGrowthStageConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/list-by-crop-id")
    @Operation(summary = "Get growth stage list by crop ID")
    @Parameter(name = "cropId", description = "Crop ID", required = true)
    @PreAuthorize("@ss.hasPermission('agri:crop:query')")
    public CommonResult<List<CropGrowthStageRespVO>> getCropGrowthStageListByCropId(@RequestParam("cropId") Long cropId) {
        List<CropGrowthStageDO> list = cropGrowthStageService.getCropGrowthStageListByCropId(cropId);
        return success(CropGrowthStageConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "Export crop growth stages to Excel")
    @PreAuthorize("@ss.hasPermission('agri:crop:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportCropGrowthStageExcel(@Valid CropGrowthStagePageReqVO pageReqVO, HttpServletResponse response) throws IOException {
        PageResult<CropGrowthStageDO> pageResult = cropGrowthStageService.getCropGrowthStagePage(pageReqVO);
        List<CropGrowthStageRespVO> list = CropGrowthStageConvert.INSTANCE.convertList(pageResult.getList());
        ExcelUtils.write(response, "crop-growth-stages.xls", "data", CropGrowthStageRespVO.class, list);
    }

}
