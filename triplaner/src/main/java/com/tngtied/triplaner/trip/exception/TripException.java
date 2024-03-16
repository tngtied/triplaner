package com.tngtied.triplaner.trip.exception;

import com.tngtied.triplaner.member.exception.UserErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TripException extends RuntimeException {
	TripErrorCode tripErrorCode;
}