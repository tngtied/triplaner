package com.tngtied.triplaner.authentication.jwt;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@RedisHash(value = "refreshToken", timeToLive = 14440)
public class RefreshToken {
	@Id
	private String id;
	private String refreshToken;
	@Indexed
	private String accessToken;
}