package com.taurus.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * 定时任务
 *
 * @author 郑楷山
 **/

//@Configuration
//@EnableScheduling
@Slf4j
public class ScheduleConfig {


    /**
     * 主题: 每天定时将缓存值刷入,缓存7天有效期
     */
    @Scheduled(cron = "${schedule.redis.refresh}")
    public void refreshLikesAndClicks() {
    }

    // todo 后期补上定期把redis未同步成功数据再次同步并清空

}
