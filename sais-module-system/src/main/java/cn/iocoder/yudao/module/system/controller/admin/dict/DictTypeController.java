package cn.iocoder.yudao.module.system.controller.admin.dict;

import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.module.system.controller.admin.dict.vo.type.DictTypePageReqVO;
import cn.iocoder.yudao.module.system.controller.admin.dict.vo.type.DictTypeRespVO;
import cn.iocoder.yudao.module.system.controller.admin.dict.vo.type.DictTypeSaveReqVO;
import cn.iocoder.yudao.module.system.controller.admin.dict.vo.type.DictTypeSimpleRespVO;
import cn.iocoder.yudao.module.system.dal.dataobject.dict.DictTypeDO;
import cn.iocoder.yudao.module.system.service.dict.DictTypeService;
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

import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.EXPORT;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "Management backend - dict type")
@RestController
@RequestMapping("/system/dict-type")
@Validated
public class DictTypeController {

    @Resource
    private DictTypeService dictTypeService;

    @PostMapping("/create")
    @Operation(summary = "Create dict type")
    @PreAuthorize("@ss.hasPermission('system:dict:create')")
    public CommonResult<Long> createDictType(@Valid @RequestBody DictTypeSaveReqVO createReqVO) {
        Long dictTypeId = dictTypeService.createDictType(createReqVO);
        return success(dictTypeId);
    }

    @PutMapping("/update")
    @Operation(summary = "Modify dict type")
    @PreAuthorize("@ss.hasPermission('system:dict:update')")
    public CommonResult<Boolean> updateDictType(@Valid @RequestBody DictTypeSaveReqVO updateReqVO) {
        dictTypeService.updateDictType(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "Delete dict type")
    @Parameter(name = "id", description = "ID", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('system:dict:delete')")
    public CommonResult<Boolean> deleteDictType(Long id) {
        dictTypeService.deleteDictType(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Operation(summary = "Delete dict types in batches")
    @Parameter(name = "ids", description = "ID list", required = true)
    @PreAuthorize("@ss.hasPermission('system:dict:delete')")
    public CommonResult<Boolean> deleteDictTypeList(@RequestParam("ids") List<Long> ids) {
        dictTypeService.deleteDictTypeList(ids);
        return success(true);
    }

    @GetMapping("/page")
    @Operation(summary = "Get a paginated list of dict type")
    @PreAuthorize("@ss.hasPermission('system:dict:query')")
    public CommonResult<PageResult<DictTypeRespVO>> pageDictTypes(@Valid DictTypePageReqVO pageReqVO) {
        PageResult<DictTypeDO> pageResult = dictTypeService.getDictTypePage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, DictTypeRespVO.class));
    }

    @Operation(summary = "/Query dict type details")
    @Parameter(name = "id", description = "ID", required = true, example = "1024")
    @GetMapping(value = "/get")
    @PreAuthorize("@ss.hasPermission('system:dict:query')")
    public CommonResult<DictTypeRespVO> getDictType(@RequestParam("id") Long id) {
        DictTypeDO dictType = dictTypeService.getDictType(id);
        return success(BeanUtils.toBean(dictType, DictTypeRespVO.class));
    }

    @GetMapping(value = {"/list-all-simple", "simple-list"})
    @Operation(summary = "Get a list of all dict types", description = "Including enabled + disabled dict types, mainly used for frontend drop-down options")
    // There is no need to add permission authentication, because the frontend requires it globally.
    public CommonResult<List<DictTypeSimpleRespVO>> getSimpleDictTypeList() {
        List<DictTypeDO> list = dictTypeService.getDictTypeList();
        return success(BeanUtils.toBean(list, DictTypeSimpleRespVO.class));
    }

    @Operation(summary = "Export data type")
    @GetMapping("/export-excel")
    @PreAuthorize("@ss.hasPermission('system:dict:query')")
    @ApiAccessLog(operateType = EXPORT)
    public void export(HttpServletResponse response, @Valid DictTypePageReqVO exportReqVO) throws IOException {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DictTypeDO> list = dictTypeService.getDictTypePage(exportReqVO).getList();
        // Export
        ExcelUtils.write(response, "Dict type.xls", "Data", DictTypeRespVO.class,
                BeanUtils.toBean(list, DictTypeRespVO.class));
    }

}
