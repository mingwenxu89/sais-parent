package cn.iocoder.yudao.module.infra.enums.codegen;

import cn.iocoder.yudao.framework.common.util.object.ObjectUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * Code generation template type
 *
 * @author Yudao Source Code
 */
@AllArgsConstructor
@Getter
public enum CodegenTemplateTypeEnum {

    ONE(1), // Single table (CRUD)
    TREE(2), // Tree table (CRUD)

    MASTER_NORMAL(10), // Master-detail - master table - normal mode
    MASTER_ERP(11), // Master-detail - master table - ERP mode
    MASTER_INNER(12), // Master-detail - master table - embedded mode
    SUB(15), // Master-detail - detail table
    ;

    /**
     * Type
     */
    private final Integer type;

    /**
     * Whether it is the main table
     *
     * @param type Type
     * @return Is it the main table?
     */
    public static boolean isMaster(Integer type) {
        return ObjectUtils.equalsAny(type,
                MASTER_NORMAL.type, MASTER_ERP.type, MASTER_INNER.type);
    }

    /**
     * Whether it is a tree table
     *
     * @param type Type
     * @return Whether tree table
     */
    public static boolean isTree(Integer type) {
        return Objects.equals(type, TREE.type);
    }

}
