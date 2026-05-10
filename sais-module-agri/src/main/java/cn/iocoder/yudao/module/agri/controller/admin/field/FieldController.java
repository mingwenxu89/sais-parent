package cn.iocoder.yudao.module.agri.controller.admin.field;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import cn.iocoder.yudao.module.agri.controller.admin.field.vo.FieldPageReqVO;
import cn.iocoder.yudao.module.agri.controller.admin.field.vo.FieldRespVO;
import cn.iocoder.yudao.module.agri.controller.admin.field.vo.FieldSaveReqVO;
import cn.iocoder.yudao.module.agri.convert.field.FieldConvert;
import cn.iocoder.yudao.module.agri.dal.dataobject.field.FieldDO;
import cn.iocoder.yudao.module.agri.service.field.FieldService;
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

@Tag(name = "Admin - Field")
@RestController
@RequestMapping("/agri/field")
@Validated
public class FieldController {

    @Resource
    private FieldService fieldService;

    @PostMapping("/create")
    @Operation(summary = "Create field")
    @PreAuthorize("@ss.hasPermission('agri:field:create')")
    public CommonResult<Long> createField(@RequestBody @Valid FieldSaveReqVO createReqVO) {
        return success(fieldService.createField(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "Update field")
    @PreAuthorize("@ss.hasPermission('agri:field:update')")
    public CommonResult<Boolean> updateField(@RequestBody @Valid FieldSaveReqVO updateReqVO) {
        fieldService.updateField(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "Delete field")
    @Parameter(name = "id", description = "Field ID", required = true)
    @PreAuthorize("@ss.hasPermission('agri:field:delete')")
    public CommonResult<Boolean> deleteField(@RequestParam("id") Long id) {
        fieldService.deleteField(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "Get field")
    @Parameter(name = "id", description = "Field ID", required = true)
    @PreAuthorize("@ss.hasPermission('agri:field:query')")
    public CommonResult<FieldRespVO> getField(@RequestParam("id") Long id) {
        FieldDO fieldInfo = fieldService.getField(id);
        return success(FieldConvert.INSTANCE.convert(fieldInfo));
    }

    @GetMapping("/page")
    @Operation(summary = "Get field page")
    @PreAuthorize("@ss.hasPermission('agri:field:query')")
    public CommonResult<PageResult<FieldRespVO>> getFieldPage(@Valid FieldPageReqVO pageReqVO) {
        PageResult<FieldDO> pageResult = fieldService.getFieldPage(pageReqVO);
        return success(FieldConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "Export fields to Excel")
    @PreAuthorize("@ss.hasPermission('agri:field:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportFieldExcel(@Valid FieldPageReqVO pageReqVO, HttpServletResponse response) throws IOException {
        PageResult<FieldDO> pageResult = fieldService.getFieldPage(pageReqVO);
        List<FieldRespVO> list = FieldConvert.INSTANCE.convertList(pageResult.getList());
        ExcelUtils.write(response, "fields.xls", "data", FieldRespVO.class, list);
    }

}
