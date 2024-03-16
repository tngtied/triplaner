package com.tngtied.triplaner.member.exception;

import com.tngtied.triplaner.member.exception.UserErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserException extends RuntimeException {
	UserErrorCode userErrorCode;
}