package com.tngtied.triplaner.trip.exception;

import org.springframework.http.HttpStatus;

import com.tngtied.triplaner.common.ErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TripErrorCode implements ErrorCode {
	PLAN_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 계획이 존재하지 않습니다."),
	AUTHOR_NOT_MATCH(HttpStatus.UNAUTHORIZED, "해당 계획에 접근할 권한이 없습니다.");

	private final HttpStatus httpStatus;
	private final String message;

}
