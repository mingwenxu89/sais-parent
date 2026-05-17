package cn.iocoder.yudao.framework.common.enums;

import cn.iocoder.yudao.framework.common.core.ArrayValuable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * Enumeration of terminals
 *
 * @author Yudao Source Code
 */
@RequiredArgsConstructor
@Getter
public enum TerminalEnum implements ArrayValuable<Integer> {

    UNKNOWN(0, "unknown"), // Purpose: Use it when terminal cannot be resolved
    WECHAT_MINI_PROGRAM(10, "WeChat applet"),
    WECHAT_WAP(11, "WeChat public account"),
    H5(20, "H5 web page"),
    APP(31, "Mobile App"),
;

 public static final Integer[] ARRAYS = Arrays.stream(values()).map(TerminalEnum::getTerminal).toArray(Integer[]::new);

 /**
     * terminal
 */
 private final Integer terminal;
 /**
     * terminal name
 */
 private final String name;

 @Override
 public Integer[] array() {
 return ARRAYS;
 }
}
