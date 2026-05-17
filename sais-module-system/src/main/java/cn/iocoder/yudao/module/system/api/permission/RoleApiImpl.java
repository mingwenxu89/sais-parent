package cn.iocoder.yudao.module.system.api.permission;

import cn.iocoder.yudao.module.system.service.permission.RoleService;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.Collection;

/**
 * Role API implementation class
 *
 * @author Yudao Source Code
 */
@Service
public class RoleApiImpl implements RoleApi {

    @Resource
    private RoleService roleService;

    @Override
    public void validRoleList(Collection<Long> ids) {
        roleService.validateRoleList(ids);
    }
}
