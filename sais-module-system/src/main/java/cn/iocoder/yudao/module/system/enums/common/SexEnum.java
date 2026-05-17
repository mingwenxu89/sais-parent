package cn.iocoder.yudao.module.system.enums.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Gender enumeration value
 *
 * @author Yudao Source Code
 */
@Getter
@AllArgsConstructor
public enum SexEnum {

    /** Male */
    MALE(1),
    /** Female */
    FEMALE(2),
    /* Unknown */
    UNKNOWN(0);

    /**
     * gender
     */
    private final Integer sex;

}
