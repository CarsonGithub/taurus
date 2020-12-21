package com.code.taurus.common.config.security;

import com.code.taurus.common.constant.CommonConstant;
import com.code.taurus.common.model.SysSecurityUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;


@Data
@Component
@Slf4j
public class JWTAuthUtil {

    @Value("${jwt.expire-time}")
    private Long expireTime;

    @Value("${jwt.iss}")
    private String iss;

    @Value("${jwt.enable}")
    private Boolean enable;

    // 创建token
    public String createToken(String username, Collection<? extends GrantedAuthority> authority) {
        JwtBuilder jwtBuilder;
        jwtBuilder = Jwts.builder().signWith(SignatureAlgorithm.HS512, CommonConstant.TOKEN_SECRET)
                .setIssuer(iss)
                .setSubject(username)
                .setIssuedAt(new Date());
        jwtBuilder.addClaims(Collections.singletonMap("authority", authority));
        if (expireTime != null && expireTime > 0) {
            jwtBuilder.setExpiration(new Date(System.currentTimeMillis() + expireTime));
        }
        return jwtBuilder.compact();
    }


    // 从token中获取用户名
    public String getUsername(String token) {
        return getTokenBody(token).getSubject();
    }

    // 是否已过期
    public boolean isExpiration(String token) {
        Claims claims = getTokenBody(token);
        return claims.getExpiration() != null && claims.getExpiration().before(new Date());
    }

    public Claims getTokenBody(String token) {
        return Jwts.parser()
                .setSigningKey(CommonConstant.TOKEN_SECRET)
                .parseClaimsJws(token)
                .getBody();
    }


    /**
     * response配置token头部
     *
     * @param response, sysSecurityUser
     * @return javax.servlet.http.HttpServletResponse
     **/
    public void buildResponseTokenHeader(HttpServletResponse response, SysSecurityUser sysSecurityUser) {
        assert response != null;
        log.info(CommonConstant.LOG_JWT_AUTHENTICATION_SUCCESS, sysSecurityUser);
        String token = createToken(sysSecurityUser.getUsername(), sysSecurityUser.getAuthorities());
        response.setHeader(CommonConstant.TOKEN_HEADER, CommonConstant.TOKEN_PREFIX + token);
        response.setHeader(CommonConstant.TOKEN_REFRESH, "false");
        response.setContentType(CommonConstant.APPLICATION_UTF8);
    }

}
