package com.tngtied.triplaner.member.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tngtied.triplaner.member.entity.UserRole;
import com.tngtied.triplaner.member.entity.Member;
import com.tngtied.triplaner.authentication.jwt.JwtTokenProvider;
import com.tngtied.triplaner.authentication.jwt.TokenInfo;
import com.tngtied.triplaner.member.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserRepository userRepository;
	private final JwtTokenProvider jwtTokenProvider;
	private final PasswordEncoder passwordEncoder;
	private final MemberService memberService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Member siteUserFound = memberService.loadMemberByUsername(username);
		List<GrantedAuthority> authorities = new ArrayList<>();

		if ("admin".equals(siteUserFound.getUsername())) {
			authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));
		} else {
			authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));
		}
		log.debug(">> loadUserByUsername() called, found successfully");

		return new User(siteUserFound.getUsername(), siteUserFound.getPassword(), authorities);

	}

	public TokenInfo login(String username, String password) {
		UserDetails user = this.loadUserByUsername(username);

		if (passwordEncoder.matches(password, user.getPassword())) {
			return jwtTokenProvider.generateToken(user);
		} else {
			throw new BadCredentialsException("패스워드가 일치하지 않습니다");
		}
	}

	public Member getUserFromAuthorization(String authorization) {
		String username = jwtTokenProvider.getUsername(authorization.substring(7));
		return memberService.loadMemberByUsername(username);
	}
}
