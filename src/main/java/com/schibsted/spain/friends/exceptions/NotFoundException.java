package com.schibsted.spain.friends.exceptions;

public class NotFoundException extends RuntimeException {
	public NotFoundException(String cause) {
		super(cause);
	}
}
