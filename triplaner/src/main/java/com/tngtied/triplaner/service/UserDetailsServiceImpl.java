package com.tngtied.triplaner.service;

import com.tngtied.triplaner.JwtTokenProvider;
import com.tngtied.triplaner.UserRole;
import com.tngtied.triplaner.dto.TokenInfo;
import com.tngtied.triplaner.entity.Member;
import com.tngtied.triplaner.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	Logger logger = LoggerFactory.getLogger(getClass());

	private final UserRepository userRepository;
	private final JwtTokenProvider jwtTokenProvider;
	private final PasswordEncoder passwordEncoder;

	public Member create(String userName, String email, String password) {
		if (userRepository.findByUsername(userName).isPresent()) {
			System.out.println(">>" + userRepository.findByUsername(userName).toString());
			throw new DataIntegrityViolationException("USERNAME");
		}
		if (userRepository.findByEmail(email).isPresent()) {
			throw new DataIntegrityViolationException("EMAIL");
		}

		Member member = new Member(userName, passwordEncoder.encode(password), "USER", email);
		this.userRepository.save(member);
		logger.debug(">>user creation succeeded with username [{}], email [{}], password [{}]", member.getUsername(),
			member.getEmail(), password);
		return member;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Member> user = this.userRepository.findByUsername(username);
		if (user.isEmpty()) {
			throw new UsernameNotFoundException(">> Username not found.");
		}
		Member siteUserFound = user.get();
		List<GrantedAuthority> authorities = new ArrayList<>();

		if ("admin".equals(siteUserFound.getUsername())) {
			authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));
		} else {
			authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));
		}
		logger.debug(">> loadUserByUsername() called, found successfully");

		return new User(siteUserFound.getUsername(), siteUserFound.getPassword(), authorities);

	}

	public TokenInfo login(String username, String password) {
		try {
			UserDetails user = this.loadUserByUsername(username);
			if (passwordEncoder.matches(password, user.getPassword())) {
				return jwtTokenProvider.generateToken(user);
			} else {
				System.out.println(">> Password Doesn't match");
				throw new BadCredentialsException("패스워드가 일치하지 않습니다");
			}
		} catch (UsernameNotFoundException exception) {
			System.out.println(">> User not Found");
			throw new UsernameNotFoundException("유저가 존재하지 않습니다");
		}
	}
}
