package com.taurus.common.service.impl;

import com.taurus.common.enums.ExceptionEnum;
import com.taurus.common.model.BusinessException;
import com.taurus.common.service.ISmsService;
import io.jsonwebtoken.lang.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.taurus.common.constant.CommonConstant.REDIS_GLOBAL;

/**
 * 服务: 短信接口服务
 *
 * @author 郑楷山
 **/

@Service
@Slf4j
public class SmsService implements ISmsService {

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Resource
    private RestTemplate myRestTemplate;

    @Value("${sms.juhe.url}")
    private String apiUrl;
    @Value("${sms.juhe.tplId}")
    private String tplId;
    @Value("${sms.juhe.limit}")
    private Long limit;
    @Value("${sms.juhe.appKey}")
    private String appKey;


    @Override
    public String getCode(String phoneNumber) {
        BoundValueOperations<String, String> opr = redisTemplate.boundValueOps(REDIS_GLOBAL + "SMS:"+phoneNumber);
        Date intervalTime = new Date(System.currentTimeMillis() - limit);
        String value = opr.get();
        if (StringUtils.isNotBlank(value)) {
            String[] vas = value.split(",");
            if (vas.length > 1 && new Date(Long.parseLong(vas[1])).after(intervalTime)) {
                throw new BusinessException(ExceptionEnum.SMS_REPEAT_ERROR);
            }
        }
        try {
            String code = RandomStringUtils.randomNumeric(6);
            String url = apiUrl + "?mobile=" + phoneNumber + "&tpl_id=" + tplId + "&tpl_value=%23code%23%3D" + code + "&key=" + appKey;
            ResponseEntity<String> responseEntity = myRestTemplate.getForEntity(url, String.class);
            if (HttpStatus.OK == responseEntity.getStatusCode()) {
                log.info("手机号码({})获取验证码:{},成功!", phoneNumber, code);
                opr.set(code + "," + new Date().getTime(), 5, TimeUnit.MINUTES);
                return responseEntity.getBody();
            } else {
                log.info("手机号码({})获取验证码:{},失败!", phoneNumber, code);
                return responseEntity.getBody();
            }
        } catch (Exception e) {
            throw new BusinessException(ExceptionEnum.SMS_OPERATE_ERROR);
        }
    }

    @Override
    public void checkCode(String phoneNumber, String code) {
        BoundValueOperations<String, String> opr = redisTemplate.boundValueOps(REDIS_GLOBAL + "SMS:"+phoneNumber);
        String value = opr.get();
        if (StringUtils.isBlank(value)) {
            throw new BusinessException(ExceptionEnum.SMS_CHECK_ERROR);
        }
        String[] vas = value.split(",");
        if (vas.length < 2 || !Objects.nullSafeEquals(vas[0], code)) {
            throw new BusinessException(ExceptionEnum.SMS_CHECK_ERROR);
        }
    }

}
