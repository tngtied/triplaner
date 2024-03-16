package com.tngtied.triplaner.member.exception;

import org.springframework.http.ResponseEntity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserErrorResponseEntity {
	private int status;
	private String code;
	private String message;

	public static ResponseEntity<UserErrorResponseEntity> toResponseEntity(UserErrorCode e) {
		return ResponseEntity
			.status(e.getHttpStatus())
			.body(UserErrorResponseEntity.builder()
				.status(e.getHttpStatus().value())
				.code(e.name())
				.message(e.getMessage())
				.build()
			);
	}
}