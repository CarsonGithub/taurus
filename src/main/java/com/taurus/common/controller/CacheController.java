package com.taurus.common.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * 控制器： 缓存处理
 *
 * @author 郑楷山
 **/

@Slf4j
@Api(tags = "Global: 缓存处理")
@RestController
@RequestMapping("/cache")
public class CacheController {

    @Autowired
    private RedisTemplate<String, Object> myRedisTemplate;
    @Autowired
    private StringRedisTemplate redisTemplate;
/*
    @ApiOperation("获取更新业务量")
    @GetMapping()
    public Object getBusinessCount() {
        try {
            BoundValueOperations<String, String> opr = redisTemplate.boundValueOps(CommonConstant.REDIS_UPDATE_COUNT);
            if (StringUtils.isBlank(opr.get())) {
                opr.set("0");
            }
            log.info("获取更新业务量!");
            return opr.get();
        } catch (RedisConnectionFailureException e) {
            throw new BusinessException(ExceptionEnum.REDIS_CONNECT_FIELD_ERROR);
        }

    }

    @ApiOperation("清空业务缓存")
    @DeleteMapping()
    public void clearBusinessCache() {
        try {
            BoundValueOperations<String, String> opr = redisTemplate.boundValueOps(CommonConstant.REDIS_UPDATE_COUNT);
            if (StringUtils.isBlank(opr.get())) {
                opr.set(CommonConstant.REDIS_DEFAULT_VALUE);
            }
            if (CommonConstant.REDIS_DEFAULT_VALUE.equalsIgnoreCase((String) opr.get())) {
                throw new BusinessException(ExceptionEnum.CLEAR_CACHE_ERROR);
            }
            Set<String> stringSet = myRedisTemplate.keys(CommonConstant.REDIS_DEFAULT_CACHE_NAMESPACE + "*");
            myRedisTemplate.delete(stringSet);
            opr.set(CommonConstant.REDIS_DEFAULT_VALUE);
            log.info("已清空业务缓存!");
        } catch (RedisConnectionFailureException e) {
            throw new BusinessException(ExceptionEnum.REDIS_CONNECT_FIELD_ERROR);
        }
    }

    @ApiOperation("强制清空业务缓存")
    @DeleteMapping("/force")
    public void forceClearBusinessCache() {
        try {
            Set<String> stringSet = myRedisTemplate.keys(CommonConstant.REDIS_DEFAULT_CACHE_NAMESPACE + "*");
            myRedisTemplate.delete(stringSet);
            log.info("已强制清空业务缓存!");
            BoundValueOperations<String, String> opr = redisTemplate.boundValueOps(CommonConstant.REDIS_UPDATE_COUNT);
            if (StringUtils.isBlank(opr.get())) {
                opr.set(CommonConstant.REDIS_DEFAULT_VALUE);
            }
        } catch (RedisConnectionFailureException e) {
            throw new BusinessException(ExceptionEnum.REDIS_CONNECT_FIELD_ERROR);
        }
    }*/

    //@ApiOperation("测试接口")
    //@GetMapping
    public void test() {
        ProcessBuilder pb = new ProcessBuilder("./test.sh");
        int runningStatus = 0;
        String s = null;
        try {
            Process p = Runtime.getRuntime().exec("/usr/local/taurus/node-js/test.sh");
            p.waitFor();
            try {
                runningStatus = p.waitFor();
            } catch (InterruptedException e) {
            }

        } catch (IOException | InterruptedException e) {
        }
        if (runningStatus != 0) {
        }
        return;
    }

}
