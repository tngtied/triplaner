package com.tngtied.triplaner.service;

import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tngtied.triplaner.entity.Member;
import com.tngtied.triplaner.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public Member create(String userName, String email, String password) {
		if (userRepository.findByUsername(userName).isPresent()) {
			System.out.println(">>" + userRepository.findByUsername(userName).toString());
			throw new DataIntegrityViolationException("USERNAME");
		}
		if (userRepository.findByEmail(email).isPresent()) {
			throw new DataIntegrityViolationException("EMAIL");
		}

		Member siteUser = new Member(userName, passwordEncoder.encode(password), "USER", email);
		this.userRepository.save(siteUser);
		System.out.printf(">>user creation success with username [%s], email [%s], password [%s]\n",
			siteUser.getUsername(), siteUser.getEmail(), password);
		return siteUser;
	}

	public Member loadMemberByUsername(String username) throws UsernameNotFoundException {
		Optional<Member> member = this.userRepository.findByUsername(username);
		if (member.isEmpty()) {
			throw new UsernameNotFoundException(">> Username not found.");
		}
		return member.get();
	}

}
