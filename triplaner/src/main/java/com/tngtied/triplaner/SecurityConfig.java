package com.tngtied.triplaner;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtAuthenticationFilter jwtAuthenticationFilter;

	@Value("$base.path")
	private String base_path;

	// 나중에 허용할 url 취합해서 넣기
	@Bean
	public FilterRegistrationBean<JwtAuthenticationFilter> filterDefaultChain(HttpSecurity http) throws Exception {
		System.out.println(">> before jwtAuthenticationFilter...");
		FilterRegistrationBean<JwtAuthenticationFilter> bean = new FilterRegistrationBean<>(jwtAuthenticationFilter);
		bean.addUrlPatterns("/*");  // 모든 요청에 대해서 필터 적용
		bean.addUrlPatterns(base_path + "/trip/*");
		bean.setOrder(0);   // 낮은 숫자일수록 우선순위
		return bean;
	}

	@Bean
	@Order(1)
	SecurityFilterChain filterUserChain(HttpSecurity http) throws Exception {
		http.securityMatcher("/api/v1/user/**")
			.authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll());
		return http.build();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
