package cn.iocoder.yudao.module.system.enums.permission;

import cn.iocoder.yudao.framework.common.core.ArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * Data range enumeration class
 *
 * Used to implement data-level permissions
 *
 * @author Yudao Source Code
 */
@Getter
@AllArgsConstructor
public enum DataScopeEnum implements ArrayValuable<Integer> {

    ALL(1), // All data permissions

    DEPT_CUSTOM(2), // Specify department data permissions
    DEPT_ONLY(3), // Department data permissions
    DEPT_AND_CHILD(4), // Data permissions for departments and below

    SELF(5); // Only personal data permissions

    /**
     * scope
     */
    private final Integer scope;

    public static final Integer[] ARRAYS = Arrays.stream(values()).map(DataScopeEnum::getScope).toArray(Integer[]::new);

    @Override
    public Integer[] array() {
        return ARRAYS;
    }

}
