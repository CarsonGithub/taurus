package com.code.taurus.common.config.security;

import com.code.taurus.common.constant.CommonConstant;
import com.code.taurus.common.enums.ExceptionEnum;
import com.code.taurus.common.model.BusinessException;
import com.code.taurus.common.model.SysExceptionJson;
import com.code.taurus.common.model.SysSecurityUser;
import com.code.taurus.common.service.impl.SecurityUserService;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Slf4j
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private final SecurityUserService securityUserService;

    private final JWTAuthUtil jwtAuthUtil;

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, SecurityUserService securityUserService, JWTAuthUtil jwtAuthUtil) {
        super(authenticationManager);
        this.securityUserService = securityUserService;
        this.jwtAuthUtil = jwtAuthUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
        String tokenHeader = request.getHeader(CommonConstant.TOKEN_HEADER);
        if (tokenHeader == null || !tokenHeader.startsWith(CommonConstant.TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }
        try {
            UPAuTokenWithExpiration usernamePasswordAuthenticationToken = getAuthentication(tokenHeader);
            Long expireTime = jwtAuthUtil.getExpireTime();
            assert usernamePasswordAuthenticationToken != null;
            Date expiration = usernamePasswordAuthenticationToken.getExpiration();
            //现在时间 + 半衰期 > 有效时间
            if (expiration != null && new Date(System.currentTimeMillis() + (expireTime / 2)).after(expiration)) {
                response.setHeader(CommonConstant.TOKEN_REFRESH, "true");
            }
            // 如果请求头中有token，则进行解析，并且设置认证信息
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            super.doFilterInternal(request, response, chain);
        } catch (AuthenticationException e) {
            this.onUnsuccessfulAuthentication(request, response, e);
        }
    }

    @Override
    protected void onUnsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setStatus(ExceptionEnum.TOKEN_ERROR.getStatus());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        try {
            response.getWriter().write(new SysExceptionJson(ExceptionEnum.TOKEN_ERROR).toJSONString());
        } catch (IOException e) {
            throw new BusinessException(ExceptionEnum.SERVER_ERROR);
        }
    }

    // 这里从token中获取用户信息并新建一个token
    private UPAuTokenWithExpiration getAuthentication(String tokenHeader) {
        String token = tokenHeader.replace(CommonConstant.TOKEN_PREFIX, "");
        try {
            Claims claims = jwtAuthUtil.getTokenBody(token);
            if (StringUtils.isBlank(claims.getSubject())) {
                return null;
            }
            if (claims.getIssuer().equals(jwtAuthUtil.getIss())) {
                String name = claims.getSubject();
                SysSecurityUser userDetails = (SysSecurityUser) securityUserService.loadUserByUsername(name);
//                userDetails.setToken(token);
                UserContextHelper.setUser(userDetails);
                return new UPAuTokenWithExpiration(name, null, userDetails.getAuthorities(), claims.getExpiration());
            }
            return null;
        } catch (Exception e) {
            throw new BadCredentialsException(e.getMessage(), e);
        }

    }

}
