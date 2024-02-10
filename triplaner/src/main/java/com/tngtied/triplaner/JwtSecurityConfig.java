package com.tngtied.triplaner;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
	private final JwtTokenProvider jwtTokenProvider;

	@Override
	public void configure(HttpSecurity httpSecurity) {
		//UsernamePasswordAuthenticationFilter 뒤에 저장 생성자 주입 받은 JwtTokenProvider를 넣어준다.
		httpSecurity.addFilterBefore(
			new JwtAuthenticationFilter(jwtTokenProvider),
			UsernamePasswordAuthenticationFilter.class
		);
	}
}