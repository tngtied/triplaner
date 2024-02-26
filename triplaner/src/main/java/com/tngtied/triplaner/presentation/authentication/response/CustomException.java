package com.tngtied.triplaner.presentation.authentication.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CustomException extends RuntimeException {
	CustomErrorCode customErrorCode;
}