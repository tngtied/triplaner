package com.tngtied.triplaner.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tngtied.triplaner.presentation.authentication.jwt.RefreshToken;
import com.tngtied.triplaner.repository.RefreshTokenRepository;

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
