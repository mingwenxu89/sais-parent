package cn.iocoder.yudao.module.system.api.permission;

import java.util.Collection;

/**
 * Role API API
 *
 * @author Yudao Source Code
 */
public interface RoleApi {

    /**
     * Verify that the characters are valid. The following situations will be deemed invalid:
     * 1. The character ID does not exist
     * 2. The character is disabled
     *
     * @param ids Role ID array
     */
    void validRoleList(Collection<Long> ids);

}
