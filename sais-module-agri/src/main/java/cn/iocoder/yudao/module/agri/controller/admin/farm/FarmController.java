package cn.iocoder.yudao.module.agri.controller.admin.farm;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.agri.controller.admin.farm.vo.FarmPageReqVO;
import cn.iocoder.yudao.module.agri.controller.admin.farm.vo.FarmRespVO;
import cn.iocoder.yudao.module.agri.controller.admin.farm.vo.FarmSaveReqVO;
import cn.iocoder.yudao.module.agri.convert.farm.FarmConvert;
import cn.iocoder.yudao.module.agri.dal.dataobject.farm.FarmDO;
import cn.iocoder.yudao.module.agri.service.farm.FarmService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "Admin - Farm")
@RestController
@RequestMapping("/agri/farm")
@Validated
public class FarmController {

    @Resource
    private FarmService farmInfoService;

    @GetMapping("/current")
    @Operation(summary = "Get current tenant's farm")
    @PreAuthorize("@ss.hasPermission('agri:field:query')")
    public CommonResult<FarmRespVO> getCurrentFarm() {
        FarmDO farmInfo = farmInfoService.getCurrentFarm();
        return success(FarmConvert.INSTANCE.convert(farmInfo));
    }

    @PutMapping("/current")
    @Operation(summary = "Save current tenant's farm")
    @PreAuthorize("@ss.hasPermission('agri:field:update')")
    public CommonResult<Long> saveCurrentFarm(@RequestBody @Valid FarmSaveReqVO reqVO) {
        return success(farmInfoService.saveCurrentFarm(reqVO));
    }

    @GetMapping("/page")
    @Operation(summary = "Get farm page")
    @PreAuthorize("@ss.hasPermission('agri:field:query')")
    public CommonResult<PageResult<FarmDO>> getFarmPage(@Valid FarmPageReqVO pageReqVO) {
        return success(farmInfoService.getFarmPage(pageReqVO));
    }

}
