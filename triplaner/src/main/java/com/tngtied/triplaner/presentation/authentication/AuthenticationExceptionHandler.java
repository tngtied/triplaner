package com.tngtied.triplaner.presentation.authentication;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthenticationExceptionHandler implements AuthenticationEntryPoint {

	private final ObjectMapper objectMapper;

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException authException) throws IOException, ServletException {
		response.setContentType("application/json"); // JSON으로 사용자에게 전달
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Http Status 401로 전달
		response.setCharacterEncoding("utf-8"); // 본문 내용은 UTF-8
		response.getWriter().write(objectMapper.writeValueAsString(new LoginFailResponse("유효하지 않은 요청입니다."))); //
	}

	record LoginFailResponse(
		String message
	) {
	}
}
