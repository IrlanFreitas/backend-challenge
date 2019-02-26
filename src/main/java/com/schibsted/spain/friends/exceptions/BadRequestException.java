package com.schibsted.spain.friends.exceptions;

public class BadRequestException extends RuntimeException {
	public BadRequestException(String cause) {
		super(cause);
	}
}
