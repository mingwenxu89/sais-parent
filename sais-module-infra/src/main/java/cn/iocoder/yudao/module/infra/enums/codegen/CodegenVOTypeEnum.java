package cn.iocoder.yudao.module.infra.enums.codegen;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Code-generated VO type enumeration
 *
 * Current role: When adding, modifying, and responding to a Controller, should VO or DO be used?
 * Note: Controller’s paging parameters are not included!
 *
 * @author Yudao Source Code
 */
@AllArgsConstructor
@Getter
public enum CodegenVOTypeEnum {

    VO(10, "VO"),
    DO(20, "DO");

    /**
     * scene
     */
    private final Integer type;
    /**
     * scene name
     */
    private final String name;

}
