package com.schibsted.spain.friends.model;

import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

public class Password {

	private final String password;

	public Password(String password) {
		checkRestrictions(password);

		this.password = password;
	}

	String getPassword() {
		return password;
	}

	private void checkRestrictions(String password) {
		throwExceptionIf(password == null);

		throwExceptionIf(password.isEmpty());

		throwExceptionIf(password.length() < 8);

		throwExceptionIf(password.length() > 12);

		throwExceptionIf(compile("[^A-Za-z0-9]", Pattern.CASE_INSENSITIVE).matcher(password).find());
	}

	private void throwExceptionIf(boolean b) {
		if (b) {
			throw new IllegalArgumentException();
		}
	}

}
