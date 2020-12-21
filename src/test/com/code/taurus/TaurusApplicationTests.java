package com.code.taurus;

import jodd.util.ThreadUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@Slf4j
public class TaurusApplicationTests {

    @Autowired
    private RedissonClient redissonClient;

    @Test
    public void test() {
        for (int i = 0; i < 10; ++i) {
            new Thread(() -> {
                System.out.println("--> Run:" + Thread.currentThread().getName());
                lockTest();
            }).start();
        }
        ThreadUtil.sleep(600 * 1000);
    }

    private void lockTest() {
        RLock lock = redissonClient.getLock("lock");
        try {
            boolean got = lock.tryLock(4, 2, TimeUnit.SECONDS);
            if (got) {
                log.info("当前线程{},获取到锁!", Thread.currentThread().getName());
                ThreadUtil.sleep(1800);
            } else {
                log.info("当前线程{},失败!", Thread.currentThread().getName());
            }
        } catch (InterruptedException e) {
            log.error("出错了", e);
        } finally {
            if (Objects.nonNull(lock) && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

}
