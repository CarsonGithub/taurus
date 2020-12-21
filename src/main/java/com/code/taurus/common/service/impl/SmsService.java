package com.code.taurus.common.service.impl;

import com.code.taurus.common.enums.ExceptionEnum;
import com.code.taurus.common.model.BusinessException;
import io.jsonwebtoken.lang.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 服务: 短信接口服务
 *
 * @author 郑楷山
 **/

@Service
@Slf4j
public class SmsService {

    @Autowired
    private RedissonClient redissonClient;
    @Resource(name = "myRestTemplate")
    private RestTemplate myRestTemplate;

    @Value("${sms.juhe.url}")
    private String apiUrl;
    @Value("${sms.juhe.tplId}")
    private String tplId;
    @Value("${sms.juhe.limit}")
    private Long limit;
    @Value("${sms.juhe.appKey}")
    private String appKey;


    public String getCode(String phoneNumber) {
        RBucket<Object> bucket = redissonClient.getBucket("SMS:" + phoneNumber);
        Date intervalTime = new Date(System.currentTimeMillis() - limit);
        String value = (String) bucket.get();
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
            String result = "成功";
            if (HttpStatus.OK == responseEntity.getStatusCode()) {
                bucket.set(code + "," + new Date().getTime(), 5, TimeUnit.MINUTES);
            } else {
                result = "失败";
            }
            log.info("手机号码({})获取验证码:{},{}!", phoneNumber, code, result);
            return responseEntity.getBody();
        } catch (Exception e) {
            throw new BusinessException(ExceptionEnum.SMS_OPERATE_ERROR);
        }
    }

    public void checkCode(String phoneNumber, String code) {
        RBucket<Object> bucket = redissonClient.getBucket("SMS:" + phoneNumber);
        String value = (String) bucket.get();
        if (StringUtils.isBlank(value)) {
            throw new BusinessException(ExceptionEnum.SMS_CHECK_ERROR);
        }
        String[] vas = value.split(",");
        if (vas.length < 2 || !Objects.nullSafeEquals(vas[0], code)) {
            throw new BusinessException(ExceptionEnum.SMS_CHECK_ERROR);
        }
    }

}
