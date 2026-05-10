package cn.iocoder.yudao.module.agri.controller.admin.crop;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import cn.iocoder.yudao.module.agri.controller.admin.crop.vo.CropPageReqVO;
import cn.iocoder.yudao.module.agri.controller.admin.crop.vo.CropRespVO;
import cn.iocoder.yudao.module.agri.controller.admin.crop.vo.CropSaveReqVO;
import cn.iocoder.yudao.module.agri.convert.crop.CropConvert;
import cn.iocoder.yudao.module.agri.dal.dataobject.crop.CropDO;
import cn.iocoder.yudao.module.agri.service.crop.CropService;
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

@Tag(name = "Admin - Crop")
@RestController
@RequestMapping("/agri/crop")
@Validated
public class CropController {

    @Resource
    private CropService cropService;

    @PostMapping("/create")
    @Operation(summary = "Create crop")
    @PreAuthorize("@ss.hasPermission('agri:crop:create')")
    public CommonResult<Long> createCrop(@RequestBody @Valid CropSaveReqVO createReqVO) {
        return success(cropService.createCrop(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "Update crop")
    @PreAuthorize("@ss.hasPermission('agri:crop:update')")
    public CommonResult<Boolean> updateCrop(@RequestBody @Valid CropSaveReqVO updateReqVO) {
        cropService.updateCrop(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "Delete crop")
    @Parameter(name = "id", description = "Crop ID", required = true)
    @PreAuthorize("@ss.hasPermission('agri:crop:delete')")
    public CommonResult<Boolean> deleteCrop(@RequestParam("id") Long id) {
        cropService.deleteCrop(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "Get crop")
    @Parameter(name = "id", description = "Crop ID", required = true)
    @PreAuthorize("@ss.hasPermission('agri:crop:query')")
    public CommonResult<CropRespVO> getCrop(@RequestParam("id") Long id) {
        CropDO crop = cropService.getCrop(id);
        return success(CropConvert.INSTANCE.convert(crop));
    }

    @GetMapping("/page")
    @Operation(summary = "Get crop page")
    @PreAuthorize("@ss.hasPermission('agri:crop:query')")
    public CommonResult<PageResult<CropRespVO>> getCropPage(@Valid CropPageReqVO pageReqVO) {
        PageResult<CropDO> pageResult = cropService.getCropPage(pageReqVO);
        return success(CropConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "Export crops to Excel")
    @PreAuthorize("@ss.hasPermission('agri:crop:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportCropExcel(@Valid CropPageReqVO pageReqVO, HttpServletResponse response) throws IOException {
        PageResult<CropDO> pageResult = cropService.getCropPage(pageReqVO);
        List<CropRespVO> list = CropConvert.INSTANCE.convertList(pageResult.getList());
        ExcelUtils.write(response, "crops.xls", "data", CropRespVO.class, list);
    }

}
