package com.tngtied.triplaner.trip.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.tngtied.triplaner.member.exception.UserErrorResponseEntity;
import com.tngtied.triplaner.member.exception.UserException;

@ControllerAdvice
public class TripExceptionHandler {
	@ExceptionHandler(TripException.class)
	protected ResponseEntity<TripErrorResponseEntity> handleCustomException(TripException e) {
		return TripErrorResponseEntity.toResponseEntity(e.getTripErrorCode());
	}
}