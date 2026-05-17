package cn.iocoder.yudao.module.system.api.tenant;

import cn.iocoder.yudao.framework.common.biz.system.tenant.TenantCommonApi;
import cn.iocoder.yudao.module.system.service.tenant.TenantService;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.List;

/**
 * Multi-tenant API implementation class
 *
 * @author Yudao Source Code
 */
@Service
public class TenantApiImpl implements TenantCommonApi {

    @Resource
    private TenantService tenantService;

    @Override
    public List<Long> getTenantIdList() {
        return tenantService.getTenantIdList();
    }

    @Override
    public void validateTenant(Long id) {
        tenantService.validTenant(id);
    }

}
