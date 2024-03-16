package com.tngtied.triplaner.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.tngtied.triplaner.authentication.exception.AuthenticationExceptionHandler;
import com.tngtied.triplaner.authentication.exception.CustomAccessDeniedHandler;
import com.tngtied.triplaner.authentication.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final AuthenticationExceptionHandler authenticationExceptionHandler;
	private final CustomAccessDeniedHandler customAccessDeniedHandler;

	@Value("$base.path")
	private String basePath;

	@Bean
	SecurityFilterChain filterDefaultChain(HttpSecurity http) throws Exception {
		log.debug(">> filterDefaultChain activated");
		http.
			securityMatcher("/api/v1/.*")
			.authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated());
		http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		http.exceptionHandling(
			exceptionHandler -> {
				exceptionHandler.authenticationEntryPoint(authenticationExceptionHandler);
				exceptionHandler.accessDeniedHandler(customAccessDeniedHandler);
				
			});

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
