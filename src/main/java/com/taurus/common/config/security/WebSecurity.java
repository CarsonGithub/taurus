package com.taurus.common.config.security;

import com.taurus.api.enums.RoleEnum;
import com.taurus.common.service.ISecurityUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * security配置
 *
 * @author 郑楷山
 **/

@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter  {

	@Autowired
	private JWTConfig jwtConfig;
	@Autowired
	private ISecurityUserService ISecurityUserService;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfigurationSource source =   new UrlBasedCorsConfigurationSource();
		CorsConfiguration corsConfiguration = new CorsConfiguration().applyPermitDefaultValues();
		corsConfiguration.addExposedHeader("Authorization");
		corsConfiguration.addExposedHeader("X-Refresh-Token");
		corsConfiguration.addAllowedMethod("*");
		((UrlBasedCorsConfigurationSource) source).registerCorsConfiguration("/**",corsConfiguration);
		return source;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		if (!jwtConfig.getEnable()) {
			http.cors().and().csrf().disable().authorizeRequests().anyRequest().permitAll();
			return;
		}
		http.cors().and().csrf().disable().authorizeRequests()
				// 放行
				.antMatchers("/health", "/env", "/swagger*/**",  "/static/**").permitAll()
				.antMatchers("/**/likes/**","/**/clicks/**").permitAll()

				.antMatchers(HttpMethod.PUT,"/test").hasAnyAuthority(RoleEnum.SUPER_ADMIN.getAuthority(),RoleEnum.USER.getAuthority())
				// 管理后台权限
				.antMatchers(HttpMethod.GET,"/**").permitAll()

				.antMatchers( "/**").hasAnyAuthority(RoleEnum.SUPER_ADMIN.getAuthority())
				// 需求有token登录
				.anyRequest().authenticated()
				.and()
				.addFilter(new JWTAuthenticationFilter(authenticationManager(), jwtConfig.getIss(), jwtConfig.getSecret(), jwtConfig.getExpireTime()))
				.addFilter(new JWTAuthorizationFilter(authenticationManager(), jwtConfig.getSecret(), ISecurityUserService, jwtConfig))
				.exceptionHandling().authenticationEntryPoint(new MyAuthenticationEntryPoint())
				.and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(ISecurityUserService).passwordEncoder(passwordEncoder);
	}


}
