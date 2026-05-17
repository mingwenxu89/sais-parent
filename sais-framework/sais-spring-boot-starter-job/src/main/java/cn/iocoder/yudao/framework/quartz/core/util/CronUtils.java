package cn.iocoder.yudao.framework.quartz.core.util;

import cn.hutool.core.date.LocalDateTimeUtil;
import org.quartz.CronExpression;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Utility class for Quartz Cron expressions
 *
 * @author Yudao Source Code
 */
public class CronUtils {

    /**
     * Verify that CRON expression is valid
     *
     * @param cronExpression CRON expression
     * @return Is it valid?
     */
    public static boolean isValid(String cronExpression) {
        return CronExpression.isValidExpression(cronExpression);
    }

    /**
     * Based on the CRON expression, obtain the next n satisfying execution times
     *
     * @param cronExpression CRON expression
     * @param n quantity
     * @return Execution time that satisfies the condition
     */
    public static List<LocalDateTime> getNextTimes(String cronExpression, int n) {
        // 1. Obtain CronExpression object
        CronExpression cron;
        try {
            cron = new CronExpression(cronExpression);
        } catch (ParseException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        // 2. Calculating from the current moment, n satisfying conditions
        Date now = new Date();
        List<LocalDateTime> nextTimes = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            Date nextTime = cron.getNextValidTimeAfter(now);
            // 2.1 If nextTime is null, it means there is no more valID time and exit the loop
            if (nextTime == null) {
                break;
            }
            nextTimes.add(LocalDateTimeUtil.of(nextTime));
            // 2.2 Switch now to the next trigger time;
            now = nextTime;
        }
        return nextTimes;
    }

}
