package cn.iocoder.yudao.module.system.controller.app.tenant;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.tenant.core.aop.TenantIgnore;
import cn.iocoder.yudao.module.system.controller.app.tenant.vo.AppTenantRespVO;
import cn.iocoder.yudao.module.system.dal.dataobject.tenant.TenantDO;
import cn.iocoder.yudao.module.system.service.tenant.TenantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import jakarta.annotation.security.PermitAll;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "User App - Tenant")
@RestController
@RequestMapping("/system/tenant")
public class AppTenantController {

    @Resource
    private TenantService tenantService;

    @GetMapping("/get-by-website")
    @PermitAll
    @TenantIgnore
    @Operation(summary = "Use domain name to obtain tenant information", description = "Obtain tenant information based on the user's domain name")
    @Parameter(name = "website", description = "domain name", required = true, example = "www.iocoder.cn")
    public CommonResult<AppTenantRespVO> getTenantByWebsite(@RequestParam("website") String website) {
        TenantDO tenant = tenantService.getTenantByWebsite(website);
        if (tenant == null || CommonStatusEnum.isDisable(tenant.getStatus())) {
            return success(null);
        }
        return success(BeanUtils.toBean(tenant, AppTenantRespVO.class));
    }

}
