package cn.iocoder.yudao.framework.quartz.config;

import cn.iocoder.yudao.framework.quartz.core.scheduler.SchedulerManager;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Optional;

/**
 * Scheduled task Configuration
 */
@AutoConfiguration
@EnableScheduling // Start the scheduled task that comes with Spring
@Slf4j
public class YudaoQuartzAutoConfiguration {

    @Bean
    public SchedulerManager schedulerManager(Optional<Scheduler> scheduler) {
        if (!scheduler.isPresent()) {
            log.info("[定时任务 - 已禁用][参考 https:// doc.iocoder.cn/job/Open]");
            return new SchedulerManager(null);
        }
        return new SchedulerManager(scheduler.get());
    }

}
