package com.tngtied.triplaner.presentation.authentication.response;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CustomErrorCode {
	PLAN_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 계획이 존재하지 않습니다."),
	EMAIL_PATTERN_NOT_MATCH(HttpStatus.BAD_REQUEST, "이메일 형식에 부합하지 않습니다."),
	INCORRECT_USERNAME_SIZE(HttpStatus.BAD_REQUEST, "유저네임의 길이는 2글자 이상, 20글자 이하여야만 합니다."),
	INCORRECT_PASSWORD_SIZE(HttpStatus.BAD_REQUEST, "패스워드의 길이는 8글자 이상, 20글자 이하여야만 합니다."),
	DUPLICATE_USERNAME(HttpStatus.BAD_REQUEST, "해당하는 유저네임이 이미 존재합니다."),
	DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "해당하는 이메일이 이미 존재합니다."),
	AUTHOR_NOT_MATCH(HttpStatus.UNAUTHORIZED, "해당 계획에 접근할 권한이 없습니다.");

	private final HttpStatus httpStatus;
	private final String message;

}
