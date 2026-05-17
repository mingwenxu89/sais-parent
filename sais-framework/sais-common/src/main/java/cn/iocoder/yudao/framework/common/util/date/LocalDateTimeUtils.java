package cn.iocoder.yudao.framework.common.util.date;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.date.TemporalAccessorUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.enums.DateIntervalEnum;

import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

import static cn.hutool.core.date.DatePattern.*;

/**
 * Time utility class for {@link LocalDateTime}
 *
 * @author Yudao Source Code
 */
public class LocalDateTimeUtils {

 /**
     * Empty LocalDateTime object, mainly used as the default value for DB unique indexes
 */
 public static LocalDateTime EMPTY = buildTime(1970, 1, 1);

 public static DateTimeFormatter UTC_MS_WITH_XXX_OFFSET_FORMATTER = createFormatter(UTC_MS_WITH_XXX_OFFSET_PATTERN);

 /**
     * parsing time
 *
     * Compared with the {@link LocalDateTimeUtil#parse(CharSequence)} method, it will try its best to parse until it succeeds
 *
     * @param time time
     * @return time string
 */
 public static LocalDateTime parse(String time) {
 try {
 return LocalDateTimeUtil.parse(time, DatePattern.NORM_DATE_PATTERN);
 } catch (DateTimeParseException e) {
 return LocalDateTimeUtil.parse(time);
 }
 }

 public static LocalDateTime addTime(Duration duration) {
 return LocalDateTime.now().plus(duration);
 }

 public static LocalDateTime minusTime(Duration duration) {
 return LocalDateTime.now().minus(duration);
 }

 public static boolean beforeNow(LocalDateTime date) {
 return date.isBefore(LocalDateTime.now());
 }

 public static boolean afterNow(LocalDateTime date) {
 return date.isAfter(LocalDateTime.now());
 }

 /**
     * Create specified time
 *
     * @param year Year
     * @param month moon
     * @param day day
     * @return specified time
 */
 public static LocalDateTime buildTime(int year, int month, int day) {
 return LocalDateTime.of(year, month, day, 0, 0, 0);
 }

 public static LocalDateTime[] buildBetweenTime(int year1, int month1, int day1,
 int year2, int month2, int day2) {
 return new LocalDateTime[]{buildTime(year1, month1, day1), buildTime(year2, month2, day2)};
 }

 /**
     * Determine whether the judgment time is within this time range
 *
     * @param startTime start time
     * @param endTime end time
     * @param time specified time
     * @return whether
 */
 public static boolean isBetween(LocalDateTime startTime, LocalDateTime endTime, Timestamp time) {
 if (startTime == null || endTime == null || time == null) {
 return false;
 }
 return LocalDateTimeUtil.isIn(LocalDateTimeUtil.of(time), startTime, endTime);
 }

 /**
     * Determine whether the judgment time is within this time range
 *
     * @param startTime start time
     * @param endTime end time
     * @param time specified time
     * @return whether
 */
 public static boolean isBetween(LocalDateTime startTime, LocalDateTime endTime, String time) {
 if (startTime == null || endTime == null || time == null) {
 return false;
 }
 return LocalDateTimeUtil.isIn(parse(time), startTime, endTime);
 }

 /**
     * Determine whether the current time is within the time range
 *
     * @param startTime start time
     * @param endTime end time
     * @return whether
 */
 public static boolean isBetween(LocalDateTime startTime, LocalDateTime endTime) {
 if (startTime == null || endTime == null) {
 return false;
 }
 return LocalDateTimeUtil.isIn(LocalDateTime.now(), startTime, endTime);
 }

 /**
     * Determine whether the current time is within the time range
 *
     * @param startTime start time
     * @param endTime end time
     * @return whether
 */
 public static boolean isBetween(String startTime, String endTime) {
 if (startTime == null || endTime == null) {
 return false;
 }
 LocalDate nowDate = LocalDate.now();
 return LocalDateTimeUtil.isIn(LocalDateTime.now(),
 LocalDateTime.of(nowDate, LocalTime.parse(startTime)),
 LocalDateTime.of(nowDate, LocalTime.parse(endTime)));
 }

 /**
     * Determine whether time periods overlap
 *
     * @param startTime1 Start time1
     * @param endTime1 end time1
     * @param startTime2 Start time2
     * @param endTime2 end time2
     * @return Overlap: true No overlap: false
 */
 public static boolean isOverlap(LocalTime startTime1, LocalTime endTime1, LocalTime startTime2, LocalTime endTime2) {
 LocalDate nowDate = LocalDate.now();
 return LocalDateTimeUtil.isOverlap(LocalDateTime.of(nowDate, startTime1), LocalDateTime.of(nowDate, endTime1),
 LocalDateTime.of(nowDate, startTime2), LocalDateTime.of(nowDate, endTime2));
 }

 /**
     * Gets the start time of the month on the specified date
     * For example: 2023-09-30 00:00:00,000
 *
     * @param date date
     * @return start time of month
 */
 public static LocalDateTime beginOfMonth(LocalDateTime date) {
 return date.with(TemporalAdjusters.firstDayOfMonth()).with(LocalTime.MIN);
 }

 /**
     * Get the last time of the month on the specified date
     * For example: 2023-09-30 23:59:59,999
 *
     * @param date date
     * @return end of month
 */
 public static LocalDateTime endOfMonth(LocalDateTime date) {
 return date.with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX);
 }

 /**
     * Get the quarter of the specified date
 *
     * @param date date
     * @return quarter
 */
 public static int getQuarterOfYear(LocalDateTime date) {
 return (date.getMonthValue() - 1) / 3 + 1;
 }

 /**
     * Get the number of days since the specified date. If the specified date is after the current date, the result will be negative.
 *
     * @param dateTime date
     * @return Days difference
 */
 public static Long between(LocalDateTime dateTime) {
 return LocalDateTimeUtil.between(dateTime, LocalDateTime.now(), ChronoUnit.DAYS);
 }

 /**
     * Get today's start time
 *
     * @return today
 */
 public static LocalDateTime getToday() {
 return LocalDateTimeUtil.beginOfDay(LocalDateTime.now());
 }

 /**
     * Get yesterday's start time
 *
     * @return yesterday
 */
 public static LocalDateTime getYesterday() {
 return LocalDateTimeUtil.beginOfDay(LocalDateTime.now().minusDays(1));
 }

 /**
     * Get the start time of this month
 *
     * @return this month
 */
 public static LocalDateTime getMonth() {
 return beginOfMonth(LocalDateTime.now());
 }

 /**
     * Get the start time of this year
 *
     * @return this year
 */
 public static LocalDateTime getYear() {
 return LocalDateTime.now().with(TemporalAdjusters.firstDayOfYear()).with(LocalTime.MIN);
 }

 public static List<LocalDateTime[]> getDateRangeList(LocalDateTime startTime,
 LocalDateTime endTime,
 Integer interval) {
        // 1.1 Find the enumeration
 DateIntervalEnum intervalEnum = DateIntervalEnum.valueOf(interval);
        Assert.notNull(intervalEnum, "interval({}} cannot find the corresponding enumeration", interval);
        // 1.2 Align time
 startTime = LocalDateTimeUtil.beginOfDay(startTime);
 endTime = LocalDateTimeUtil.endOfDay(endTime);

        // 2. Loop, generate time range
 List<LocalDateTime[]> timeRanges = new ArrayList<>();
 switch (intervalEnum) {
 case HOUR:
 while (startTime.isBefore(endTime)) {
 timeRanges.add(new LocalDateTime[]{startTime, startTime.plusHours(1).minusNanos(1)});
 startTime = startTime.plusHours(1);
 }
 case DAY:
 while (startTime.isBefore(endTime)) {
 timeRanges.add(new LocalDateTime[]{startTime, startTime.plusDays(1).minusNanos(1)});
 startTime = startTime.plusDays(1);
 }
 break;
 case WEEK:
 while (startTime.isBefore(endTime)) {
 LocalDateTime endOfWeek = startTime.with(DayOfWeek.SUNDAY).plusDays(1).minusNanos(1);
 timeRanges.add(new LocalDateTime[]{startTime, endOfWeek});
 startTime = endOfWeek.plusNanos(1);
 }
 break;
 case MONTH:
 while (startTime.isBefore(endTime)) {
 LocalDateTime endOfMonth = startTime.with(TemporalAdjusters.lastDayOfMonth()).plusDays(1).minusNanos(1);
 timeRanges.add(new LocalDateTime[]{startTime, endOfMonth});
 startTime = endOfMonth.plusNanos(1);
 }
 break;
 case QUARTER:
 while (startTime.isBefore(endTime)) {
 int quarterOfYear = getQuarterOfYear(startTime);
 LocalDateTime quarterEnd = quarterOfYear == 4
 ? startTime.with(TemporalAdjusters.lastDayOfYear()).plusDays(1).minusNanos(1)
: startTime.withMonth(quarterOfYear * 3 + 1).withDayOfMonth(1).minusNanos(1);
 timeRanges.add(new LocalDateTime[]{startTime, quarterEnd});
 startTime = quarterEnd.plusNanos(1);
 }
 break;
 case YEAR:
 while (startTime.isBefore(endTime)) {
 LocalDateTime endOfYear = startTime.with(TemporalAdjusters.lastDayOfYear()).plusDays(1).minusNanos(1);
 timeRanges.add(new LocalDateTime[]{startTime, endOfYear});
 startTime = endOfYear.plusNanos(1);
 }
 break;
 default:
 throw new IllegalArgumentException("Invalid interval: " + interval);
 }
        // 3. Bottom line, the last time needs to be kept before endTime
 LocalDateTime[] lastTimeRange = CollUtil.getLast(timeRanges);
 if (lastTimeRange != null) {
 lastTimeRange[1] = endTime;
 }
 return timeRanges;
 }

 /**
     * Format time range
 *
     * @param startTime start time
     * @param endTime end time
     * @param interval time interval
     * @return time range
 */
 public static String formatDateRange(LocalDateTime startTime, LocalDateTime endTime, Integer interval) {
        // 1. Find the enumeration
 DateIntervalEnum intervalEnum = DateIntervalEnum.valueOf(interval);
        Assert.notNull(intervalEnum, "interval({}} cannot find the corresponding enumeration", interval);

        // 2. Loop, generate time range
 switch (intervalEnum) {
 case HOUR:
 return LocalDateTimeUtil.format(startTime, DatePattern.NORM_DATETIME_MINUTE_PATTERN);
 case DAY:
 return LocalDateTimeUtil.format(startTime, DatePattern.NORM_DATE_PATTERN);
 case WEEK:
 return LocalDateTimeUtil.format(startTime, DatePattern.NORM_DATE_PATTERN)
                        + StrUtil.format("(Week {})", LocalDateTimeUtil.weekOfYear(startTime));
 case MONTH:
 return LocalDateTimeUtil.format(startTime, DatePattern.NORM_MONTH_PATTERN);
 case QUARTER:
 return StrUtil.format("{}-Q{}", startTime.getYear(), getQuarterOfYear(startTime));
 case YEAR:
 return LocalDateTimeUtil.format(startTime, DatePattern.NORM_YEAR_PATTERN);
 default:
 throw new IllegalArgumentException("Invalid interval: " + interval);
 }
 }

 /**
     * Converts the given {@link LocalDateTime} to the number of seconds since Unix epoch time (1970-01-01T00:00:00Z).
 *
     * @param sourceDateTime the local date and time to be converted cannot be empty
     * @return Number of seconds since 1970-01-01T00:00:00Z (epoch second)
     * @throws NullPointerException if {@code sourceDateTime} is {@code null}
     * @throws DateTimeException if a time out of range or other time handling exception occurs during conversion
 */
 public static Long toEpochSecond(LocalDateTime sourceDateTime) {
 return TemporalAccessorUtil.toInstant(sourceDateTime).getEpochSecond();
 }

}
