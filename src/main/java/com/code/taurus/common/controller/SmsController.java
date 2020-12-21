package com.code.taurus.common.controller;

import com.code.taurus.common.service.impl.SmsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * 控制器： 短信接口
 *
 * @author 郑楷山
 **/

@Slf4j
@Api(tags = "Global: 短信接口")
@RestController
@RequestMapping("/sms")
public class SmsController {

    @Autowired
    private SmsService smsService;

    @ApiOperation("获取验证码")
    @GetMapping("/{phoneNumber}")
    public String getCode(@Validated @ApiParam(value = "手机号码", required = true) @PathVariable @NotBlank @Pattern(regexp = "^[1]([3-9])[0-9]{9}$") String phoneNumber) {
        return smsService.getCode(phoneNumber);
    }

    @ApiOperation("判断验证码")
    @GetMapping("/{phoneNumber}/{code}")
    public void checkCode(@Validated @ApiParam(value = "手机号码", required = true) @PathVariable @NotBlank @Pattern(regexp = "^[1]([3-9])[0-9]{9}$") String phoneNumber,
                          @Validated @ApiParam(value = "验证码", required = true, example = "009487") @PathVariable @NotBlank @Size(min = 6, max = 6) String code) {
        smsService.checkCode(phoneNumber, code);
    }


}
