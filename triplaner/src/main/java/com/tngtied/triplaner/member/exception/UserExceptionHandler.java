package com.tngtied.triplaner.member.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class UserExceptionHandler {
	@ExceptionHandler(UserException.class)
	protected ResponseEntity<UserErrorResponseEntity> handleCustomException(UserException e) {
		return UserErrorResponseEntity.toResponseEntity(e.getUserErrorCode());
	}
}