package com.code.taurus.common.config.security;

import com.code.taurus.api.enums.RoleEnum;
import com.code.taurus.common.constant.CommonConstant;
import com.code.taurus.common.service.impl.SecurityUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * security配置
 *
 * @author 郑楷山
 **/

@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    @Autowired
    private JWTAuthUtil jwtAuthUtil;
    @Autowired
    private SecurityUserService securityUserService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration().applyPermitDefaultValues();
        corsConfiguration.addExposedHeader(CommonConstant.TOKEN_REFRESH);
        corsConfiguration.addExposedHeader(CommonConstant.TOKEN_HEADER);
        corsConfiguration.addAllowedHeader(CommonConstant.TOKEN_REFRESH);
        corsConfiguration.addAllowedHeader(CommonConstant.TOKEN_HEADER);
        corsConfiguration.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        if (!jwtAuthUtil.getEnable()) {
            http.cors().and().csrf().disable().authorizeRequests().anyRequest().permitAll();
            return;
        }
//        String[] SWAGGER_WHITELIST = {"/swagger-ui.html", "/swagger-ui/*", "/swagger-resources/**", "/v2/api-docs", "/v3/api-docs", "/webjars/**"};
        http.cors().and().csrf().disable().authorizeRequests()
                // 放行
//                .antMatchers(SWAGGER_WHITELIST).permitAll()

                .antMatchers("/**").hasAnyAuthority(RoleEnum.ADMIN.getAuthority())
                // 需求有token登录
                .anyRequest().authenticated()
                .and()
                .addFilter(new JWTAuthenticationFilter(authenticationManager(), jwtAuthUtil))
                .addFilter(new JWTAuthorizationFilter(authenticationManager(), securityUserService, jwtAuthUtil))
                .exceptionHandling().authenticationEntryPoint(new MyAuthenticationEntryPoint())
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(securityUserService).passwordEncoder(passwordEncoder);
    }

    /**
     * 配置地址栏不能识别 // 的情况
     */
    @Bean
    public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        //此处可添加别的规则,目前只设置 允许双 //
        firewall.setAllowUrlEncodedDoubleSlash(true);
        return firewall;
    }


}
