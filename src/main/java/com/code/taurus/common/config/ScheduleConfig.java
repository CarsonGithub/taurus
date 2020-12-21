package com.code.taurus.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * 定时任务
 *
 * @author 郑楷山
 **/

@Configuration
@EnableScheduling
@Slf4j
public class ScheduleConfig {

    @Scheduled(cron = "${schedule.redis.refresh}")
    public void refreshLikesAndClicks() {
    }

}
