package com.code.taurus.common.config.security;

import com.code.taurus.common.constant.CommonConstant;
import com.code.taurus.common.enums.ExceptionEnum;
import com.code.taurus.common.model.SysExceptionJson;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 无路径访问权限
 *
 * @author 郑楷山
 **/

public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException {
        httpServletResponse.setStatus(ExceptionEnum.NO_PERMISSION_ACCESS.getStatus());
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpServletResponse.setContentType(CommonConstant.APPLICATION_UTF8);
        httpServletResponse.getWriter().write(new SysExceptionJson(ExceptionEnum.NO_PERMISSION_ACCESS).toJSONString());
    }
}
