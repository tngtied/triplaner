package com.tngtied.triplaner.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RestAPIException extends RuntimeException{
	private final ErrorCode errorCode;
}
