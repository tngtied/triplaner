package com.tngtied.triplaner.member.exception;

import com.tngtied.triplaner.common.ErrorCode;
import com.tngtied.triplaner.common.RestAPIException;
import com.tngtied.triplaner.member.exception.UserErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class UserException extends RestAPIException {
	UserErrorCode userErrorCode;

	public UserException(UserErrorCode userErrorcode) {
		super(userErrorcode);
	}
}