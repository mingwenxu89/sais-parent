package cn.iocoder.yudao.framework.common.enums;

import cn.hutool.core.util.ArrayUtil;
import cn.iocoder.yudao.framework.common.core.ArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * Global user type enumeration
 */
@AllArgsConstructor
@Getter
public enum UserTypeEnum implements ArrayValuable<Integer> {

    MEMBER(1, "member"), // For c-side, ordinary users
    ADMIN(2, "administrator"); // Facing b-end, management backend

 public static final Integer[] ARRAYS = Arrays.stream(values()).map(UserTypeEnum::getValue).toArray(Integer[]::new);

 /**
     * type
 */
 private final Integer value;
 /**
     * Type name
 */
 private final String name;

 public static UserTypeEnum valueOf(Integer value) {
 return ArrayUtil.firstMatch(userType -> userType.getValue().equals(value), UserTypeEnum.values());
 }

 @Override
 public Integer[] array() {
 return ARRAYS;
 }
}
