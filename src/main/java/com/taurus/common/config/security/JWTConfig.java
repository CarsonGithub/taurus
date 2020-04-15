package com.taurus.common.config.security;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import  static com.taurus.common.constant.CommonConstant.TOKEN_SECRET;

@Data
@Component
public class JWTConfig {

	@Value("${jwt.expire-time}")
	private Long expireTime;

	@Value("${jwt.iss}")
	private String iss;

	@Value("${jwt.enable}")
	private Boolean enable;

	/**
	 * 私钥
	 */
	private String secret = TOKEN_SECRET;

}
