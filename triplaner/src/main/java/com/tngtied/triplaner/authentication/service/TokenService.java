package com.tngtied.triplaner.authentication.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tngtied.triplaner.authentication.jwt.RefreshToken;
import com.tngtied.triplaner.authentication.repository.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenService {
	private final RefreshTokenRepository refreshTokenRepository;

	@Transactional
	public void saveTokenInfo(Long username, String refreshToken, String accessToken) {
		refreshTokenRepository.save(new RefreshToken(String.valueOf(username), refreshToken, accessToken));
	}

	@Transactional
	public void removeRefreshToken(String accessToken) {
		refreshTokenRepository.findByAccessToken(accessToken)
			.ifPresent(refreshToken -> refreshTokenRepository.delete(refreshToken));
	}

}
