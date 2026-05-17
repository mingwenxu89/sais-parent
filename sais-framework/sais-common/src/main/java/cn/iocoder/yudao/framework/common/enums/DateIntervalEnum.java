package cn.iocoder.yudao.framework.common.enums;

import cn.hutool.core.util.ArrayUtil;
import cn.iocoder.yudao.framework.common.core.ArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * Enumeration of time intervals
 *
 * @author dhb52
 */
@Getter
@AllArgsConstructor
public enum DateIntervalEnum implements ArrayValuable<Integer> {

    HOUR(0, "Hour"), // Special: This enumeration does not exist in the dictionary for the time being! ! ! Because in most cases, this interval is not used
    DAY(1, "sky"),
    WEEK(2, "week"),
    MONTH(3, "moon"),
    QUARTER(4, "quarter"),
    YEAR(5, "Year")
;

 public static final Integer[] ARRAYS = Arrays.stream(values()).map(DateIntervalEnum::getInterval).toArray(Integer[]::new);

 /**
     * type
 */
 private final Integer interval;
 /**
     * name
 */
 private final String name;

 @Override
 public Integer[] array() {
 return ARRAYS;
 }

 public static DateIntervalEnum valueOf(Integer interval) {
 return ArrayUtil.firstMatch(item -> item.getInterval().equals(interval), DateIntervalEnum.values());
 }

}