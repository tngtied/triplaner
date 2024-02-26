package com.tngtied.triplaner.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CustomException extends RuntimeException {
	CustomErrorCode customErrorCode;
}