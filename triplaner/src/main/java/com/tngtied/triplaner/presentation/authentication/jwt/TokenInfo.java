package com.tngtied.triplaner.presentation.authentication.jwt;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TokenInfo {
	private String grantType;
	private String accessToken;
	private String refreshToken;
}
