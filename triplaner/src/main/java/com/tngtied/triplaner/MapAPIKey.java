package com.tngtied.triplaner;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Getter
@Component
public class MapAPIKey {
	@Value("${NAVER-KEY}")
	private String apiKey;

	@Override
	public String toString() {
		return "ApiKey [apiKey=" + apiKey + "]";
	}
}
