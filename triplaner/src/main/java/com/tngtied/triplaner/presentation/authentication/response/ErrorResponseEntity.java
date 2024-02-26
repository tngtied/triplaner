package com.tngtied.triplaner.presentation.authentication.response;

import org.springframework.http.ResponseEntity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponseEntity {
	private int status;
	private String code;
	private String message;

	public static ResponseEntity<ErrorResponseEntity> toResponseEntity(CustomErrorCode e) {
		return ResponseEntity
			.status(e.getHttpStatus())
			.body(ErrorResponseEntity.builder()
				.status(e.getHttpStatus().value())
				.code(e.name())
				.message(e.getMessage())
				.build()
			);
	}
}