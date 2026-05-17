package cn.iocoder.yudao.framework.tenant.core.db;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Extend the BaseDO base class for multi-tenancy
 *
 * @author Yudao Source Code
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class TenantBaseDO extends BaseDO {

 /**
     * multi-tenant ID
 */
 private Long tenantId;

}
