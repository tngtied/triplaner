package com.tngtied.triplaner.presentation.authentication;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.tngtied.triplaner.presentation.authentication.response.CustomException;
import com.tngtied.triplaner.presentation.authentication.response.ErrorResponseEntity;

@ControllerAdvice
public class CustomExceptionHandler {
	@ExceptionHandler(CustomException.class)
	protected ResponseEntity<ErrorResponseEntity> handleCustomException(CustomException e) {
		return ErrorResponseEntity.toResponseEntity(e.getCustomErrorCode());
	}
}