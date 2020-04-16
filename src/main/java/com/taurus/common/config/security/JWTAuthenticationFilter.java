package com.taurus.common.config.security;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.taurus.common.constant.CommonConstant;
import com.taurus.common.enums.ExceptionEnum;
import com.taurus.common.model.SysExceptionJson;
import com.taurus.common.model.SysSecurityUser;
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

	private final String jwtISS;

	private final String jwtSecret;

	private final Long jwtTokenExpireTime;

	private final AuthenticationManager authenticationManager;

	JWTAuthenticationFilter(AuthenticationManager authenticationManager,
							String jwtISS,
							String jwtSecret,
							Long jwtTokenExpireTime) {
		this.authenticationManager = authenticationManager;
		this.jwtISS = jwtISS;
		this.jwtSecret = jwtSecret;
		this.jwtTokenExpireTime = jwtTokenExpireTime;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest req,
	                                            HttpServletResponse res) throws AuthenticationException {
		try {
			UserDetails creds = JSONObject.parseObject(req.getInputStream(), SysSecurityUser.class);
			if (null == creds) {
				throw new BadCredentialsException(ExceptionEnum.PARAMETER_ERROR.getDescription());
			}
			if (StringUtils.isBlank(creds.getUsername()) || StringUtils.isBlank(creds.getPassword())) {
				throw new BadCredentialsException(ExceptionEnum.USER_PASSWORD_ERROR.getDescription());
			}
			return authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(
							creds.getUsername(),
							creds.getPassword(),
							new ArrayList<>())
			);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	// 成功验证后调用的方法
	@Override
	protected void successfulAuthentication(HttpServletRequest request,
	                                        HttpServletResponse response,
	                                        FilterChain chain,
	                                        Authentication authResult) throws IOException {
		UserDetails jwtUser = (UserDetails) authResult.getPrincipal();
		log.debug(CommonConstant.LOG_JWT_AUTHENTICATION_SUCCESS, jwtUser);
		String token = JWTAuthUtil.createToken(jwtUser.getUsername(),
				jwtISS,
				jwtSecret,
				jwtTokenExpireTime,
				jwtUser.getAuthorities());
		response.setHeader(CommonConstant.TOKEN_HEADER, CommonConstant.TOKEN_PREFIX + token);
		response.setContentType("application/json; charset=utf-8");
		Map<String, Object> map = new HashMap<>();
		map.put("fdRole", jwtUser.getAuthorities());
		map.put("fdName", jwtUser.getUsername());
		response.getWriter().write(JSON.toJSONString(map));
	}

	// 验证失败时候调用的方法
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
		response.setStatus(ExceptionEnum.USER_PASSWORD_ERROR.getStatus());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.getWriter().write(new SysExceptionJson(ExceptionEnum.USER_PASSWORD_ERROR).toJSONString());

	}

}
