package cn.iocoder.yudao.module.system.service.tenant.handler;

import java.util.Set;

/**
 * Tenant menu handling
 * Purpose: Minimize the coupling of tenant logic into the system
 *
 * @author Yudao Source Code
 */
public interface TenantMenuHandler {

    /**
     * Execute relevant logic based on the incoming tenant menu [full] list.
     * For example, when returning to the assignable menu, you can remove redundant
     *
     * @param menuIds Menu list
     */
    void handle(Set<Long> menuIds);

}
