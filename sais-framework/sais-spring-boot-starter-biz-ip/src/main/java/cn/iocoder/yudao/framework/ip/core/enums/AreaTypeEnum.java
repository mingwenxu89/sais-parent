package cn.iocoder.yudao.framework.ip.core.enums;

import cn.iocoder.yudao.framework.common.core.ArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * Area type enum
 *
 * @author Yudao Source Code
 */
@AllArgsConstructor
@Getter
public enum AreaTypeEnum implements ArrayValuable<Integer> {

    COUNTRY(1, "nation"),
    PROVINCE(2, "province"),
    CITY(3, "City"),
    DISTRICT(4, "area"), // County, town, district, etc.
;

 public static final Integer[] ARRAYS = Arrays.stream(values()).map(AreaTypeEnum::getType).toArray(Integer[]::new);

 /**
     * type
 */
 private final Integer type;
 /**
     * name
 */
 private final String name;

 @Override
 public Integer[] array() {
 return ARRAYS;
 }
}
