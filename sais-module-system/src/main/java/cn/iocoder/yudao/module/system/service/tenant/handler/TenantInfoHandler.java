package cn.iocoder.yudao.module.system.service.tenant.handler;

import cn.iocoder.yudao.module.system.dal.dataobject.tenant.TenantDO;

/**
 * Tenant information processing
 * Purpose: Minimize the coupling of tenant logic into the system
 *
 * @author Yudao Source Code
 */
public interface TenantInfoHandler {

    /**
     * Execute relevant logic based on the incoming tenant information.
     * For example, when creating a user, the maximum account quota is exceeded
     *
     * @param tenant Tenant information
     */
    void handle(TenantDO tenant);

}
