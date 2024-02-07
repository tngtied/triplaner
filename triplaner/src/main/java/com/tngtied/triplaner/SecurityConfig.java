package com.tngtied.triplaner;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtAuthenticationFilter jwtAuthenticationFilter;

	@Value("$base.path")
	private String basePath;

	// 나중에 허용할 url 취합해서 넣기
	@Bean
	SecurityFilterChain filterDefaultChain(HttpSecurity http) throws Exception {
		System.out.println(">> filterDefaultChain activated");
		http.securityMatcher("/api/v1/**")
			// .securityMatcher("/api/v1/*/*")
			.authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated());
		http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
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
