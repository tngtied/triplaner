package com.tngtied.triplaner.common;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
	String name();
	HttpStatus getHttpStatus();
	String getMessage();
}
