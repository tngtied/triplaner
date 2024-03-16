package com.tngtied.triplaner.member.dto;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Data
@Getter
@RequiredArgsConstructor
public class UserLoginDTO {
	private String username;
	private String password;

	public UserLoginDTO(String username, String password) {
		this.username = username;
		this.password = password;
	}
}
