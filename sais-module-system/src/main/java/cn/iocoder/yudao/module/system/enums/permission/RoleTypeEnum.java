package cn.iocoder.yudao.module.system.enums.permission;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleTypeEnum {

    /**
     * Built-in roles
     */
    SYSTEM(1),
    /**
     * Custom role
     */
    CUSTOM(2);

    private final Integer type;

}
