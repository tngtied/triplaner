package com.tngtied.triplaner.authentication;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tngtied.triplaner.authentication.exception.AuthenticationExceptionHandler;
import com.tngtied.triplaner.authentication.jwt.JwtTokenProvider;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtTokenProvider jwtTokenProvider;
	private final ObjectMapper objectMapper;

	@Override
	public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws
		ServletException,
		IOException {
		System.out.println(">> JwtAuthenticationFilter doFilter...");
		// Get jwt token and validate
		String token = resolveToken((HttpServletRequest)request);
		System.out.println(">> token provided: " + token);
		if (token != null && jwtTokenProvider.validateToken(token)) {
			Authentication authentication = jwtTokenProvider.getAuthentication(token);
			SecurityContextHolder.getContext().setAuthentication(authentication);

		} else {
			response.setContentType("application/json"); // JSON으로 사용자에게 전달
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Http Status 401로 전달
			response.setCharacterEncoding("utf-8"); // 본문 내용은 UTF-8
			response.getWriter()
				.write(objectMapper.writeValueAsString(
					new AuthenticationExceptionHandler.LoginFailResponse("유효하지 않은 요청입니다."))); //
			return;
		}
		chain.doFilter(request, response);
		// refresh token 검증하는 부분 필요

	}

	// Get authorization header and get Token Information
	private String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
			return bearerToken.substring(7);
		}

		return null;
	}
}