package com.code.taurus.common.config.security;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.code.taurus.common.constant.CommonConstant;
import com.code.taurus.common.enums.ExceptionEnum;
import com.code.taurus.common.model.SysExceptionJson;
import com.code.taurus.common.model.SysSecurityUser;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    private final JWTAuthUtil jwtAuthUtil;

    JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTAuthUtil jwtAuthUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtAuthUtil = jwtAuthUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
        try {
            UserDetails userDetails = JSONObject.parseObject(req.getInputStream(), SysSecurityUser.class);
            if (null == userDetails) {
                throw new BadCredentialsException(ExceptionEnum.PARAMETER_ERROR.getMessage());
            }
            if (StringUtils.isBlank(userDetails.getUsername()) || StringUtils.isBlank(userDetails.getPassword())) {
                throw new BadCredentialsException(ExceptionEnum.USER_PASSWORD_ERROR.getMessage());
            }
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userDetails.getUsername(),
                            userDetails.getPassword(),
                            new ArrayList<>())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 成功验证后调用的方法
    @SneakyThrows
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) {
        SysSecurityUser sysSecurityUser = (SysSecurityUser) authResult.getPrincipal();
        Map<String, Object> map = new HashMap<>();
        map.put("fdRole", sysSecurityUser.getAuthorities());
        map.put("fdName", sysSecurityUser.getUsername());
        jwtAuthUtil.buildResponseTokenHeader(response, sysSecurityUser);
        response.getWriter().write(JSON.toJSONString(map));
    }

    // 验证失败时候调用的方法
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        response.setStatus(ExceptionEnum.USER_PASSWORD_ERROR.getStatus());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setContentType(CommonConstant.APPLICATION_UTF8);
        response.getWriter().write(new SysExceptionJson(ExceptionEnum.USER_PASSWORD_ERROR).toJSONString());
    }

}
