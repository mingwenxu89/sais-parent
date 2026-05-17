package cn.iocoder.yudao.module.infra.enums.codegen;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Code generator field filter enum
 */
@AllArgsConstructor
@Getter
public enum CodegenColumnListConditionEnum {

    EQ("="),
    NE("!="),
    GT(">"),
    GTE(">="),
    LT("<"),
    LTE("<="),
    LIKE("LIKE"),
    BETWEEN("BETWEEN");

    /**
     * Conditions
     */
    private final String condition;

}
