package com.tngtied.triplaner.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tngtied.triplaner.authentication.exception.AuthenticationExceptionHandler;
import com.tngtied.triplaner.authentication.exception.CustomAccessDeniedHandler;

@Configuration
public class AuthenticationConfig {

	@Bean
	public AuthenticationExceptionHandler authenticationExceptionHandler(ObjectMapper objectMapper) {
		return new AuthenticationExceptionHandler(objectMapper);
	}

	@Bean
	public CustomAccessDeniedHandler customAccessDeniedHandler(ObjectMapper objectMapper) {
		return new CustomAccessDeniedHandler(objectMapper);
	}
}
