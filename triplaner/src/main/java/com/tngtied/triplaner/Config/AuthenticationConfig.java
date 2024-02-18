package com.tngtied.triplaner.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tngtied.triplaner.AuthenticationExceptionHandler;
import com.tngtied.triplaner.CustomAccessDeniedHandler;

@Configuration
public class AuthenticationConfig {

	@Bean
	public ObjectMapper objectMapper() {

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		return objectMapper;
	}

	@Bean
	public AuthenticationExceptionHandler authenticationExceptionHandler(ObjectMapper objectMapper) {
		return new AuthenticationExceptionHandler(objectMapper);
	}

	@Bean
	public CustomAccessDeniedHandler customAccessDeniedHandler(ObjectMapper objectMapper) {
		return new CustomAccessDeniedHandler(objectMapper);
	}
}
