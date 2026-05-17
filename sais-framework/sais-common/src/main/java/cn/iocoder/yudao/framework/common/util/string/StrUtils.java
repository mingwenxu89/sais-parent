package cn.iocoder.yudao.framework.common.util.string;

import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import org.aspectj.lang.JoinPoint;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * String utility class
 *
 * @author Yudao Source Code
 */
public class StrUtils {

 public static String maxLength(CharSequence str, int maxLength) {
        return StrUtil.maxLength(str, maxLength - 3); // The reason for -3 is that this method will add... exactly
 }

 /**
     * Whether the given string starts with any string
     * Returns false for both the given string and if the array is empty.
 *
     * @param str given string
     * @param prefixes The starting string to be detected
 * @since 3.0.6
 */
 public static boolean startWithAny(String str, Collection<String> prefixes) {
 if (StrUtil.isEmpty(str) || ArrayUtil.isEmpty(prefixes)) {
 return false;
 }

 for (CharSequence suffix: prefixes) {
 if (StrUtil.startWith(str, suffix, false)) {
 return true;
 }
 }
 return false;
 }

 public static List<Long> splitToLong(String value, CharSequence separator) {
 long[] longs = StrUtil.splitToLong(value, separator);
 return Arrays.stream(longs).boxed().collect(Collectors.toList());
 }

 public static Set<Long> splitToLongSet(String value) {
 return splitToLongSet(value, StrPool.COMMA);
 }

 public static Set<Long> splitToLongSet(String value, CharSequence separator) {
 long[] longs = StrUtil.splitToLong(value, separator);
 return Arrays.stream(longs).boxed().collect(Collectors.toSet());
 }

 public static List<Integer> splitToInteger(String value, CharSequence separator) {
 int[] integers = StrUtil.splitToInt(value, separator);
 return Arrays.stream(integers).boxed().collect(Collectors.toList());
 }

 /**
     * Remove lines from a string that contain the specified string
 *
     * @param content string
     * @param sequence Contains the string
     * @return String after removal
 */
 public static String removeLineContains(String content, String sequence) {
 if (StrUtil.isEmpty(content) || StrUtil.isEmpty(sequence)) {
 return content;
 }
 return Arrays.stream(content.split("\n"))
.filter(line -> !line.contains(sequence))
.collect(Collectors.joining("\n"));
 }

 /**
     * Parameters of splicing method
 *
     * Special: exclude some parameters that cannot be serialized, such as ServletRequest, ServletResponse, MultipartFile
 *
     * @param joinPoint connection point
     * @return Parameters after splicing
 */
 public static String joinMethodArgs(JoinPoint joinPoint) {
 Object[] args = joinPoint.getArgs();
 if (ArrayUtil.isEmpty(args)) {
 return "";
 }
 return ArrayUtil.join(args, ",", item -> {
 if (item == null) {
 return "";
 }
            // The discussion can be found at: https://t.zsxq.com/XUJVk, https://t.zsxq.com/MnKcL
 String clazzName = item.getClass().getName();
 if (StrUtil.startWithAny(clazzName, "javax.servlet", "jakarta.servlet", "org.springframework.web")) {
 return "";
 }
 return item;
 });
 }

}
