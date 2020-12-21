package com.code.taurus.common.init;

import com.code.taurus.api.entity.BizUser;
import com.code.taurus.api.enums.RoleEnum;
import com.code.taurus.api.model.BizUserVO;
import com.code.taurus.api.service.BizUserService;
import com.code.taurus.common.model.QueryModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 超级管理账号初始化
 *
 * @author 郑楷山
 **/

@Slf4j
@Order(1)
@Component
public class UserInitializer implements ApplicationListener<ApplicationReadyEvent> {

    @Value("${spring.datasource.url}")
    private String dbStr;

    /*@Value("${spring.redis.host}")
    private String redisStr;*/

    @Autowired
    private BizUserService bizUserService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        initialize();
    }

    public void initialize() {
        Optional<BizUserVO> optional = bizUserService.findFirstVO(new QueryModel().addCondition("fdName", "admin"));
        String name;
        if (!optional.isPresent()) {
            name = "admin";
            bizUserService.create(new BizUser()
                    .setFdName(name)
                    .setFdPassword("123123")
                    .setFdRole(RoleEnum.ADMIN));
            log.info("默认超级管理员创建成功");
        } else {
            name = optional.get().getFdName();
        }
        log.info("超级管理员:" + name);
        log.info("当前db:" + dbStr.substring(5, StringUtils.indexOf(dbStr, "?")));
//        log.info("当前redis:" + redisStr);
    }
}

