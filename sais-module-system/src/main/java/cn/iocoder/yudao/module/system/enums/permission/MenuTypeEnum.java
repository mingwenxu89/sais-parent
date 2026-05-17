package cn.iocoder.yudao.module.system.enums.permission;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Menu type enumeration class
 *
 * @author Yudao Source Code
 */
@Getter
@AllArgsConstructor
public enum MenuTypeEnum {

    DIR(1), // Directory
    MENU(2), // Menu
    BUTTON(3) // button
    ;

    /**
     * Type
     */
    private final Integer type;

}
