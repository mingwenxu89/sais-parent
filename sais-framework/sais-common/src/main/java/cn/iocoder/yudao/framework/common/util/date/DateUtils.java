package cn.iocoder.yudao.framework.common.util.date;

import cn.hutool.core.date.LocalDateTimeUtil;

import java.time.*;
import java.util.Calendar;
import java.util.Date;

/**
 * Time tools
 *
 * @author Yudao Source Code
 */
public class DateUtils {

 /**
     * Time zone - default
 */
 public static final String TIME_ZONE_DEFAULT = "GMT+8";

 /**
     * Convert seconds to milliseconds
 */
 public static final long SECOND_MILLIS = 1000;

 public static final String FORMAT_YEAR_MONTH_DAY = "yyyy-MM-dd";

 public static final String FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND = "yyyy-MM-dd HH:mm:ss";

 /**
     * Convert LocalDateTime to Date
 *
 * @param date LocalDateTime
 * @return LocalDateTime
 */
 public static Date of(LocalDateTime date) {
 if (date == null) {
 return null;
 }
        // Combine this datetime with a time zone to create a ZonedDateTime
 ZonedDateTime zonedDateTime = date.atZone(ZoneId.systemDefault());
        // Local Timeline LocalDateTime to Instant Timeline Instant Timestamp
 Instant instant = zonedDateTime.toInstant();
        // UTC time (Coordinated Universal Time, UTC + 00:00) to Beijing (Beijing, UTC + 8:00) time
 return Date.from(instant);
 }

 /**
     * Convert Date to LocalDateTime
 *
 * @param date Date
 * @return LocalDateTime
 */
 public static LocalDateTime of(Date date) {
 if (date == null) {
 return null;
 }
        // Convert to timestamp
 Instant instant = date.toInstant();
        // UTC time (Coordinated Universal Time, UTC + 00:00) to Beijing (Beijing, UTC + 8:00) time
 return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
 }

 public static Date addTime(Duration duration) {
 return new Date(System.currentTimeMillis() + duration.toMillis());
 }

 public static boolean isExpired(LocalDateTime time) {
 LocalDateTime now = LocalDateTime.now();
 return now.isAfter(time);
 }

 /**
     * Create specified time
 *
     * @param year Year
     * @param month moon
     * @param day day
     * @return specified time
 */
 public static Date buildTime(int year, int month, int day) {
 return buildTime(year, month, day, 0, 0, 0);
 }

 /**
     * Create specified time
 *
     * @param year Year
     * @param month moon
     * @param day day
     * @param hour Hour
     * @param minute minute
     * @param second Second
     * @return specified time
 */
 public static Date buildTime(int year, int month, int day,
 int hour, int minute, int second) {
 Calendar calendar = Calendar.getInstance();
 calendar.set(Calendar.YEAR, year);
 calendar.set(Calendar.MONTH, month - 1);
 calendar.set(Calendar.DAY_OF_MONTH, day);
 calendar.set(Calendar.HOUR_OF_DAY, hour);
 calendar.set(Calendar.MINUTE, minute);
 calendar.set(Calendar.SECOND, second);
        calendar.set(Calendar.MILLISECOND, 0); // Normally, this is 0 milliseconds
 return calendar.getTime();
 }

 public static Date max(Date a, Date b) {
 if (a == null) {
 return b;
 }
 if (b == null) {
 return a;
 }
 return a.compareTo(b) > 0 ? a: b;
 }

 public static LocalDateTime max(LocalDateTime a, LocalDateTime b) {
 if (a == null) {
 return b;
 }
 if (b == null) {
 return a;
 }
 return a.isAfter(b) ? a: b;
 }

 /**
     * whether today
 *
     * @param date date
     * @return whether
 */
 public static boolean isToday(LocalDateTime date) {
 return LocalDateTimeUtil.isSameDay(date, LocalDateTime.now());
 }

 /**
     * whether yesterday
 *
     * @param date date
     * @return whether
 */
 public static boolean isYesterday(LocalDateTime date) {
 return LocalDateTimeUtil.isSameDay(date, LocalDateTime.now().minusDays(1));
 }

}
