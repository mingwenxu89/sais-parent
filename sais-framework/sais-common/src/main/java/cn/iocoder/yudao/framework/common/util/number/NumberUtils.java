package cn.iocoder.yudao.framework.common.util.number;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;

import java.math.BigDecimal;
import java.util.List;

/**
 * Number tool class, completing the functions of {@link cn.hutool.core.util.NumberUtil}
 *
 * @author Yudao Source Code
 */
public class NumberUtils {

 public static Long parseLong(String str) {
 return StrUtil.isNotEmpty(str) ? Long.valueOf(str): null;
 }

 public static Integer parseInt(String str) {
 return StrUtil.isNotEmpty(str) ? Integer.valueOf(str): null;
 }

 public static boolean isAllNumber(List<String> values) {
 if (CollUtil.isEmpty(values)) {
 return false;
 }
 for (String value: values) {
 if (!NumberUtil.isNumber(value)) {
 return false;
 }
 }
 return true;
 }

 /**
     * Get the distance between two points on the earth by latitude and longitude
 *
     * Reference <<a href="https://gitee.com/dromara/hutool/blob/1caabb586b1f95aec66a21d039c5695df5e0f4c1/hutool-core/src/main/java/cn/hutool/core/util/DistanceUtil.java">DistanceUtil</a>> implementation, which has been deleted by hutool
 *
     * @param lat1 Longitude 1
     * @param lng1 Latitude 1
     * @param lat2 Longitude 2
     * @param lng2 Latitude 2
     * @return Distance, unit: kilometers
 */
 public static double getDistance(double lat1, double lng1, double lat2, double lng2) {
 double radLat1 = lat1 * Math.PI / 180.0;
 double radLat2 = lat2 * Math.PI / 180.0;
 double a = radLat1 - radLat2;
 double b = lng1 * Math.PI / 180.0 - lng2 * Math.PI / 180.0;
 double distance = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
 + Math.cos(radLat1) * Math.cos(radLat2)
 * Math.pow(Math.sin(b / 2), 2)));
 distance = distance * 6378.137;
 distance = Math.round(distance * 10000d) / 10000d;
 return distance;
 }

 /**
     * Provides precise multiplication operations
 *
     * The difference between hutool {@link NumberUtil#mul(BigDecimal...)} is that if null exists, null is returned
 *
     * @param values multiple multiplicands
     * @return product
 */
 public static BigDecimal mul(BigDecimal... values) {
 for (BigDecimal value: values) {
 if (value == null) {
 return null;
 }
 }
 return NumberUtil.mul(values);
 }

}
