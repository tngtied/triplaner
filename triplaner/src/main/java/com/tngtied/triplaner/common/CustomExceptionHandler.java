package com.tngtied.triplaner.common;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {
	@ExceptionHandler(RestAPIException.class)
	protected ResponseEntity<ErrorResponseEntity> handleCustomException(RestAPIException e) {
		return ErrorResponseEntity.toResponseEntity(e.getErrorCode());
	}
}