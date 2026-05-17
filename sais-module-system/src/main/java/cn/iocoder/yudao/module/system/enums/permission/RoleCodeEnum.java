package cn.iocoder.yudao.module.system.enums.permission;

import cn.iocoder.yudao.framework.common.util.object.ObjectUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Role ID enum
 */
@Getter
@AllArgsConstructor
public enum RoleCodeEnum {

    SUPER_ADMIN("super_admin", "super administrator"),
    TENANT_ADMIN("tenant_admin", "Tenant Administrator"),
    CRM_ADMIN("crm_admin", "CRM Administrator"); // CRM System specific
    ;

    /**
     * role coding
     */
    private final String code;
    /**
     * name
     */
    private final String name;

    public static boolean isSuperAdmin(String code) {
        return ObjectUtils.equalsAny(code, SUPER_ADMIN.getCode());
    }

}
