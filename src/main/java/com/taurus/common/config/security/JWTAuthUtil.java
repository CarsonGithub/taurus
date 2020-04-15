package com.taurus.common.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.GrantedAuthority;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

public class JWTAuthUtil {

	// 创建token
	public static String createToken(String username, String iss, String jwtSecret, Long jwtTokenExpireTime, Collection<? extends GrantedAuthority> authority) {
		JwtBuilder jwtBuilder = Jwts.builder().signWith(SignatureAlgorithm.HS512, jwtSecret)
				.setIssuer(iss)
				.setSubject(username)
				.setIssuedAt(new Date());
		jwtBuilder.addClaims(Collections.singletonMap("authority", authority));
		if (jwtTokenExpireTime != null && jwtTokenExpireTime > 0) {
			jwtBuilder.setExpiration(new Date(System.currentTimeMillis() + jwtTokenExpireTime));
		}
		return jwtBuilder.compact();
	}


	// 从token中获取用户名
	public static String getUsername(String token, String jwtSecret) {
		return getTokenBody(token, jwtSecret).getSubject();
	}

	public static Claims getTokenBody(String token, String jwtSecret) {
		return Jwts.parser()
				.setSigningKey(jwtSecret)
				.parseClaimsJws(token)
				.getBody();
	}

	// 是否已过期
	public static boolean isExpiration(String token, String jwtSecret) {
		Claims claims = getTokenBody(token, jwtSecret);
		return claims.getExpiration() != null && claims.getExpiration().before(new Date());
	}

}
