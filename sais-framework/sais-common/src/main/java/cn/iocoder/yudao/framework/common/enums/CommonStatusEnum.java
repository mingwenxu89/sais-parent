package cn.iocoder.yudao.framework.common.enums;

import cn.hutool.core.util.ObjUtil;
import cn.iocoder.yudao.framework.common.core.ArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * Generic status enum
 *
 * @author Yudao Source Code
 */
@Getter
@AllArgsConstructor
public enum CommonStatusEnum implements ArrayValuable<Integer> {

    ENABLE(0, "turn on"),
    DISABLE(1, "closure");

 public static final Integer[] ARRAYS = Arrays.stream(values()).map(CommonStatusEnum::getStatus).toArray(Integer[]::new);

 /**
     * status value
 */
 private final Integer status;
 /**
     * status name
 */
 private final String name;

 @Override
 public Integer[] array() {
 return ARRAYS;
 }

 public static boolean isEnable(Integer status) {
 return ObjUtil.equal(ENABLE.status, status);
 }

 public static boolean isDisable(Integer status) {
 return ObjUtil.equal(DISABLE.status, status);
 }

}
