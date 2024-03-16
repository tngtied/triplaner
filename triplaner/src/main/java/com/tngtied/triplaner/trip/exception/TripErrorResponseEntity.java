package com.tngtied.triplaner.trip.exception;

import org.springframework.http.ResponseEntity;

import com.tngtied.triplaner.member.exception.UserErrorCode;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TripErrorResponseEntity {
	private int status;
	private String code;
	private String message;

	public static ResponseEntity<TripErrorResponseEntity> toResponseEntity(TripErrorCode e) {
		return ResponseEntity
			.status(e.getHttpStatus())
			.body(TripErrorResponseEntity.builder()
				.status(e.getHttpStatus().value())
				.code(e.name())
				.message(e.getMessage())
				.build()
			);
	}
}