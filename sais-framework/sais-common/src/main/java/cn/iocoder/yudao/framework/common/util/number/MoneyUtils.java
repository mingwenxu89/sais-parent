package cn.iocoder.yudao.framework.common.util.number;

import cn.hutool.core.math.Money;
import cn.hutool.core.util.NumberUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Amount tools
 *
 * @author Yudao Source Code
 */
public class MoneyUtils {

 /**
     * The number of decimal places for the amount
 */
 private static final int PRICE_SCALE = 2;

 /**
     * BigDecimal object corresponding to the percentage
 */
 public static final BigDecimal PERCENT_100 = BigDecimal.valueOf(100);

 /**
     * Calculate percentage amount, rounded
 *
     * @param price Amount
     * @param rate Percent, for example 56.77% then pass in 56.77
     * @return Percent amount
 */
 public static Integer calculateRatePrice(Integer price, Double rate) {
 return calculateRatePrice(price, rate, 0, RoundingMode.HALF_UP).intValue();
 }

 /**
     * Calculate the percentage amount and pass it down
 *
     * @param price Amount
     * @param rate Percent, for example 56.77% then pass in 56.77
     * @return Percent amount
 */
 public static Integer calculateRatePriceFloor(Integer price, Double rate) {
 return calculateRatePrice(price, rate, 0, RoundingMode.FLOOR).intValue();
 }

 /**
     * Calculate percentage amount
 *
     * @param price Amount (unit points)
     * @param count quantity
     * @param percent Discount (unit points), for example, 60.2%, then pass in 6020
     * @return Total product price
 */
 public static Integer calculator(Integer price, Integer count, Integer percent) {
 price = price * count;
 if (percent == null) {
 return price;
 }
 return MoneyUtils.calculateRatePriceFloor(price, (double) (percent / 100));
 }

 /**
     * Calculate percentage amount
 *
     * @param price Amount
     * @param rate Percent, for example 56.77% then pass in 56.77
     * @param scale Keep decimal places
     * @param roundingMode rounding mode
 */
 public static BigDecimal calculateRatePrice(Number price, Number rate, int scale, RoundingMode roundingMode) {
        return NumberUtil.toBigDecimal(price).multiply(NumberUtil.toBigDecimal(rate)) // multiply by
                .divide(BigDecimal.valueOf(100), scale, roundingMode); // Divide by 100
 }

 /**
     * points to yuan
 *
     * @param fen point
     * @return Yuan
 */
 public static BigDecimal fenToYuan(int fen) {
 return new Money(0, fen).getAmount();
 }

 /**
     * cent-to-unit (string)
 *
     * For example, when fen is 1, the result is 0.01
 *
     * @param fen point
     * @return Yuan
 */
 public static String fenToYuanStr(int fen) {
 return new Money(0, fen).toString();
 }

 /**
     * Amounts are multiplied and rounded by default.
 *
     * Number of digits: {@link #PRICE_SCALE}
 *
     * @param price Amount
     * @param count quantity
     * @return Amount multiplied result
 */
 public static BigDecimal priceMultiply(BigDecimal price, BigDecimal count) {
 if (price == null || count == null) {
 return null;
 }
 return price.multiply(count).setScale(PRICE_SCALE, RoundingMode.HALF_UP);
 }

 /**
     * Amounts are multiplied (percentage), rounded by default
 *
     * Number of digits: {@link #PRICE_SCALE}
 *
     * @param price Amount
     * @param percent percentage
     * @return Amount multiplied result
 */
 public static BigDecimal priceMultiplyPercent(BigDecimal price, BigDecimal percent) {
 if (price == null || percent == null) {
 return null;
 }
 return price.multiply(percent).divide(PERCENT_100, PRICE_SCALE, RoundingMode.HALF_UP);
 }

}
